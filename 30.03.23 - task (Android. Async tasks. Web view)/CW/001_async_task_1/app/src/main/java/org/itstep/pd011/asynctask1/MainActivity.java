package org.itstep.pd011.asynctask1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // экземпляр AsyncTask. Проблема - при смене ориентации экрана
    // объект пересоздается...
    MyTask mt;

    TextView tvInfo, tvDate;
    Button btnStart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.tvInfo);
        tvDate = findViewById(R.id.tvDate);
        btnStart = findViewById(R.id.btnStart);

        // вывод текущей даты и времени в TextView
        foo();  // метод, имеющий доступ к UI - просто демонстрация
    } // onCreate

    // создание и запуск задачи
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnStart:
                mt = new MyTask();   // создание объекта AsyncTask
                mt.execute();        // запуск задачи AsyncTask - ВСЕГДА выполняется в потоке UI
                break;

            case R.id.btnTestUI:
                foo();  // работа продолжается...
                break;

            case R.id.btnQuit:
                finish();
                break;
        } // switch
    } // onClick

    // вывод текущей даты и времени в TextView
    private void foo() {
        // демонстрация замерзания UI-потока
        // Utils.sleepMs(2000);

        tvDate.setText("Работа продолжается, сейчас: " + DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()));
    } // foo

    // Класс для реализации AsyncTask - подкласс AsyncTask
    // Четыре правила работы с AsyncTask
    // ☼ объект AsyncTask д.б. создан в UI-потоке
    // ☼ метод execute() д.б. вызван в UI-потоке
    // ☼ не вызвать напрямую методы onPreExecute(), onPostExecute(), doInBackground(),
    //   onProgressUpdate()
    //
    //   onPreExecute()     - вызывается однократно, до запуска doInBackground(),
    //                        имеет доступ к UI
    //   doInBackground()   - реализация задачи, обработки занимающей большое время,
    //                        не имеет доступа к UI, выполняется в новом потоке
    //   onProgressUpdate() - периодически вызывается при работе doInBackground(),
    //                        имеет доступ к UI
    //   onPostExecute()    - вызывается однократно, после завершения doInBackground(),
    //                        имеет доступ к UI
    // ☼ запуск AsyncTask   - только один раз (до завершения текущей задачи новую не запускать)

    // Параметры: тип входного параметра, тип промежуточного результата, тип результата
    class MyTask extends AsyncTask<Void, Void, Void> {

        // вызывается системой времени исполнения до работы doInBackground(),
        // имеет доступ к потоку UI
        @Override protected void onPreExecute() {
            super.onPreExecute();

            // пример доступа к UI
            tvInfo.setText("AsyncTask стартовала");  // вывести сообщение
            btnStart.setEnabled(false);              // запретить кнопку
        } // onPreExecute

        // Будет выполнен в новом потоке, здесь решаем все свои 'тяжелые' задачи,
        // не имеет доступа к UI.
        // Передача списка параметров переменной длины
        // т.е. params - это массив параметров
        // имяМетода(1, 2, 3);
        // имяМетода(1, 2, 3, 5, 8, 9);
        // имяМетода(1, 2);
        // доступ к параметрам в методе:
        // тип имяМетода(типПараметра... массивПараметорв) {
        //     for(парам:массивПараметорв) { ... }
        // }
        // !! В этом примере параметры не используются !!
        @Override  protected Void doInBackground(Void... params) {
            Utils.sleepMs(8_000); // имитация длительной обработки

            // возвращаем null, т.к. тип результата Void
            return null;
        } // doInBackground

        // вызывается системой времени исполнения после работы doInBackground(),
        // имеет доступ к потоку UI
        @Override protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // пример доступа к UI
            tvInfo.setText("AsyncTask завершена");  // вывести сообщение
            btnStart.setEnabled(true);              // разрешить кнопку
        } // onPostExecute
    } // class MyTask
} // class MAinActivity