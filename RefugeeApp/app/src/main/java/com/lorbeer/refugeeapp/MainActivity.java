package com.lorbeer.refugeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lorbeer.refugeeapp.databinding.ActivityMainBinding;
import com.lorbeer.refugeeapp.domain.Course;
import com.lorbeer.refugeeapp.view.AddCourseActivity;
import com.lorbeer.refugeeapp.view.ContestantsForCourseActivity;
import com.lorbeer.refugeeapp.viewmodel.CourseViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static String COURSE_IDENTIFIER = "COURSE_IDENTIFIER";
    private static String COURSE_NAME = "COURSE_NAME";
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
        courseViewModel.queryAllCourses(getApplicationContext());

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

        binding.simpleListView.setOnItemClickListener((parent, view, position, id) -> {
            final Course course = courseViewModel.getAllCourses().getValue().get(position);
            final Intent intent = new Intent(MainActivity.this, ContestantsForCourseActivity.class);
            intent.putExtra(COURSE_IDENTIFIER, course.getId());
            intent.putExtra(COURSE_NAME, course.getName());
            startActivity(intent);

        });


        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
               startActivity(intent);
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