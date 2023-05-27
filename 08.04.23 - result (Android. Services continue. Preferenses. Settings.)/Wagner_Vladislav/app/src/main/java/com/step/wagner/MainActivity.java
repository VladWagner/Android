package com.step.wagner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.step.wagner.activities.AboutActivity;
import com.step.wagner.activities.ServiceActivity;
import com.step.wagner.activities.SettingsActivity;
import com.step.wagner.infrastructure.AppSettings;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnServiceHandling).setOnClickListener(v -> startServiceActivity());
        findViewById(R.id.btnSettingsMainActivity).setOnClickListener(v -> startSettingsActivity());
        findViewById(R.id.btnExit).setOnClickListener(v -> finish());
        findViewById(R.id.btnAbout).setOnClickListener(v -> startActivityAbout());

        //Прочитать настройки приложения
        AppSettings.getSetting(this);

    }

    //Запустить активность с сервисом
    private void startServiceActivity(){
        Intent intent = new Intent(this, ServiceActivity.class);

        startActivity(intent);
    }
    //Запустить активность с настройками
    private void startSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);

        startActivity(intent);
    }

    //Запустить активность о приложении
    private void startActivityAbout(){
        Intent intent = new Intent(this, AboutActivity.class);

        startActivity(intent);
    }


    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }



    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.menuBtnServiceActivity:
                startServiceActivity();
                break;
            case R.id.menuBtnSettingsActivity:
                startSettingsActivity();
                break;
            case R.id.menuBtnAbout:
                startActivityAbout();
                break;
            case R.id.menuBtnExit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

}