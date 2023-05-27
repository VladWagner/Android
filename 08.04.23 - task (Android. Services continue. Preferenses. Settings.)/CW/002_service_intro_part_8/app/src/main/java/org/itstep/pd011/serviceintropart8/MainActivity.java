package org.itstep.pd011.serviceintropart8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

// прямой синхронный вызов методов сервиса
public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";

    boolean bound = false;
    ServiceConnection sConn;
    Intent intent;
    MyService myService;

    TextView tvInterval;
    long interval = 1_000;  // начальное значение, установлено в сервисе


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInterval = findViewById(R.id.tvInterval);
        tvInterval.setText(String.format("Интервал = %d", interval));

        // интент для работы с сервисом
        intent = new Intent(this, MyService.class);

        // В методе onServiceConnected мы берем binder, преобразуем его к MyService.MyBinder,
        // вызываем метод getService и получаем наш сервис MyService.
        // Теперь мы можем выполнять методы сервиса.
        sConn = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected");
                myService = ((MyService.MyBinder) binder).getService();
                bound = true;
            } // onServiceConnected

            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
                bound = false;
            } // onServiceDisconnected
        };
    } // onCreate


    @Override
    protected void onStart() {
        super.onStart();
        bindService(intent, sConn, 0);
    } // onStart

    @Override
    protected void onStop() {
        super.onStop();
        if (!bound) return;

        unbindService(sConn);
        bound = false;
    } // onStop

    public void onClickStart(View v) {
        startService(intent);
    } // onClickStart

    public void onClickUp(View v) {
        if (!bound) return;

        // прямое, синхронное обращение к сервису!
        interval = myService.upInterval(500);
        tvInterval.setText("interval = " + interval + " мс");
    } // onClickUp

    public void onClickDown(View v) {
        if (!bound) return;

        // прямое, синхронное обращение к сервису!
        interval = myService.downInterval(500);
        tvInterval.setText("interval = " + interval + " мс");
    } // onClickDown

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bound) unbindService(sConn);
        stopService(intent);
    } // onDestroy
} // class MainActivity
