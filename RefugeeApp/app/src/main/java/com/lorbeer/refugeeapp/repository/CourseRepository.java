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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    private static String REQUEST_URL = "http://10.0.2.2:8080/data";

    private static String GEOCODE_URL = "http://nominatim.openstreetmap.org/search?q=%s&format=json&addressdetails=1";

    public static CourseRepository getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new CourseRepository();
            client = new OkHttpClient();
        }
        return INSTANCE;
    }

    public MutableLiveData<List<Course>> getAllCourses() {
        Log.v(TAG, "getAllCourses ");
        final Request request = new Request.Builder().url(REQUEST_URL + "/allCourses").get().build();
        final MutableLiveData<List<Course>> allCourses = new MutableLiveData<>();
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

    public MutableLiveData<Boolean> createCourse(Course course) {

        course = geocodeAddress(course);

        final MutableLiveData<Boolean> success = new MutableLiveData<>();

        final String json = new Gson().toJson(course);

        Log.v(TAG, json);

        final RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        final Request request = new Request.Builder().url(REQUEST_URL + "/createCourse").post(body).build();

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

    public MutableLiveData<Course> getCourseByName(String name) {
        Log.v(TAG, "getCourseByName " + name);
        final Request request = new Request.Builder().url(REQUEST_URL + "/getCoursesByName/" + name).get().build();
        final MutableLiveData<Course> course = new MutableLiveData<>();
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
