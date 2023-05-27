package org.itstep.pd011.serviceintropart7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

// Определение моментов подключеия к сервису, отключения от сервиса при помощи
// объекта класса ServiceConnection
public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "service_07";

    private boolean bound = false; // флаг подключения к сервису или отключения от сервиса
    private ServiceConnection sConn;
    private Intent intent;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // интент для запуска сервиса, реализован полем класса для эффективности
        //                  из какой активности запускаем, какой сервис запускаем
        intent = new Intent(this, MyService.class);

        /*
         * Объект ServiceConnection позволит нам определить, когда мы подключились к сервису и
         * когда связь с сервисом потеряна (если сервис был убит системой при нехватке памяти).
         * При подключении к сервису сработает метод onServiceConnected. На вход он получает
         * имя компонента-сервиса и объект Binder для взаимодействия с сервисом.
         * В этом приложении мы Binder пока не пользуемся.
         * При потере связи сработает метод onServiceDisconnected.
         * Переменную bound используем для того, чтобы знать – подключены мы в данный момент к
         * сервису (bound установлен в true) или нет (bound установлен в false).
         *
         * */
        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected");
                bound = true;
            } // onServiceConnected

            // метод срабатывает при отключении сервиса
            // при явном отключении вызовом unbindService() не сработает
            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
                bound = false;
            } // onServiceDisconnected
        };
    } // onCreate

    // клики по кнопкам запуска и завершения сервиса
    public void onClickStart(View v) {
        Log.d(LOG_TAG, "MainActivity onClickStart");
        startService(intent);
    } // onClickStart

    public void onClickStop(View v)  {
        Log.d(LOG_TAG, "MainActivity onClickStop");
        Toast.makeText(this, "MainActivity onClickStop", Toast.LENGTH_SHORT).show();
        stopService(intent);
    } // onClickStop

    // Соединяемся с сервисом, используя метод bindService. На вход передаем Intent,
    // ServiceConnection и флаг BIND_AUTO_CREATE (если сервис, к которому мы пытаемся
    // подключиться, не работает, то он будет запущен).
    public void onClickBind(View v) {
        Log.d(LOG_TAG, "MainActivity onClickBind");

        // intent - интент для запуска сервиса
        // sConn  - ServiceConnection для определения подключения/отключения к сервису/от сервиса
        // BIND_AUTO_CREATE - запустить сервис, если он еще не запущен
        bindService(intent, sConn, BIND_AUTO_CREATE);
        // bindService(intent, sConn, 0);
    } // onClickBind

    // Отсоединяемся методом unbindService, на вход передавая ему ServiceConnection.
    // И в bound пишем false, т.к. мы сами разорвали соединение.
    public void onClickUnBind(View v) {
        if (!bound) return;
        Log.d(LOG_TAG, "MainActivity onClickUnBind");

        unbindService(sConn);
        bound = false;
    } // onClickUnBind

    // явно отсоединяемся при завершении активности
    protected void onDestroy() {
        super.onDestroy();

        onClickUnBind(null);
        Log.d(LOG_TAG, "MainActivity onDestroy, service unbind");
    } // onDestroy
} // class MainActivity