package com.lorbeer.refugeeapp.repository;


import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lorbeer.refugeeapp.R;
import com.lorbeer.refugeeapp.domain.Contestants;
import com.lorbeer.refugeeapp.domain.Course;
import com.lorbeer.refugeeapp.domain.GeocoderResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CourseRepository {

    private static final String TAG = "CourseRepository";

    private static CourseRepository INSTANCE;
    private static OkHttpClient client;

    private static String REQUEST_URL = "url";

    private static String GEOCODE_URL = "http://nominatim.openstreetmap.org/search?q=%s&format=json&addressdetails=1";

    public static CourseRepository getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new CourseRepository();
            client = new OkHttpClient();
        }
        return INSTANCE;
    }

    public MutableLiveData<List<Course>> getAllCourses() {
        Log.v(TAG, "getAllCoursesCalles ");
        //  Request request = new Request.Builder().url(REQUEST_URL).get().build();
        final MutableLiveData<List<Course>> allCourses = new MutableLiveData<>();
       /* try (Response response = client.newCall(request).execute()) {
            String data = response.body().string();
            Log.v(TAG, "statuscode " + response.code());
            Log.v(TAG, data);
            final Type listType = new TypeToken<ArrayList<Course>>() {
            }.getType();
            List<Course> courses = new Gson().fromJson(data, listType);
            allCourses.setValue(courses);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        Course course = new Course();
        course.setName("testname");

        return allCourses;

    }

    public MutableLiveData<Boolean> createCourse(Course course) {

        course = geocodeAddress(course);

        final MutableLiveData<Boolean> success = new MutableLiveData<>();

        final String json = new Gson().toJson(course);

        Log.v(TAG, json);

        final RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        final Request request = new Request.Builder().url(REQUEST_URL).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, e.getMessage());
                success.setValue(false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    success.setValue(true);
                } else {
                    Log.e(TAG, "response code " + response.code());
                    success.setValue(false);
                }
            }
        });

        return success;
    }

    public MutableLiveData<List<Contestants>> getContestantsForCourse(Integer id) {
        final MutableLiveData<List<Contestants>> contestants = new MutableLiveData<>();

        final Request request = new Request.Builder().url(REQUEST_URL + "/" + id).get().build();

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
                    List<Contestants> cons = new Gson().fromJson(data, listType);
                    contestants.setValue(cons);
                } else {
                    Log.e(TAG, "response code " + response.code());

                }
            }
        });

        return contestants;
    }

    private Course geocodeAddress(Course course) {
        final String requestUrl = String.format(GEOCODE_URL, course.getPlace());

        final Request request = new Request.Builder().url(requestUrl).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (response.isSuccessful()) {
                        final String json = responseBody.string();

                        final Type listType = new TypeToken<ArrayList<GeocoderResponse>>() {
                        }.getType();
                        final List<GeocoderResponse> geocoderResponses = new Gson().fromJson(json, listType);
                        course.setLat(geocoderResponses.get(0).getLat());
                        course.setLon(geocoderResponses.get(0).getLon());
                    }
                }
            }
        });

        return course;

    }


}
