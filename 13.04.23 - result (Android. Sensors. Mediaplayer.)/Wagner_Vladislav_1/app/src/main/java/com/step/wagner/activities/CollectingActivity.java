package com.step.wagner.activities;

import static com.step.wagner.infrastructure.Utils.collectingRepository;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.step.wagner.R;
import com.step.wagner.adapters.CollectingAdapter;
import com.step.wagner.fragments.ContainerFragment;
import com.step.wagner.infrastructure.AppSettings;
import com.step.wagner.infrastructure.SessionState;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.entities.Accelerometer;
import com.step.wagner.models.entities.Collecting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CollectingActivity extends AppCompatActivity {

    //Датчики
    private SensorManager sensorManager;

    private MediaPlayer mediaPlayer;

    private Sensor sensorAccelerometer;
    private Sensor sensorApproximation;
    private Sensor sensorLight;

    //Фрагмент с выводом recycler view
    private ContainerFragment containerFragment;

    private TextView txvCollectingActivityTitle;

    private View currentView;

    //Коллекция сущностей для записи в БД
    private List<Collecting> collectingList;

    private Button btnStop;

    //Таймер для переодического запуска сбора информации с датчиков
    private Timer timer;

    private TimerTask timerTask;

    private int iterationIndex;

    private int duration = 120;
    private long startTimeMillis;

    //Дата и время начала сбора
    private Date startDateTime;

    //Осуществлялся ли сбор данных с датчика на текущей итерации - примитивная синхронизация записи в коллекцию
    private boolean isAccelerometerListened = false;
    private boolean isLightSensorListened = false;
    private boolean isApproximationListened = false;

    //Вывод текущих значений
    private TextView txvAccelerometer;
    private TextView txvLightSensor;
    private TextView txvApproximation;

    private ProgressBar progressBar;


    //Используемые для сбора датчики
    private String usedSensors;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collecting_circle);

        currentView = this.findViewById(android.R.id.content);
        txvCollectingActivityTitle = this.findViewById(R.id.collectingActivityTitle);

        AppSettings.getSetting(this);


        if (!AppSettings.listenAccelerometer && !AppSettings.listenApproximation && !AppSettings.listenLightSensor){
            Utils.showToast(this,"Для запуска в настройках нужно выбрать хотя бы 1 датчик!");
            finish();
            return;
        }

        //Получение датчиков
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Создать медиа проигрыватель
        mediaPlayer = MediaPlayer.create(this, R.raw.iphone_ring);

        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorApproximation = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //Получить фрагмент
        containerFragment = (ContainerFragment) getSupportFragmentManager().findFragmentById(R.id.resultFragment);
        containerFragment.getTextView().setVisibility(View.GONE);
        containerFragment.getRecyclerView().setVisibility(View.GONE);

        //Элементы вывода
        txvAccelerometer = findViewById(R.id.accelerometerTxv);
        txvApproximation = findViewById(R.id.approximationTxv);
        txvLightSensor = findViewById(R.id.lightTxv);
        btnStop = findViewById(R.id.btnStopCollecting);
        btnStop.setOnClickListener(v -> onStopClickHandler());

        progressBar = findViewById(R.id.progressBar);
        duration = AppSettings.queryDuration;
        progressBar.setMax(duration);

        collectingList = new ArrayList<>();

        timer = new Timer();
        timerTask = readSensors;

        //Очистить таблицу сбора
        collectingRepository.open();

        collectingRepository.cleanAll();

        collectingRepository.close();

        usedSensors = String.format("%s%s%s",
                AppSettings.listenAccelerometer ? "Акселерометр" : "",
                AppSettings.listenApproximation ? "\nПриближение" : "",
                AppSettings.listenLightSensor ? "\nОсвещённость" : "");

        removeTextViews();

        //Получить время запуска
        startTimeMillis = Utils.millisecondsToSeconds(SystemClock.uptimeMillis());
        startDateTime = Calendar.getInstance(Utils.timeZone).getTime();

        timer.schedule(timerTask,50,AppSettings.queryInterval);

        Utils.showSnackBar(currentView, String.format("Сбор начат. Длительность: %d сек.\nПериод опроса датчиков: %d сек.",
                duration,AppSettings.queryInterval/1000)
        );

        if (AppSettings.launchMediaPlayer) {
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }

    }

    //Сохранить состояние сессии
    @Override
    protected void onStop() {
        super.onStop();

        SessionState.writeValues(this);

    }

    //region TimerTask
    //Сбор информации с датчиков
    private TimerTask readSensors = new TimerTask() {
        @Override
        public void run() {

            //Создание и добавление в список объектп по умолчанию

            Collecting collectingItem = new Collecting();

            collectingItem.setStartDateTime(startDateTime);

            //Задать датчики, которые прослушиваются
            collectingItem.setSensorsTypes(usedSensors);

            collectingList.add(collectingItem);

            //Обнулить флаги прослушивания датчиков
            isAccelerometerListened = false;
            isLightSensorListened = false;
            isApproximationListened = false;

            //Регистрация слушателей
            registerListeners();

            //Остановка на некоторое время
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            unregisterListeners();

            //Записать время получения данных
            collectingItem = collectingList.get(iterationIndex);

            //Время получения данных с датчиков
            collectingItem.setReceivingTime(Calendar.getInstance(Utils.timeZone).getTime());

            collectingList.set(iterationIndex++,collectingItem);

            //Проверка прошедшего времени
            int currentTime = (int)(Utils.millisecondsToSeconds(SystemClock.uptimeMillis()) - startTimeMillis);

            progressBar.setProgress(currentTime,true);

            if (currentTime >= duration){


                /*Looper.prepare();

                new Handler().postDelayed(() -> runOnUiThread(completeExecution),100L);

                Looper.loop();*/

                //Задержка, чтобы закончить отрисовку progress bar
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    runOnUiThread(completeExecution);
                }

                //Остановка задачи и вывод результатов
                runOnUiThread(completeExecution);
            }

        }
    };

    //Действия по прошествии заданного времени сбора
    private Runnable completeExecution = this::onStopGetResult;


    private void hideOutPut(){
        txvAccelerometer.setVisibility(View.GONE);
        txvApproximation.setVisibility(View.GONE);
        txvLightSensor.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }
    //endregion

    //Остановка сбора по кнопке
    private void onStopClickHandler(){
        unregisterListeners();

        onStopGetResult();
    }

    //Остановка исполнения
    private void onStopGetResult(){
        timer.cancel();
        timerTask.cancel();

        //Если медиаплеер запускался
        if (AppSettings.launchMediaPlayer)
            stopMediaPlayer();

        int lastItemIndex = collectingList.size()-1;

        //Если в последнем элементе есть null-поля, тогда удалить элемент (сбор прерван до окончания требуемого времени сбора)
        if (Collecting.hasNull(collectingList.get(lastItemIndex)))
            collectingList.remove(lastItemIndex);

        iterationIndex = 0;

        hideOutPut();

        txvCollectingActivityTitle.setText("Результаты опроса датчиков");

        //Записать коллекцию сущностей в БД
        collectingRepository.open();

        collectingRepository.insertAll(collectingList);

        //Вывести результат в recyclerView
        containerFragment.getRecyclerView().setVisibility(View.VISIBLE);
        containerFragment.getRecyclerView().setAdapter(new CollectingAdapter(CollectingActivity.this,collectingRepository.getAll()));

        collectingRepository.close();

        Utils.showToast(this, "Сбор показателей завершен. Теперь можно производить обработку!");

        //Установить флаги для определения режима работы
        SessionState.isSessionHandled = false;
        SessionState.isNewCollecting = true;

    }

    //Остановка media player
    private void stopMediaPlayer(){
        mediaPlayer.stop();

        try {
            mediaPlayer.prepare();

            mediaPlayer.seekTo(0);
        } catch (Exception e) {
            Utils.showSnackBar(currentView,e.getMessage());
        }

    }

    //Убрать textView датчиков, которые не опрашиваются
    private void removeTextViews(){
        if (!AppSettings.listenAccelerometer)
            txvAccelerometer.setVisibility(View.GONE);

        if (!AppSettings.listenApproximation)
            txvApproximation.setVisibility(View.GONE);

        if (!AppSettings.listenLightSensor)
            txvLightSensor.setVisibility(View.GONE);

    }

    //region Слушатели датчиков

    //Зарегистрировать датчики
    private void registerListeners(){

        //Если в настройках задан опрос датчиков, тогда зарегистрировать их

        if (AppSettings.listenAccelerometer)
            sensorManager.registerListener(listenerAccelerometer,sensorAccelerometer,SensorManager.SENSOR_DELAY_UI);

        if (AppSettings.listenApproximation)
            sensorManager.registerListener(listenerApproximation,sensorApproximation,SensorManager.SENSOR_DELAY_UI);

        if (AppSettings.listenLightSensor)
            sensorManager.registerListener(listenerLight,sensorLight,SensorManager.SENSOR_DELAY_UI);


    }

    //Снять регистрацию слушателей с датчиков
    private void unregisterListeners(){
        sensorManager.unregisterListener(listenerAccelerometer,sensorAccelerometer);
        sensorManager.unregisterListener(listenerLight,sensorLight);
        sensorManager.unregisterListener(listenerApproximation,sensorApproximation);
    }

    //Слушатель акселерометра
    SensorEventListener listenerAccelerometer = new SensorEventListener() {
        @SuppressLint("DefaultLocale")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            if (!isAccelerometerListened) {

                //Записать значения акселерометра в коллекцию
                Collecting item = collectingList.get(iterationIndex);

                item.setAccelerometer(new Accelerometer(0,sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]));

                collectingList.set(iterationIndex,item);

                txvAccelerometer.setText(String.format("Акселерометр:\n X: %.3f\n Y: %.3f\n Z: %.3f",
                        sensorEvent.values[0],
                        sensorEvent.values[1],
                        sensorEvent.values[2]));

                isAccelerometerListened = true;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    //Слушатель датчика приближенния
    SensorEventListener listenerApproximation = new SensorEventListener() {
        @SuppressLint("DefaultLocale")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (!isApproximationListened) {

                //Записать значения датчика приближения в коллекцию
                Collecting item = collectingList.get(iterationIndex);

                item.setApproximation(sensorEvent.values[0]);

                collectingList.set(iterationIndex,item);

                txvApproximation.setText(String.format("Приближение: %.3f см", sensorEvent.values[0]));

                isApproximationListened = true;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    //Слушатель датчика освещённости
    SensorEventListener listenerLight = new SensorEventListener() {
        @SuppressLint("DefaultLocale")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (!isLightSensorListened) {

                //Добавить значения с писок
                Collecting item = collectingList.get(iterationIndex);

                item.setLight(sensorEvent.values[0]);

                //Задать элемент
                collectingList.set(iterationIndex,item);

                txvLightSensor.setText(String.format("Освещённость: %.3f ", sensorEvent.values[0]));


                isLightSensorListened = true;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
    //endregion

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.btnSettings:
                startActivity(new Intent(this,SettingsActivity.class));
            case R.id.btnExitFromActivity:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

}