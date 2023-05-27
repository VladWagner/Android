package org.itstep.pd011.serviceintropart1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /*
     * Сервис – это некая задача, которая работает в фоне и не использует UI.
     * Запускать и останавливать сервис можно из приложений и других сервисов.
     * Также можно подключиться к уже работающему сервису и взаимодействовать с ним.
     *
     * В качестве примера можно рассмотреть алгоритм почтовой программы.
     * Она состоит из приложения и сервиса.
     * Сервис работает в фоне и периодически проверяет наличие новой почты,
     * скачивает ее и выводит уведомления.
     * А когда вы запускаете приложение, оно отображает вам эти загруженные сервисом письма.
     * Также приложение может подключиться к сервису и поменять в нем,
     * например, период проверки почты или совсем закрыть сервис, если постоянная проверка
     * почты больше не нужна.
     *
     * Т.е. сервис нужен, чтобы ваша задача продолжала работать, даже когда приложение
     * закрыто. В других примерах обсудим, какие способы взаимодействия существуют
     * между приложением и сервисом.
     *
     * В этом примере создадим простейший сервис, который будет выводить что-нибудь в лог.
     * Приложение будет запускать и останавливать сервис.
     *
     * */

    private TextView txvInfo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvInfo = findViewById(R.id.txvInfo);
        txvInfo.setText("Старт приложения: ок");
    } // onCreate

    // запуск сервиса (службы) - в том же потоке, что и MainActivity
    public void onClickStart(View view) {
        Log.d("Service01", "Запуск сервиса");

        // параметр запуска - интент
        Intent intent = new Intent(this, MyService.class);
        startService(intent);

        // краткий вариант
        // startService(new Intent(this, MyService.class));

        txvInfo.setText("Сервис запущен");
    } // onClickStart

    // завершение службы
    public void onClickStop(View view) {
        Log.d("Service01", "Завершение сервиса");

        Intent intent = new Intent(this, MyService.class);
        stopService(intent);

        // краткий вариант
        // stopService(new Intent(this, MyService.class));

        txvInfo.setText("Сервис завершен");
    } // onClickStop
} // class MainActivity