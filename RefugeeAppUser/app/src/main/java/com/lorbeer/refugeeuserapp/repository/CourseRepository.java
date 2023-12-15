package com.lorbeer.refugeeuserapp.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lorbeer.refugeeuserapp.domain.Contestants;
import com.lorbeer.refugeeuserapp.domain.Course;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseRepository {

    private static String GEOCODE_URL = "http://nominatim.openstreetmap.org/search?q=%s&format=json&addressdetails=1";
    private static final String TAG = "CourseRepository";

    private static CourseRepository INSTANCE;
    private static OkHttpClient client;

    private static String REQUEST_URL = "http://10.0.2.2:8080/data";
    private MutableLiveData<Boolean> success;

    public static CourseRepository getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new CourseRepository();
            client = new OkHttpClient();
        }
        return INSTANCE;
    }

    private List<Course> dummyListCourse(Context context) throws IOException {
        final InputStream jsonStream = context.getAssets().open("listCourses.json");
        final int size = jsonStream.available();
        byte[] buffer = new byte[size];
        jsonStream.read(buffer);
        jsonStream.close();
        String json = new String(buffer, "UTF-8");

        final Type listType = new TypeToken<ArrayList<Course>>() {
        }.getType();
        List<Course> courses = new Gson().fromJson(json, listType);
        return courses;
    }

    public MutableLiveData<List<Course>> getAllCourses(Context context) {
        Log.v(TAG, "getAllCourses ");
        final Request request = new Request.Builder().url(REQUEST_URL + "/allCourses").get().build();
        final MutableLiveData<List<Course>> allCourses = new MutableLiveData<>();

      /*  try {
            allCourses.setValue(dummyListCourse(context));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    final Type listType = new TypeToken<ArrayList<Course>>() {
                    }.getType();
                    List<Course> courses = new Gson().fromJson(data, listType);
                    allCourses.postValue(courses);
                } else {
                    Log.e(TAG, "response code " + response.code());
                }
            }
        });
        return allCourses;

    }

    public MutableLiveData<Course> getCourseByName(String name, Context context) {
        Log.v(TAG, "getCourseByName " + name);
        final Request request = new Request.Builder().url(REQUEST_URL + "/getCoursesByName/" + name).get().build();
        final MutableLiveData<Course> course = new MutableLiveData<>();

      /*  try {
            course.setValue(dummyListCourse(context).get(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();

                    Course c = new Gson().fromJson(data, Course.class);
                    course.postValue(c);
                } else {
                    Log.e(TAG, "response code " + response.code());
                }
            }
        });
        return course;
    }

    public MutableLiveData<Boolean> entrollContestant(Integer course_id, Contestants contestants) {
        Log.v(TAG, "entrollContestant");

        String json = new Gson().toJson(contestants);


        final RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));


        final Request request = new Request.Builder().url(REQUEST_URL + "/enrollContestant/" + course_id).post(body).build();
        final MutableLiveData<Boolean> success = new MutableLiveData<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, e.getMessage());
                success.postValue(false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    success.postValue(true);
                } else {
                    Log.e(TAG, "response code " + response.code());
                    success.postValue(false);
                }
            }
        });
        return success;
    }

    public MutableLiveData<Map<String, Double>> getAddress(String address) {
        final MutableLiveData<Map<String, Double>> addr = new MutableLiveData<>();
        final CompletableFuture<Map<String, Double>> geocode = geocodeAddress(address);

        try {
            final Map<String, Double> result = geocode.get();
            addr.setValue(result);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }
        return addr;
    }


    private CompletableFuture<Map<String, Double>> geocodeAddress(String address) {
        final String requestUrl = String.format(GEOCODE_URL, address);

        final Request request = new Request.Builder().url(requestUrl).get().build();

        CompletableFuture<Map<String, Double>> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            try (Response response = client.newCall(request).execute()) {
                String data = response.body().string();
                Log.v(TAG, "statuscode " + response.code());
                Log.v(TAG, data);
                final Type listType = new TypeToken<ArrayList<GeocoderResponse>>() {
                }.getType();
                final Map<String, Double> result = new HashMap<>();
                final List<GeocoderResponse> geocoderResponses = new Gson().fromJson(data, listType);
                result.put("lat", Double.parseDouble(geocoderResponses.get(0).getLat()));
                result.put("lon", Double.parseDouble(geocoderResponses.get(0).getLon()));
                completableFuture.complete(result);
                return null;
            }
        });
        return completableFuture;
    }


}
