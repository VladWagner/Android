package org.itstep.pd011.asynctask2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // сигнатура записи, выводимой в Logcat
    private static final String SIGN = "AT_02";

    MyTask mt;     // экземпляр AsyncTask

    TextView tvInfo;
    Button btnStart;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.tvInfo);
        btnStart = findViewById(R.id.btnStart);
    } // onCreate

    // запуск задачи AsyncTask
    public void onClick(View v) {
        // часть параметров передаем через конструктор
        mt = new MyTask(4);

        // передача параметров в AsyncTask - все эти параметры
        // принимаются методом doInBackground
        mt.execute("file_path_1", "file_path_2", "file_path_3", "file_path_4", "file_path_5");
        btnStart.setEnabled(false);
    } // onClick

    // AsyncTask тут:
    // параметры
    // входной параметр - имена файлов                           String
    // промежуточный результат - количество обработанных файлов  Integer
    // выходной результат - не используется                      Void
    class MyTask extends AsyncTask<String, Integer, Void> {
        private int totalFiles;

        public MyTask(int totalFiles) {
            this.totalFiles = totalFiles;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvInfo.setText(String.format("Загружаем %d файлов", totalFiles));
        } // onPreExecute

        // Имитация загрузки файлов - тип параметра совпадает
        // с типом входного параметра класса
        @Override  protected Void doInBackground(String... urls) {
            Log.d(SIGN, "коннект");

            int cnt = 0;
            for (String url : urls) {   // для каждого элемента из массива urls
                downloadFile(url);      // имитация загрузки файла

                // этот вызов приводит к вызову метода onProgressUpdate()
                // тоже имеет список параметров переменной длины
                // тип параметров этого метода - второй тип при объявлении класса MyTask
                publishProgress(cnt++);  // выводим промежуточные результаты
            } // for i

            // имитация отсоединения через 1 с
            Utils.sleepMs(1_000);
            Log.d(SIGN, "дисконнект");

            // возвращаем null, т.к. указан тип результата Void
            return null;
        } // doInBackground


        // вывод промежуточного результата в UI
        // ‼ параметры переменной длины
        // тип параметров этого метода - второй тип при объявлении класса MyTask
        @Override protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // пример доступа к UI, записи в журнал
            String temp = "Загружено файлов: " + (values[0]+1) + " из " + totalFiles;
            Log.d(SIGN, temp);
            tvInfo.setText(temp);
        } // onProgressUpdate

        // вывод финишного сообщения
        @Override protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // пример доступа к UI
            tvInfo.setText(getString(R.string.load_complete));
            Log.d(SIGN, getString(R.string.load_complete));
            btnStart.setEnabled(true);
        } // onPostExecute

        // имитация загрузки файла - просто задержка...
        private void downloadFile(String url) {
            Log.d(SIGN, "Загружается файл: " + url);
            Utils.sleepMs(3_000);
        } // downloadFile
    } // class MyTask
}