package org.itstep.pd011.sensorsdemo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Приложения для ознакомления с аппаратурой устройства
    // CPU-Z - для Windows, Android, ...
    // CPU-X

    TextView tvText;
    SensorManager sensorManager; // доступ к датчикам
    List<Sensor> sensors;        // коллекция датчиков

    Sensor sensorAccelerometer;  // датчик ускорения
    Sensor sensorProxy;          // датчик приближения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvText = findViewById(R.id.tvText);

        // системный сервис - менеджер датчиков
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // получить список всех датчиков в системе - Sensor.TYPE_ALL
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // получить ссылку на конкретный датчик ускорения - Sensor.TYPE_ACCELEROMETER
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorProxy = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    } // onCreate

    // получить список всех датчиков по клику на кнопку
    public void onClickSensList(View v) {
        // отписаться от слушателя событий датчика ускорения для экономии батареи
        sensorManager.unregisterListener(listenerAccelerometer, sensorAccelerometer);
        sensorManager.unregisterListener(listenerProximity, sensorProxy);

        // StringBuffer - потокобезопасный аналог StringBuilder
        StringBuffer sb = new StringBuffer();

        for (Sensor sensor : sensors) {
            sb
                // название, тип, производитель, версия датчика
                .append(String.format(Locale.UK, "name: %s, type: %d\nvendor: %s, version: %d\n",
                    sensor.getName(), sensor.getType(), sensor.getVendor(), sensor.getVersion()))

                // максимальное значение, точность, потребляемый ток
               .append(String.format(Locale.UK, "max: %f, resolution: %f, power: %f mkA\n",
                    sensor.getMaximumRange(), sensor.getResolution(), sensor.getPower()))
                .append("--------------------------------------\n");
        } // for i

        tvText.setText(sb);
    } // onClickSensList

    // Обработчик нажатия на кнопку "Ускорение" - подписка на отслеживание датчика
    public void onClickSensAccelerometer(View v) {
        sensorManager.unregisterListener(listenerProximity, sensorProxy);

        // регистрация слушателя события - получение данных от датчика
        sensorManager.registerListener(
            listenerAccelerometer,  // слушатель датчика,
            sensorAccelerometer,    // датчик ускорения,
            // 100_000);      // период опроса (> API9 в мкс)
            SensorManager.SENSOR_DELAY_UI); // другой способ задания периода опроса датчика
    } // onClickSensLight


    // Обработчик нажатия на кнопку "Приближение" - подписка на отслеживание датчика
    public void onClickSensProximity(View v) {
        sensorManager.unregisterListener(listenerAccelerometer, sensorAccelerometer);

        // регистрация слушателя события - получение данных от датчика
        sensorManager.registerListener(
            listenerProximity,  // слушатель датчика,
            sensorProxy,    // датчик ускорения,
            // 100_000);      // период опроса (> API9 в мкс)
            SensorManager.SENSOR_DELAY_UI); // другой способ задания периода опроса датчика
    } // onClickSensProximity

    @Override // рекомендуется освобождать ресурсы и отписываться от событий в этом обработчике
    protected void onPause() {
        super.onPause();

        // отписаться от слушателя событий датчиков для экономии батареи
        // sensorAccelerometer - слушаем этот датчик
        sensorManager.unregisterListener(listenerAccelerometer, sensorAccelerometer);
        sensorManager.unregisterListener(listenerProximity, sensorProxy);
    } // onPause

    // Реализация интерфейса SensorEventListener - слушатель
    // событий датчика
    SensorEventListener listenerAccelerometer = new SensorEventListener() {

        @Override // не интересует - не реализуем
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        // по изменению данных с датчика получить значение от датчика
        @Override public void onSensorChanged(SensorEvent event) {
            // датчик всегда возвращает значения в виде массива
            // (рекомендуеся массив значений копировать в собственный массив)
            // tvText.setText(String.valueOf(event.values[0]));
            tvText.setText(String.format(Locale.UK,
                    "Ускорение:\nX: %10.6f;\nY: %10.6f;\nZ: %10.6f",
                    event.values[0], event.values[1], event.values[2]));
        } // onSensorChanged
    };

    // Реализация интерфейса SensorEventListener - слушатель
    // событий датчика
    SensorEventListener listenerProximity = new SensorEventListener() {

        @Override // не интересует - не реализуем
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        // по изменению данных с датчика получить значение от датчика
        @Override public void onSensorChanged(SensorEvent event) {
            // датчик всегда возвращает значения в виде массива
            // (рекомендуеся массив значений копировать в собственный массив)
            // tvText.setText(String.valueOf(event.values[0]));
            tvText.setText(String.format(Locale.UK,"Приближение: %10.6f", event.values[0]));
        } // onSensorChanged
    };

    //region Работа с меню приложения
    @Override  // создание меню приложения
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu


    @Override // обработка выбора в меню приложения
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mniExit) {
            finish();
            return true;
        } // if
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class MainActivity