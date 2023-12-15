package com.lorbeer.refugeeuserapp.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;

import androidx.core.app.NavUtils;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.gson.Gson;
import com.lorbeer.refugeeuserapp.databinding.ActivityUserBinding;

import com.lorbeer.refugeeuserapp.R;
import com.lorbeer.refugeeuserapp.domain.Contestants;

public class UserActivity extends AppCompatActivity {

    private static String USER_IDENTIFIER = "current_user_name_email";
    private ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = binding.name.getText().toString();
                final String email = binding.email.getText().toString();
                final Contestants user = new Contestants();
                user.setName(name);
                user.setEmail(email);
                final String json = new Gson().toJson(user);
                SharedPreferences preferences = getSharedPreferences(USER_IDENTIFIER, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.putString(USER_IDENTIFIER, json);
                editor.apply();
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