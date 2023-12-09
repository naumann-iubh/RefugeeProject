package com.lorbeer.refugeeapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lorbeer.refugeeapp.R;
import com.lorbeer.refugeeapp.databinding.ActivityContestantsForCourseBinding;
import com.lorbeer.refugeeapp.domain.Contestants;
import com.lorbeer.refugeeapp.viewmodel.ContestantsViewModel;

import java.util.List;

public class ContestantsForCourseActivity extends AppCompatActivity {

    private static final String TAG = "ContestantsForCourseActivity";

    private ActivityContestantsForCourseBinding binding;

    private ContestantsViewModel contestantsViewModel;

    private static String COURSE_IDENTIFIER = "COURSE_IDENTIFIER";
    private static String COURSE_NAME = "COURSE_NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContestantsForCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        showSpinner();

        final Intent intent = getIntent();
        final Integer id = intent.getIntExtra(COURSE_IDENTIFIER,9999);
        final String courseName = intent.getStringExtra(COURSE_NAME);

        binding.courseName.setText(courseName);

        contestantsViewModel = new ViewModelProvider(this).get(ContestantsViewModel.class);
        contestantsViewModel.queryContestantsForCourses(id, getApplicationContext());

        contestantsViewModel.getContestantsForCourses().observe(this, new Observer<List<Contestants>>() {
            @Override
            public void onChanged(List<Contestants> contestants) {

                if (contestants != null && !contestants.isEmpty()) {
                    final String[] names = contestants.stream().map(Contestants::getName).toArray(String[]::new);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ContestantsForCourseActivity.this, android.R.layout.simple_list_item_1, names);

                    binding.simpleListView.setAdapter(adapter);
                    hideSpinner();
                    binding.simpleListView.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void showSpinner(){
        ProgressBar progressSpinner = (ProgressBar)findViewById(R.id.pBar);
        progressSpinner.setVisibility(View.VISIBLE);
    }

    private void hideSpinner(){
        ProgressBar progressSpinner = (ProgressBar)findViewById(R.id.pBar);
        progressSpinner.setVisibility(View.GONE);
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