package org.itstep.pd011.serviceintropart4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Получение результатов в активности из сервиса
public class MainActivity extends Activity {

    final String LOG_TAG = "service_04";

    // назначенные самостоятельно коды запросов (ид задач)
    final int TASK1_CODE = 1;
    final int TASK2_CODE = 2;
    final int TASK3_CODE = 3;

    // назначенные самостоятельно статусы для запросов
    public final static int STATUS_START = 100;
    public final static int STATUS_FINISH = 200;

    // имена параметров для передачи в сервисы
    public final static String PARAM_TIME    = "time";
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT  = "result";

    TextView tvTask1, tvTask2, tvTask3;
    Button btnStart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTask1 = findViewById(R.id.tvTask1);
        tvTask2 = findViewById(R.id.tvTask2);
        tvTask3 = findViewById(R.id.tvTask3);

        tvTask1.setText("Task1");
        tvTask2.setText("Task2");
        tvTask3.setText("Task3");

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v -> onClickStart(v));
    } // onCreate

    // кнопка "Старт сервис"
    public void onClickStart(View v) {
        PendingIntent pi;
        Intent intent;

        tvTask1.setText("Task1");
        tvTask2.setText("Task2");
        tvTask3.setText("Task3");

        // Создаем PendingIntent для Task1
        // параметры - ид, интент (до версии 4 можно было передавать null), флаги
        pi = createPendingResult(TASK1_CODE, new Intent(), 0);

        // Создаем Intent для вызова сервиса, кладем туда параметр времени
        // и созданный PendingIntent
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 7)
                .putExtra(PARAM_PINTENT, pi);
        // стартуем сервис
        startService(intent);

        pi = createPendingResult(TASK2_CODE, new Intent(), 0);
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 4)
                .putExtra(PARAM_PINTENT, pi);
        startService(intent);

        pi = createPendingResult(TASK3_CODE, new Intent(), 0);
        intent = new Intent(this, MyService.class)
                .putExtra(PARAM_TIME, 6)
                .putExtra(PARAM_PINTENT, pi);
        startService(intent);
    } // onClickStart

    @Override // получает результат из сервисов
    // параметр data - это интент, полученный из сервиса
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, String.format("requestCode = %d, resultCode = %d", requestCode, resultCode));

        switch(resultCode) {
            // Ловим сообщения о старте задач
            case STATUS_START:
                switch (requestCode) {
                    case TASK1_CODE:
                        tvTask1.setText("Task1 стартовала");
                        break;
                    case TASK2_CODE:
                        tvTask2.setText("Task2 стартовала");
                        break;
                    case TASK3_CODE:
                        tvTask3.setText("Task3 стартовала");
                        break;
                } // switch
                break;

            // Ловим сообщения об окончании задач
            case STATUS_FINISH:
                // т.к. это финиш сервиса, то получаем данные из этого сервиса,
                // извлекая данные из полученного интента data
                int result = data.getIntExtra(PARAM_RESULT, 0);
                Log.d(LOG_TAG, String.format("result = %d", result));

                switch (requestCode) {
                    case TASK1_CODE:
                        tvTask1.setText("Task1 финишировала, получено из задачи " + result);
                        break;
                    case TASK2_CODE:
                        tvTask2.setText("Task2 финишировала, получено из задачи " + result);
                        break;
                    case TASK3_CODE:
                        tvTask3.setText("Task3 финишировала, получено из задачи " + result);
                        break;
                } // switch
                break;
        } // switch
    } // onActivityResult
} // class MainActivity