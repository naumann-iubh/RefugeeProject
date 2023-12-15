package com.lorbeer.refugeeuserapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lorbeer.refugeeuserapp.MainActivity;
import com.lorbeer.refugeeuserapp.domain.Contestants;
import com.lorbeer.refugeeuserapp.viewmodel.CourseViewModel;

public class QrCodeActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CAMERA = 1;
    private static String USER_IDENTIFIER = "current_user_name_email";

    private CourseViewModel courseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.queryAllCourses(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                initQRCodeScanner();
            }
        } else {
            initQRCodeScanner();
        }
    }

    private void initQRCodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scan a QR code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {

                final String course_id = result.getContents();
                SharedPreferences prefs = getSharedPreferences(USER_IDENTIFIER, MODE_PRIVATE);
                String user = prefs.getString(USER_IDENTIFIER, null);
                if (user != null) {
                    final Contestants contestants = new Gson().fromJson(user, Contestants.class);
                    courseViewModel.enrollContestant(Integer.parseInt(course_id), contestants);
                    courseViewModel.contestantEnrolled().observe(QrCodeActivity.this, new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if (aBoolean) {
                                Toast.makeText(QrCodeActivity.this, "Anmeldung erfolgreich", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(QrCodeActivity.this, "Ein Fehler ist aufgetreten", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(QrCodeActivity.this, "Bitte erstellen Sie erst einen User", Toast.LENGTH_LONG).show();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        Intent intent = new Intent(QrCodeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initQRCodeScanner();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}
