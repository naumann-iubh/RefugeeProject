package com.lorbeer.refugeeuserapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.NavUtils;
import androidx.core.view.WindowCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.gson.Gson;
import com.lorbeer.refugeeuserapp.MainActivity;
import com.lorbeer.refugeeuserapp.databinding.ActivityCourseDetailBinding;

import com.lorbeer.refugeeuserapp.R;
import com.lorbeer.refugeeuserapp.domain.Contestants;
import com.lorbeer.refugeeuserapp.domain.Course;
import com.lorbeer.refugeeuserapp.viewmodel.CourseViewModel;

import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    private static final String TAG = "CourseDetailActivity";

    private static String COURSE_IDENTIFIER = "COURSE_IDENTIFIER";

    private static String USER_IDENTIFIER = "current_user_name_email";
    private ActivityCourseDetailBinding binding;

    private CourseViewModel courseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCourseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.queryAllCourses(getApplicationContext());


        final Intent intent = getIntent();
        final Integer id = intent.getIntExtra(COURSE_IDENTIFIER, 9999);

        if (id != 9999) {
            courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
                @Override
                public void onChanged(List<Course> courses) {
                    Log.d(TAG, "intent id " + id);
                    Course chosenCourse = courses.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
                    if (chosenCourse != null) {
                        binding.name.setText("Name: \n" + chosenCourse.getName());
                        binding.shortDescription.setMovementMethod(new ScrollingMovementMethod());
                        binding.shortDescription.setText("Kurze Beschreibung: \n" + chosenCourse.getShortDescription());
                        final String dates = String.join("\n", chosenCourse.getDates());
                        binding.dates.setMovementMethod(new ScrollingMovementMethod());
                        binding.dates.setText("Termine: \n" + dates);
                        binding.place.setText("Ort: \n" + chosenCourse.getPlace());
                        binding.numContestants.setText("Anzahl an Teilnehmern: " + chosenCourse.getMaxContestants());
                        final String requirements = String.join("\n", chosenCourse.getRequirements());
                        binding.requirements.setMovementMethod(new ScrollingMovementMethod());
                        binding.requirements.setText("Anforderungen: \n" + requirements);
                    }
                }
            });
        }

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences(USER_IDENTIFIER, MODE_PRIVATE);
                String user = prefs.getString(USER_IDENTIFIER, null);
                if (user != null) {
                    final Contestants contestants = new Gson().fromJson(user, Contestants.class);
                    courseViewModel.enrollContestant(id, contestants);
                    courseViewModel.contestantEnrolled().observe(CourseDetailActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if (aBoolean) {
                                Toast.makeText(CourseDetailActivity.this, "Anmeldung erfolgreich", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(CourseDetailActivity.this, "Ein Fehler ist aufgetreten", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(CourseDetailActivity.this, "Bitte erstellen Sie erst einen User", Toast.LENGTH_LONG).show();
                }


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