package com.lorbeer.refugeeapp.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lorbeer.refugeeapp.databinding.ActivityAddCourseBinding;
import com.lorbeer.refugeeapp.domain.Course;
import com.lorbeer.refugeeapp.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddCourseActivity extends AppCompatActivity {

    private ActivityAddCourseBinding binding;

    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.toolbar.setTitle("Angemeldete Teilnehmer");

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        final ArrayList<String> dates = new ArrayList<>();
        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddCourseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                final String date = String.format(Locale.GERMAN, "%d-%d-%d", dayOfMonth, (month + 1), year);

                                new TimePickerDialog(AddCourseActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        final String date = String.format(Locale.GERMAN, "%02d-%02d-%d %02d:%02d", dayOfMonth, (month + 1), year, hourOfDay, minute);

                                        dates.add(date);
                                        binding.dateList.append(date +",\n");
                                    }
                                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = binding.name.getText().toString();
                final String shortDescription = binding.shortDescription.getText().toString();
                final String place = binding.place.getText().toString();
                final Integer maxContestants = Integer.parseInt(binding.numContestants.getText().toString());
                final List<String> requirements = Arrays.asList(binding.requirements.getText().toString().split(","));

                final Course course = new Course();
                course.setName(name);
                course.setShortDescription(shortDescription);
                course.setDates(dates);
                course.setPlace(place);
                course.setMaxContestants(maxContestants);
                course.setRequirements(requirements);

                 courseViewModel.createCourse(course);

                courseViewModel.courseCreated().observe(AddCourseActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if ( aBoolean) {
                            Toast.makeText(AddCourseActivity.this, "Course created", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}