package org.itstep.pd011.serviceintropart5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // получаем из сервиса результат с помощью BroadcastReceiver - аналогично
    // вызовам Activity с использованием Action и IntentFilter

    // Схема такая:
    // ☼ в Activity создаем BroadcastReceiver, а также создаем IntentFilter,
    //    настроенный на определенный Action, и регистрируем (включаем) эту пару.
    //    Теперь BroadcastReceiver будет получать Intent-ы подходящие под условия IntentFilter
    // ☼ в сервисе, когда нам понадобится передать данные в Activity, мы создаем Intent
    //   (с Action из предыдущего пункта), кладем в него данные, которые хотим передать,
    //   и посылаем его на поиски BroadcastReceiver
    // ☼ BroadcastReceiver в Activity ловит этот Intent и извлекает из него данные
    //
    // Т.е. аналогично вызовам Activity с использованием Action и IntentFilter.
    // Если Action в Intent (отправленном из сервиса) и в IntentFilter (у BroadcastReceiver
    // в Activity) совпадут, то BroadcastReceiver получит этот Intent и сможет извлечь данные
    // для Activity.
    //
    // Пример аналогичен примеру Service Intro part 4. Приложение, будет отправлять в сервис
    // на выполнение три задачи. Сервис сообщает, когда он начал выполнять каждую задачу,
    // когда закончил и с каким результатом.
    // Все эти сообщения отображаются в интерфейсе Activity.

    final String LOG_TAG = "service05";

    final int TASK1_CODE = 1;
    final int TASK2_CODE = 2;
    final int TASK3_CODE = 3;

    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    public final static String PARAM_TIME = "time";
    public final static String PARAM_TASK = "task";
    public final static String PARAM_RESULT = "result";
    public final static String PARAM_STATUS = "status";

    TextView tvTask1, tvTask2, tvTask3;

    // Action - уникальное имя для фильтрации интента
    public final static String BROADCAST_ACTION = "org.itstep.pd011.serviceintropart5";

    // Объект для получения данных из сервиса
    BroadcastReceiver  br = new BroadcastReceiver() {
        // действия при получении сообщений
        public void onReceive(Context context, Intent intent) {
            int task   = intent.getIntExtra(PARAM_TASK, 0);
            int status = intent.getIntExtra(PARAM_STATUS, 0);
            Log.d(LOG_TAG, String.format("onReceive: task = %d, status = %d", task, status));

            switch (status) {
                // Ловим сообщения о старте задач
                case STATUS_START:
                    switch (task) {
                        case TASK1_CODE:
                            tvTask1.setText("Task1 старт");
                            break;
                        case TASK2_CODE:
                            tvTask2.setText("Task2 старт");
                            break;
                        case TASK3_CODE:
                            tvTask3.setText("Task3 старт");
                            break;
                    } // switch
                    break;

                // Ловим сообщения об окончании задач
                case STATUS_FINISH:
                    // чтение параметра, когда он присутствует в intent
                    int result = intent.getIntExtra(PARAM_RESULT, 0);
                    switch (task) {
                        case TASK1_CODE:
                            tvTask1.setText("Task1 финиш, result = " + result);
                            break;
                        case TASK2_CODE:
                            tvTask2.setText("Task2 финиш, result = " + result);
                            break;
                        case TASK3_CODE:
                            tvTask3.setText("Task3 финиш, result = " + result);
                            break;
                    } // switch
                    break;
            } // switch
        } // onReceive
    }; // BroadcastReceiver

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTask1 = findViewById(R.id.tvTask1);
        tvTask2 = findViewById(R.id.tvTask2);
        tvTask3 = findViewById(R.id.tvTask3);

        tvTask1.setText(R.string.str_task1);
        tvTask2.setText(R.string.str_task2);
        tvTask3.setText(R.string.str_task3);

        // создаем фильтр для BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);

        // регистрируем (включаем) BroadcastReceiver
        // регистрация не обязательно в onCreate - можно где удобно
        registerReceiver(br, intentFilter);
    } // onCreate

    // убрать регистрацию BroadcastReceiver
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // дерегистрируем (выключаем) BroadcastReceiver - не обязательно
        // в onDestroy - можно где удобно
        unregisterReceiver(br);
    } // onDestroy


    // обработчик клика по кнопке запуска сервиса
    public void onClickStart(View v) {
        Intent intent;

        // чистим вывод, оставшийся от предыдущего цикла работы
        onClickClear(v);

        // Создаем Intent для вызова сервиса,
        // кладем туда параметр времени и код задачи
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 7)
                .putExtra(PARAM_TASK, TASK1_CODE);
        // стартуем сервис
        startService(intent);

        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 4)
                .putExtra(PARAM_TASK, TASK2_CODE);
        startService(intent);

        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 6)
                .putExtra(PARAM_TASK, TASK3_CODE);
        startService(intent);
    } // onClickStart


    // вывод начального текста в текстовые поля сервисов
    public void onClickClear(View view) {
        tvTask1.setText(R.string.str_task1);
        tvTask2.setText(R.string.str_task2);
        tvTask3.setText(R.string.str_task3);
    } // onClickClear

    // создать меню прилолжения и обработать команду меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case R.id.mnuStart:
                onClickStart(item.getActionView());
                break;
            case R.id.mnuFinish:
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        } // switch

        return result;
    } // onOptionsItemSelected
    // ------------------------------------------------------------


} // class MainActivity