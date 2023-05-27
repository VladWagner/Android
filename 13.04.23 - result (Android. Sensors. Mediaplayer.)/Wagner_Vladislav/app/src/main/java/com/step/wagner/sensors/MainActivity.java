package com.step.wagner.sensors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.step.wagner.sensors.R;
import com.step.wagner.sensors.activities.AboutActivity;
import com.step.wagner.sensors.activities.CollectingActivity;
import com.step.wagner.sensors.activities.ProcessingActivity;
import com.step.wagner.sensors.activities.SettingsActivity;
import com.step.wagner.sensors.infrastructure.AppSettings;
import com.step.wagner.sensors.infrastructure.SessionState;
import com.step.wagner.sensors.infrastructure.Utils;
import com.step.wagner.sensors.repositories.AccelerometersRepository;
import com.step.wagner.sensors.repositories.AccelerometersStatisticsRepository;
import com.step.wagner.sensors.repositories.CollectingRepository;
import com.step.wagner.sensors.repositories.StatisticsRepository;

public class MainActivity extends AppCompatActivity {

    Button btnSensorsListening;
    Button btnSensorsHandling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSensorsListening = findViewById(R.id.btnSensorsListening);
        btnSensorsHandling = findViewById(R.id.btnHandling);
        findViewById(R.id.btnSettingsMainActivity).setOnClickListener(v -> startSettingsActivity());
        findViewById(R.id.btnExit).setOnClickListener(v -> finish());
        findViewById(R.id.btnAbout).setOnClickListener(v -> startActivityAbout());

        btnSensorsListening.setOnClickListener(v -> startCollectingActivity());
        btnSensorsHandling.setOnClickListener(v -> startProcessingActivity());

        //Прочитать настройки приложения
        AppSettings.getSetting(this);

        //Получить сохранённое состояние сенса
        SessionState.readValues(this);

        //Инициализировать репозитории
        if (Utils.accelerometersRepository == null || Utils.collectingRepository == null ||
                Utils.accelerometersStatisticsRepository == null || Utils.statisticsRepository == null){

            Utils.accelerometersRepository = new AccelerometersRepository(this);
            Utils.collectingRepository = new CollectingRepository(this);
            Utils.accelerometersStatisticsRepository = new AccelerometersStatisticsRepository(this);
            Utils.statisticsRepository = new StatisticsRepository(this);

        }

        btnSensorsListening.setEnabled(SessionState.isSessionHandled || Utils.collectingRepository.getCount() <= 0);
        btnSensorsHandling.setEnabled(Utils.statisticsRepository.getCount() > 0 || Utils.collectingRepository.getCount() > 0);

        //Если обработка сеанса не была проведена
        /*if (!Utils.isSessionHandled && Utils.collectingRepository.getCount() > 0)
            btnSensorsListening.setVisibility(View.GONE);

        if (Utils.statisticsRepository.getCount() <= 0 && Utils.collectingRepository.getCount() <= 0)
            btnSensorsHandling.setVisibility(View.GONE);*/

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        btnSensorsListening.setEnabled(SessionState.isSessionHandled || Utils.collectingRepository.getCount() <= 0);
        btnSensorsHandling.setEnabled(Utils.statisticsRepository.getCount() > 0 || Utils.collectingRepository.getCount() > 0);
    }

    //Запустить активность прослушивания датчиков
    private void startCollectingActivity(){

        //Проверить, есть значения в коллекции
        if (!SessionState.isSessionHandled && Utils.collectingRepository.getCount() > 0) {
            Utils.showToast(this,"Сначала нужно произвести обработку полученных данных!");
            return;
        }

        Intent intent = new Intent(this, CollectingActivity.class);

        startActivity(intent);
    }

    //Запустить активность обработок
    private void startProcessingActivity(){

        //Проверить, есть значения в коллекции
        if (Utils.statisticsRepository.getCount() <= 0 && Utils.collectingRepository.getCount() <= 0) {
            Utils.showToast(this,"Сначала нужно произвести сбор данных!");
            return;
        }
        Intent intent = new Intent(this, ProcessingActivity.class);

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

            case R.id.menuBtnCollectingActivity:
                startCollectingActivity();
                break;
            case R.id.menuBtnHandlingActivity:
                startProcessingActivity();
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