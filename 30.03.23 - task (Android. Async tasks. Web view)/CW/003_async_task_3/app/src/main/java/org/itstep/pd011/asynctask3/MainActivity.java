package org.itstep.pd011.asynctask3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// демонстрация get()     -- получение результата из задачи
// демонстрация cancel()  -- завершение задачи
//
// Возможные состояния задачи
//   PENDING  - задача не запущена
//   RUNNING  - задача работает (в onPostExecute - все еще это состояние)
//   FINISHED - задача завершена (возможно, прервана)
public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "AT_03";

    MyTask mt;        // экземпляр AsyncTask
    private TextView tvInfo, tvStatus;
    private Button btnStart, btnGet, btnCancel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo =    findViewById(R.id.tvInfo);
        tvStatus =  findViewById(R.id.tvStatus);
        btnStart =  findViewById(R.id.btnStart);
        btnGet =    findViewById(R.id.btnGet);
        btnCancel = findViewById(R.id.btnCancel);

        btnGet.setEnabled(false);
        btnCancel.setEnabled(false);
    } // onCreate

    // нажатия на кнопки
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:  // запуск задачи
                mt = new MyTask();
                // получение статуса задачи
                Log.d(LOG_TAG, "Состояние задачи: " + mt.getStatus());

                mt.execute();  // выполнить задачу
                Log.d(LOG_TAG, "Состояние задачи: " + mt.getStatus());

                btnGet.setEnabled(true);
                btnCancel.setEnabled(true);
                break;

            case R.id.btnGet:  // получение результата
                getAndShowResult();
                break;

            case R.id.btnCancel: // прерывание AsyncTask
                cancelTask();
                break;
        } // switch
    } // onclick

    // вывод результата работы задачи
    private void getAndShowResult() {
        // если задачи нет, то и показывать нечего
        if (mt == null) return;

        String temp;
        int result = -1;
        try {
            Log.d(LOG_TAG, "Попытка получения результата");

            // если задача не завершена, get() переходит в ожидание
            // завершения этой задачи => "замерзает" интерфейс (фриз)
            // result = mt.get();    // получение результата из задачи

            // получение результата с таймаутом - т.е. если через 100 мс
            // нет ответа - exception TimeoutException, выход из метода showResult()
            result = mt.get(100, TimeUnit.MILLISECONDS);

            temp = "Результат задачи, возвращенный get(): " + result +
                    ". Состояние задачи: " + mt.getStatus();
            Log.d(LOG_TAG, temp);
            tvInfo.setText(temp);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {   // для get() с таймаутом
            e.printStackTrace();
            temp = "Задача все еще работает, состояние задачи: " + mt.getStatus();
            Log.d(LOG_TAG, temp);
            tvInfo.setText(temp);
        } // try-catch
    } // showResult

    // Прерывание задачи по кнопке "Прервать"
    private void cancelTask() {
        // если задачи нет, то и прерывать нечего
        if (mt == null) return;

        // ставит "метку" на завершение задачи
        // получает true - система может завершить поток
        // получает false - поток завершается в методе doInBackground()
        // возвращает true если метка установлена
        // возвращает false если задача уже отменена
        boolean result = mt.cancel(false);
        Log.d(LOG_TAG, "Результат cancel: " + result);

        btnGet.setEnabled(false);
        btnCancel.setEnabled(false);
    } // cancelTask


    // обработчик клика по кнопке "Состояние задачи"
    public void onClickGetStatus(View view) {
        String str = mt == null?"Задача еще не создана":"Состояние задачи: " + mt.getStatus();
        Log.d(LOG_TAG, str);

        tvStatus.setText(str);
    } // onClickGetStatus

    // Задача
    // Integer - тип результата, т.е. тип для onPostExecute()
    class MyTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            String temp = "onPreExecute -> Начало работы. Состояние задачи: " + mt.getStatus();
            tvInfo.setText(temp);
            Log.d(LOG_TAG, temp);
        } // onPreExecute

        /*
         * В doInBackground мы должны периодически вызывать метод isCancelled.
         * Как только выполнен метод cancel для AsyncTask, isCancelled будет
         * возвращать true. А это значит, что мы должны завершить метод doInBackground.
         *
         * Т.е. метод cancel – это мы ставим метку, что задачу надо отменить. Метод isCancelled –
         * мы же сами эту метку читаем и предпринимаем действия, для завершения работы задачи.
         * Метод cancel возвращает boolean. Мы получим false, если задача уже завершена или
         * отменена.
         *
         * */
        @Override
        // если задача прервана по cancel() - возвращаем null
        // если задача не прерывана по cancel() - возвращаем RESULT_CODE
        protected Integer doInBackground(Void... params) {
            // имитация результата задачи
            final int RESULT_CODE = (new Random()).nextInt(999999);

            for (int i = 0; i < 50; i++) {
                // программист обязан периодически проверять, не пришел ли сигнал
                // завершения задачи
                Utils.sleepMs(100);
                boolean temp = isCancelled(); // привильнее сохранить состояние, т.к. не известно когда
                // текущий поток прерывается планировщиком => повторные вызовы isCancelled()
                // могут давать разные результаты
                Log.d(LOG_TAG, String.format("doInBackground -> вызвал isCancelled(). Результат isCancelled(): %b", temp));

                // выход с признаком прерывания задачи
                // !!! это очень важно для правильной реакции на вызов cancel()
                if (temp) return null;
            } // for

            // обычный выход, получен результат работы
            return RESULT_CODE;
        } // doInBackground

        // Тип параметра - тип третьего параметра класса AsyncTask
        @Override protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            String str = "onPostExecute -> Завершено. Результат: " + result + ". Состояние задачи: " + mt.getStatus();
            tvInfo.setText(str);
            Log.d(LOG_TAG, str);

            btnGet.setEnabled(true);
            btnCancel.setEnabled(false);
        } // onPostExecute

        // Метод, вызываемый системой при прерывании задачи при помощи cancel
        // !!! onPostExecute() не вызывается после mt.cancel(),
        // !!! но onCancelled() в такой ситуации вызвается
        @Override protected void onCancelled() {
            super.onCancelled();

            String str = "onCancelled -> Прервано пользователем, состояние задачи: " + mt.getStatus();
            tvInfo.setText(str);
            Log.d(LOG_TAG, str);

            btnGet.setEnabled(false);
            btnCancel.setEnabled(false);
        } // onCancelled
    } // class MyTask
} // class MainActivity