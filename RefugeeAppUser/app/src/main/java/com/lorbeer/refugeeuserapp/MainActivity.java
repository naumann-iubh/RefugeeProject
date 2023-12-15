package com.lorbeer.refugeeuserapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lorbeer.refugeeuserapp.databinding.ActivityMainBinding;
import com.lorbeer.refugeeuserapp.domain.Course;
import com.lorbeer.refugeeuserapp.view.CourseDetailActivity;
import com.lorbeer.refugeeuserapp.view.QrCodeActivity;
import com.lorbeer.refugeeuserapp.view.UserActivity;
import com.lorbeer.refugeeuserapp.viewmodel.CourseViewModel;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static String COURSE_IDENTIFIER = "COURSE_IDENTIFIER";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private ActivityMainBinding binding;

    private CourseViewModel courseViewModel;

    private ArrayAdapter<String> adapter;

    private List<Course> courseList;

    private List<Course> filteredCourses;

    private GPSTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);

        tracker = new GPSTracker(this);

        showSpinner();

        filteredCourses = new ArrayList<>();

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.queryAllCourses(getApplicationContext());

        courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                Log.v(TAG, courses.get(0).getName());
                courseList = courses;
                final String[] names = courses.stream().map(Course::getName).toArray(String[]::new);

                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, names);

                binding.simpleListView.setAdapter(adapter);
                hideSpinner();
                binding.simpleListView.setVisibility(View.VISIBLE);
            }
        });

        binding.simpleListView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "position " + position);
            Course course;
            if (!filteredCourses.isEmpty()) {
                course = filteredCourses.get(position);
            } else {
                course = courseViewModel.getAllCourses().getValue().get(position);
            }
            final Intent intent = new Intent(MainActivity.this, CourseDetailActivity.class);
            intent.putExtra(COURSE_IDENTIFIER, course.getId());
            startActivity(intent);

        });

        binding.searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {

                filteredCourses = courseList.stream().filter(c -> c.getName().contains(filter) || c.getShortDescription().contains(filter)).collect(Collectors.toList());
                final String[] names = filteredCourses.stream().map(Course::getName).toArray(String[]::new);
                adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, names);


                binding.simpleListView.setAdapter(adapter);
                return false;
            }
        });

        binding.position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPositionDialog();
            }
        });

    }

    private void showPositionDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setMessage("Bitte gib eine Adresse an oder wähle deinen aktuellen Standort");
        alert.setTitle("Standort Sortierung");

        final View customLayout =getLayoutInflater().inflate(R.layout.position_sort_dialog,null);
        alert.setView(customLayout);
        AlertDialog dialog = alert.create();
        EditText address = customLayout.findViewById(R.id.customLocation);
        EditText umkreis = customLayout.findViewById(R.id.umkreis);
        Button ok = customLayout.findViewById(R.id.customLocationButton);
        Button ownLocation = customLayout.findViewById(R.id.ownLocation);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer value = 25;
                String location = "münchen";
                if (!umkreis.getText().toString().isEmpty()) {
                    value = Integer.parseInt(umkreis.getText().toString());
                }
                if (!address.getText().toString().isEmpty()) {
                    location = address.getText().toString();
                }

                customLocation(location, value);
                dialog.cancel();
            }
        });

        ownLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer value = 25;
                if (!umkreis.getText().toString().isEmpty()) {
                    value = Integer.parseInt(umkreis.getText().toString());
                }
                ownLocation(value);
                dialog.cancel();
            }
        });


        dialog.show();
    }

    private void customLocation(String address, Integer umkreis) {
        courseViewModel.geocodeAddress(address);
        final Map<Double, Course> distanceMap = new HashMap<>();

        courseViewModel.geocoded().observe(MainActivity.this, new Observer<Map<String, Double>>() {
            @Override
            public void onChanged(Map<String, Double> coordinates) {
                Location customLocation = new Location("customLocation");
                customLocation.setLatitude(coordinates.get("lat"));
                customLocation.setLongitude(coordinates.get("lon"));
                for (Course course : courseList) {
                    if (course.getLat() != null && course.getLon() != null) {
                        Location locationCourse = new Location("course");
                        locationCourse.setLatitude(Double.parseDouble(course.getLat()));
                        locationCourse.setLongitude(Double.parseDouble(course.getLon()));
                        double distance = customLocation.distanceTo(locationCourse);
                        if (distance < umkreis*1000) {
                            distanceMap.put(distance, course);
                        }

                    }
                }
            }
        });
        Map<Double, Course> sortedDistanceMap = new TreeMap<>(distanceMap);
        filteredCourses = new ArrayList<>(sortedDistanceMap.values());

        final String[] names = filteredCourses.stream().map(Course::getName).toArray(String[]::new);
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, names);


        binding.simpleListView.setAdapter(adapter);
    }

    private void ownLocation(Integer umkreis){
        final Map<Double, Course> distanceMap = new HashMap<>();
        if (tracker.isGPSTrackingEnabled) {
            Location current = tracker.location;
            for (Course course : courseList) {
                if (course.getLat() != null && course.getLon() != null) {
                    Location locationCourse = new Location("course");
                    locationCourse.setLatitude(Double.parseDouble(course.getLat()));
                    locationCourse.setLongitude(Double.parseDouble(course.getLon()));
                    double distance = current.distanceTo(locationCourse);
                    if (distance < umkreis*1000) {
                        distanceMap.put(distance, course);
                    }

                }
            }
        }
        Map<Double, Course> sortedDistanceMap = new TreeMap<>(distanceMap);
        filteredCourses = new ArrayList<>(sortedDistanceMap.values());

        final String[] names = filteredCourses.stream().map(Course::getName).toArray(String[]::new);
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, names);


        binding.simpleListView.setAdapter(adapter);
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
            showUserSettings();
            return true;
        }
        if (id == R.id.qrcode) {
            showQrCodeScanner();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showUserSettings() {
        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        startActivity(intent);
    }

    private void showQrCodeScanner() {
        Intent intent = new Intent(MainActivity.this, QrCodeActivity.class);
        startActivity(intent);
    }

}