package org.itstep.pd011.returnfromactivities.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.itstep.pd011.returnfromactivities.MainActivity;
import org.itstep.pd011.returnfromactivities.R;

// активность возвращает код ошибки
public class BeveridgeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beveridge);

        findViewById(R.id.btnBeveridgeActivityBack).setOnClickListener(v -> back());
    } // onCreate

    // обработка клика по кнопке выхода из активности
    private void back() {
        Intent intent = new Intent();
        // для примера вернем код ошибки
        setResult(MainActivity.RESULT_ERR, intent);
        finish();
    } // back
} // class BeveridgeActivity