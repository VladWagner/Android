package com.step.wagner.fragments.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.step.wagner.R;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.Listener;

import java.util.Calendar;

public class Query3Dialog extends DialogFragment {

    //Callback для клика по кнопке
    private Listener<Calendar> listener;

    /*private EditText minDateField;
    private EditText maxDateField;*/
    private Button minDateBtn;
    private Button maxDateBtn;

    //Calendar для получения даты
    private Calendar calendarMinDate;
    private Calendar calendarMaxDate;

    public Query3Dialog() {
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        //Сформировать предсталвение с разметкой
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.query_3_dialog, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Параметры запроса 3")
                .setView(linearLayout)
                .setPositiveButton("Задать", okClickListener)
                .setNegativeButton("Отмена", null);

        //Проверка, были заданы занчения ранее и если да, то взять старые значения
        if (Parameters.CALENDAR_MIN_DATE == null) {
            calendarMinDate = Calendar.getInstance();
            calendarMinDate.setTime(Utils.getRandomDate());

            Parameters.CALENDAR_MIN_DATE = calendarMinDate;

        } else calendarMinDate = Parameters.CALENDAR_MIN_DATE;


        if (Parameters.CALENDAR_MAX_DATE == null) {
            calendarMaxDate = Calendar.getInstance();
            calendarMaxDate.setTime(Utils.getRandomDate());

            Parameters.CALENDAR_MAX_DATE = calendarMaxDate;

        } else calendarMaxDate = Parameters.CALENDAR_MAX_DATE;

        //Если значения сегенерированы или заданы некоррректно, тогда поменять их местами

        if (calendarMinDate.getTimeInMillis() > calendarMaxDate.getTimeInMillis()) {
            long minMillis = calendarMinDate.getTimeInMillis();
            calendarMinDate.setTimeInMillis(calendarMaxDate.getTimeInMillis());
            calendarMaxDate.setTimeInMillis(minMillis);
        }

        minDateBtn = linearLayout.findViewById(R.id.query_start_date);
        maxDateBtn = linearLayout.findViewById(R.id.query_end_date);

        setFields();

        //Задать обработчик клика по edit text с датами
        minDateBtn.setOnClickListener(this::onMinDateClickListener);
        maxDateBtn.setOnClickListener(this::onMaxDateClickListener);

        //Задать обработчики кликов по заголовкам полей ввода
        linearLayout.findViewById(R.id.minDateLabel).setOnClickListener(this::onMinDateClickListener);
        linearLayout.findViewById(R.id.maxDateLabel).setOnClickListener(this::onMaxDateClickListener);

        return builder.create();
    }

    private DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {


            //Вызвать обработчик, заданный извне
            listener.onOkClickListener(calendarMinDate, calendarMaxDate);

        }
    };

    private void setFields() {
        minDateBtn.setText(Utils.dateFormat.format(calendarMinDate.getTime()));
        maxDateBtn.setText(Utils.dateFormat.format(calendarMaxDate.getTime()));
    }

    //Задать обработчика извне
    public void setListener(Listener<Calendar> listener) {
        if (listener == null) return;

        this.listener = listener;
    }

    //Открыть диалог с минимальной датой
    private void onMinDateClickListener(View view) {

        int year = calendarMinDate.get(Calendar.YEAR);
        int month = calendarMinDate.get(Calendar.MONTH);
        int day = calendarMinDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog = new DatePickerDialog(
                getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, minDateChangedListener
                , year, month, day
        );

        //Задать фон диалога
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    //Открыть диалог с максимальной датой
    private void onMaxDateClickListener(View view) {

        int year = calendarMaxDate.get(Calendar.YEAR);
        int month = calendarMaxDate.get(Calendar.MONTH);
        int day = calendarMaxDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog = new DatePickerDialog(
                getContext(), android.R.style.Theme_Holo_Dialog_MinWidth, maxDateChangedListener
                , year, month, day
        );

        //Задать фон диалога
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    //Обработчик выбора минимальной даты
    DatePickerDialog.OnDateSetListener minDateChangedListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendarMinDate.set(year, month, day);
            Parameters.CALENDAR_MIN_DATE = calendarMinDate;

            //Изменить поле ввода и переданную сущность
            minDateBtn.setText(Utils.dateFormat.format(calendarMinDate.getTime()));
        }//onDateSet
    };

    //Обработчик выбора максимальной даты
    DatePickerDialog.OnDateSetListener maxDateChangedListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendarMaxDate.set(year, month, day);
            Parameters.CALENDAR_MAX_DATE = calendarMaxDate;

            //Изменить поле ввода и переданную сущность
            maxDateBtn.setText(Utils.dateFormat.format(calendarMaxDate.getTime()));
        }//onDateSet
    };
}
