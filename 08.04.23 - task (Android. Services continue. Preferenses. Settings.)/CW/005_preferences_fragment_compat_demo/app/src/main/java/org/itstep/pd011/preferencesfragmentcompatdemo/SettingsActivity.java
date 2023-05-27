package org.itstep.pd011.preferencesfragmentcompatdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// Активность отображения фрагмента настроек
public class SettingsActivity extends AppCompatActivity {

    // SettingsActivity в качестве разметки интерфейса будет использовать ресурс
    // R.layout.activity_settings.
    // При запуске SettingsActivity будет загружать фрагмент SettingsFragment
    // в элемент с id settings_container.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.settings_container, new SettingsFragment())
            .commit();
    } // onCreate
} // class SettingsActivity