package com.lorbeer.refugeeapp.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lorbeer.refugeeapp.domain.Contestants;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ContestantsRepository {

    private static final String TAG = "ContestantsRepository";

    private static ContestantsRepository INSTANCE;
    private static OkHttpClient client;

    private static String REQUEST_URL = "http://10.0.2.2:8080/data";

    public static ContestantsRepository getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ContestantsRepository();
            client = new OkHttpClient();
        }
        return INSTANCE;
    }

    private List<Contestants> dummyListContestants(Context context ) throws IOException {
        final InputStream jsonStream = context.getAssets().open("listContestants.json");
        final int size = jsonStream.available();
        byte[] buffer = new byte[size];
        jsonStream.read(buffer);
        jsonStream.close();
        String json=  new String(buffer, "UTF-8");

        final Type listType = new TypeToken<ArrayList<Contestants>>() {
        }.getType();
        List<Contestants> contestants = new Gson().fromJson(json, listType);
        return contestants;
    }

    public MutableLiveData<List<Contestants>> getAllContestants(Context context) {
        Log.v(TAG, "getAllContestants ");
        final Request request = new Request.Builder().url(REQUEST_URL + "/allContestants").get().build();
        final MutableLiveData<List<Contestants>> allContestants = new MutableLiveData<>();

       /* try {
            allContestants.setValue(dummyListContestants(context));
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
                    final Type listType = new TypeToken<ArrayList<Contestants>>() {
                    }.getType();
                    List<Contestants> contestants = new Gson().fromJson(data, listType);
                    allContestants.postValue(contestants);
                } else {
                    Log.e(TAG, "response code " + response.code());
                }
            }
        });
        return allContestants;
    }

    public MutableLiveData<Contestants> getContestantbyMail(String email, Context context) {
        Log.v(TAG, "getAllContestants ");
        final Request request = new Request.Builder().url(REQUEST_URL + "/getContestantByMail/" + email).get().build();
        final MutableLiveData<Contestants> contestant = new MutableLiveData<>();

        /*try {
            contestant.setValue(dummyListContestants(context).get(0));
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
                    Contestants con = new Gson().fromJson(data, Contestants.class);
                    contestant.postValue(con);
                } else {
                    Log.e(TAG, "response code " + response.code());
                }
            }
        });
        return contestant;
    }

    public MutableLiveData<List<Contestants>> getContestantsForCourse(int course_id, Context context) {
        Log.v(TAG, "getAllContestants ");
        final Request request = new Request.Builder().url(REQUEST_URL + "/getContestantsForCourse/"+course_id).get().build();
        final MutableLiveData<List<Contestants>> allContestants = new MutableLiveData<>();

        /*try {
            allContestants.setValue(dummyListContestants(context));
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
                    final Type listType = new TypeToken<ArrayList<Contestants>>() {
                    }.getType();
                    List<Contestants> contestants = new Gson().fromJson(data, listType);
                    allContestants.postValue(contestants);
                } else {
                    Log.e(TAG, "response code " + response.code());
                }
            }
        });
        return allContestants;
    }
}
