package com.lorbeer.refugeeapp;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lorbeer.refugeeapp.databinding.ActivityMainBinding;
import com.lorbeer.refugeeapp.domain.Course;
import com.lorbeer.refugeeapp.viewmodel.CourseViewModel;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    private CourseViewModel courseViewModel;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);

        showSpinner();

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.queryAllCourses();

        courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                Log.v(TAG, courses.get(0).getName());

                final String[] names = courses.stream().map(Course::getName).toArray(String[]::new);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, names);

                binding.simpleListView.setAdapter(adapter);
                hideSpinner();
                binding.simpleListView.setVisibility(View.VISIBLE);
            }
        });


        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.add)
                        .setAction("Action", null).show();
            }
        });
    }

    private void showSpinner() {
        ProgressBar progressSpinner = (ProgressBar) findViewById(R.id.pBar);
        progressSpinner.setVisibility(View.VISIBLE);
    }

    private void hideSpinner() {
        ProgressBar progressSpinner = (ProgressBar) findViewById(R.id.pBar);
        progressSpinner.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}