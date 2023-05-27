package org.itstep.pd011.serviceintropart3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String SERVICE_DATA = "name";
    private TextView txvInfo;

    /*
     * Работа с параметрами метода onStartCommand()
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvInfo = findViewById(R.id.txvInfo);
        txvInfo.setText("Старт приложения: ок");
    } // onCreate


    public void onClickStart(View v) {
        // сюжетно не важно - случайно выбирвем строку для сервиса
        final String[] params = {
                "межпроцессорный", "корпускулярный", "пикулярный",
                "ретроградный", "аккреционный"};
        Random random = new Random();
        int i = random.nextInt(params.length);

        txvInfo.setText(String.format("Сервис запущен с параметром '%s'", params[i]));
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra(SERVICE_DATA, params[i]);

        // для запуска сервиса не по имени класса, а по имени интента
        // (по фильтру интентов) - аналогично запуску активности
        // startService(new Intent("org.itstep.pd011.serviceintropart3.MyService").putExtra(SERVICE_DATA, params[i]));
        // или
        // Intent intent = new Intent("org.itstep.pd011.serviceintropart3.MyService");
        // intent.putExtra(SERVICE_DATA, params[i]);
        startService(intent);
    } // onClickStart
}