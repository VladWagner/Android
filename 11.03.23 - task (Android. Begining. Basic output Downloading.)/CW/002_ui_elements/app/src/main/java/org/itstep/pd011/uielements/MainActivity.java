package org.itstep.pd011.uielements;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txvResult;
    private Button btnCalc, btnExit;
    private EditText edtNumber1, edtNumber2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvResult = findViewById(R.id.txvResult);
        btnCalc = findViewById(R.id.btnCalc);
        btnExit = findViewById(R.id.btnExit);
        edtNumber1 = findViewById(R.id.edtNumber1);
        edtNumber2 = findViewById(R.id.edtNumber2);

        // связь с обработчиком собыия клика по кнопке
        btnExit.setOnClickListener(this::exitClick);

        // связь с обработчиком события в стиле "быстро, но грязно"
        btnCalc.setOnClickListener((v) -> {
            // получить данные из полей ввода
            double number1 = Double.parseDouble(edtNumber1.getText().toString());
            double number2 = Double.parseDouble(edtNumber2.getText().toString());
            double reslult = number1 + number2;

            // сформировать строку результата
            String strResult = String.format(Locale.UK, "Сумма %.3f и %.3f равна %.3f",
                    number1, number2, reslult);

            // вывод в элемент разметки
            txvResult.setText(strResult);

            // подготовить и вывести всплывающее сообщение22
//            Toast
//                .makeText(this, strResult, Toast.LENGTH_LONG)
//                .show();

            Toast toast = Toast.makeText(this, strResult, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        });
    }


    // обработчик клика по кнопке "Привет", назначеннный в разметке
    public void greating(View view) {
        txvResult.setText("Привет, \nэто наш код");
    } // greating


    // обработчик клика по кнопке "Выход"
    public void exitClick(View view) {
        // завершает активность, но не приложение
        finish();
    } // exitClick
}