package org.itstep.pd011.androiddialogpart2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);
    } // onCreate


    // Выводим простое диалоговое окно
    public void showDialog1(View v) {
        // объект - диалог
        CustomDialogFragment1 dialog = new CustomDialogFragment1();

        // отображение диалога
        // второй параметр - произвольная строка, тег, по которому
        // можно идентифицировать диалог
        dialog.show(getFragmentManager(), "custom1");
    } // showDialog1


    // Выводим настроенное диалоговое окно
    public void showDialog2(View view) {
        CustomDialogFragment2 dialog = new CustomDialogFragment2();

        dialog.show(getFragmentManager(), "custom2");
    } // showDialog2


    // Выводим настроенное диалоговое окно с разметкой
    public void showDialog3(View view) {
        CustomDialogFragment3 dialog = new CustomDialogFragment3();
        dialog.show(getFragmentManager(), "custom3");
    } // showDialog3


    // завершение активности
    public void finishClick(View view) {
        finish();
    } // finishClick


    // отменяет выход из приложения по кнопке "Назад"
    @Override
    public void onBackPressed() {
        // для запрета выхода достаточно отменить вызов метода-предка
        // super.onBackPressed();
    } // onBackPressed
}