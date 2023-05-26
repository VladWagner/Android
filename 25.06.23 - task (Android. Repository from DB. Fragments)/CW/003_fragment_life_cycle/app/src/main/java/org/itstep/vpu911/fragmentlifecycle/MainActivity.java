package org.itstep.vpu911.fragmentlifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// Просто контейнер для размещения фран=гмента
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // завершение активности
        findViewById(R.id.btn_exit).setOnClickListener(v -> finish());
    } // onCreate
} // class MainActivity