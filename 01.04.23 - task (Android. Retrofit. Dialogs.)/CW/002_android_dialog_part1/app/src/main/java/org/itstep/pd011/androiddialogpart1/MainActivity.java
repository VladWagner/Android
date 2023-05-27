package org.itstep.pd011.androiddialogpart1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

// Демонстрация системных диалогов для ввода даты и времени
public class MainActivity extends AppCompatActivity {

    // поле отображения текста
    TextView txvCurrentDateTime;

    // объект для работы с датой и временем
    Calendar dateAndTime = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);

        // ссылка на поле отображения даты и времени
        txvCurrentDateTime = findViewById(R.id.currentDateTime);
        setInitialDateTime();
    } // onCreate


    // отображаем диалоговое окно для выбора даты - DatePickerDialog
    public void setDateToTextView(View view) {
        new DatePickerDialog(
            MainActivity.this,                  // контекст создания окна
            dateSetListener,                    // слушатель события - дата изменена
            dateAndTime.get(Calendar.YEAR),     // задать год, месяц, и день из объекта-календаря
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH))
            .show();  // показать диалог
    } // setDateToTextView

    // отображаем диалоговое окно для выбора времени - TimePickerDialog
    // в 24-х часовом формате
    public void setTime24(View v) {
        new TimePickerDialog(
            MainActivity.this,                      // контекст создания диалогового окна
            timeSetListener,                        // слушатель события изменение времени в диалоге
            dateAndTime.get(Calendar.HOUR_OF_DAY),  // час
            dateAndTime.get(Calendar.MINUTE),       // минута
            true)                                   // 24-х часовый формат времени
            .show();
    } // setTime24

    // отображаем диалоговое окно для выбора времени - TimePickerDialog
    // в 12-и часовом формате
    public void setTime12(View v) {
        new TimePickerDialog(
            MainActivity.this,                      // контекст создания диалогового окна
            timeSetListener,                        // слушатель события изменение времени в диалоге
            dateAndTime.get(Calendar.HOUR_OF_DAY),  // час
            dateAndTime.get(Calendar.MINUTE),       // минута
            false)                      // 12-и часовый формат времени
            .show();
    } // setTime12

    // установка обработчика изменения/выбора времени
    TimePickerDialog.OnTimeSetListener timeSetListener = (TimePicker view, int hourOfDay, int minute)
            -> {
        // подготовить и вывести новое время в строке отображения
        dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        dateAndTime.set(Calendar.MINUTE, minute);

        // записать время в TextView
        setInitialDateTime();
    };

    // установка обработчика изменения/выбора даты
    private DatePickerDialog.OnDateSetListener dateSetListener =
        (DatePicker view, int year, int monthOfYear, int dayOfMonth) -> {
            // подготовить и вывести новую дату в строке отображения
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // записать дату в TextView
            setInitialDateTime();
        };

    // установка начальных даты и времени для диалогов
    private void setInitialDateTime() {

        txvCurrentDateTime.setText(DateUtils.formatDateTime(
                this,
                dateAndTime.getTimeInMillis(),  // время в миллисекундах
                // выводим это время в привычном представлении - дата и время
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME));
    } // setInitialDateTime


    // завершение активности
    public void finishActivity(View view) { finish(); } // finishActivity
} // class MainActivity