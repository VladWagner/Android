package org.itstep.pd011.serviceintropart6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

// формирование уведомлений от сервиса
public class MainActivity extends AppCompatActivity {

    // Уведомления, Notifications - все важное в MyService
    // тут просто запуск и завершение сервиса
    // в завершении сервиса пример удаления уведомлений
    public final static String PARAM_FILE_NAME = "filename";
    TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);

        // TODO: передать из сервиса имя файла
        // этот блок кода срабатывает при клике на сообщение,
        // формируемое уведомлением сервиса - извлекаем
        // intent, через который можно получать данные от сервиса
        Intent intent = getIntent();
        String fileName = intent.getStringExtra(PARAM_FILE_NAME);
        if (!TextUtils.isEmpty(fileName))
            tv.setText("Имя принятого файла: " + fileName);
    } // onCreate

    // старт сервиса
    public void onClickStart(View v) {
        startService(new Intent(this, MyService.class));
        tv.setText(R.string.str_wait);
    } // onClickStart


    // стоп сервиса
    public void onClickStop(View v) {
        stopService(new Intent(this, MyService.class));
        tv.setText(R.string.str_result);

        // получить ссылку на менедженра уведомлений
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // удалить уведомление по идентификатору, например 2
        // notificationManager.cancel(2);

        // удалить все уведомления
        notificationManager.cancelAll();
    } // onClickStop
}