package com.step.wagner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.step.wagner.R;
import com.step.wagner.fragments.SettingsFragment;
import com.step.wagner.infrastructure.AppSettings;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.settingsFragment, new SettingsFragment())
                .commit();

        findViewById(R.id.btnExit).setOnClickListener(v -> finish());
    }


    @Override
    protected void onStop() {
        super.onStop();

        AppSettings.getSetting(this);
    }
}