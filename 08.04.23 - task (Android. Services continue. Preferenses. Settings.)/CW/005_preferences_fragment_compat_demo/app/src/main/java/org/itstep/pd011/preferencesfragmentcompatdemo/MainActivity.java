package org.itstep.pd011.preferencesfragmentcompatdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txvSettingsText;
    TextView txvEncode;

    boolean enabled;
    boolean encode;
    String login;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvSettingsText = findViewById(R.id.txvSettingsText);
        txvEncode = findViewById(R.id.txvEncode);

    } // onCreate

    @Override
    public void onResume() {
        super.onResume();

        // считываем значения настроек из SharedPreferences по именаам ключей,
        // определенных в xml/settings.xml
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        enabled = prefs.getBoolean("enabled", false);
        encode = prefs.getBoolean("encode", false);
        login = prefs.getString("login", "не установлено");
        email = prefs.getString("email", "не задан");

        // применим прочитанные значения
        txvSettingsText.setText(login + ": " + email);
        txvSettingsText.setVisibility(enabled?View.VISIBLE:View.INVISIBLE);
        txvEncode.setText(encode?"Контент шифруется":"Контент не шифруется");
    } // onResume

    // переход на активность настроек - эта активность содержит только
    // фрагмент с настройками
    public void setPrefs(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    } // setPrefs
} // class MainActivity