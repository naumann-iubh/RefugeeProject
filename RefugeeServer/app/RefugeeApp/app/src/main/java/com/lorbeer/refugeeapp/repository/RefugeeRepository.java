package com.lorbeer.refugeeapp.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lorbeer.refugeeapp.domain.Course;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RefugeeRepository {

    private static String baseUrl = "http://localhost:8080/refugeeServer/data";

    private static RefugeeRepository instance;

    private static OkHttpClient client;

    public static RefugeeRepository getInstance(){
        if (instance == null) {
            instance = new RefugeeRepository();
            client = new OkHttpClient();
        }
        return instance;
    }

    public List<Course> getAllCourses () {
        List<Course> courses = new ArrayList<>();
        final ObjectMapper om = new ObjectMapper();
        final String path = "/getAll";

        Request request = new Request.Builder().url(baseUrl+path).build();

        try(Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String result = response.body().string();
                System.out.println(result);
                courses = om.readValue(result, new TypeReference<List<Course>>() {
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return courses;
    }

}
