package org.itstep.pd011.helloapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // ссылка на объект интерфейса
    private TextView txvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // найти элемент разметки связать его со ссылкой
        txvHello = findViewById(R.id.txvHello);

        // вывести текст в элемент разметки
        // txvHello.setText("Это наш текст");

        // вывести текст в элемент разметки из ресурса
        txvHello.setText(R.string.msg_soft);
    }
}