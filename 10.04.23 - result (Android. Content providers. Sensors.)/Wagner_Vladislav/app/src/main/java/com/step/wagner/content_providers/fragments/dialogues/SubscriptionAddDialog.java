package com.step.wagner.content_providers.fragments.dialogues;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.step.wagner.content_providers.R;
import com.step.wagner.content_providers.async_tasks.CommonTask;
import com.step.wagner.content_providers.infrastructure.Parameters;
import com.step.wagner.content_providers.infrastructure.StringWithId;
import com.step.wagner.content_providers.infrastructure.Utils;
import com.step.wagner.content_providers.interfaces.SubscriptionListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class SubscriptionAddDialog extends DialogFragment {

    //Callback для клика по кнопке
    private SubscriptionListener listener;
    private Spinner publicationsSpinner;
    private EditText extDuration;

    private Button btnDate;

    private Switch swtRecoverInput;

    //Calendar для получения даты
    private Calendar calendar;

    //Отсылаемая дата
    private Date sendingDate;

    //Сохранение заданных данных
    private SharedPreferences sharedPreferences;

    private Activity activity;

    public SubscriptionAddDialog(){
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        activity = getActivity();

        //Сформировать предсталвение с разметкой
        View view = inflater.inflate(R.layout.subscription_dialog,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Добавление подписки")
                .setView(view)
                .setPositiveButton("Сохранить",okClickListener)
                .setNegativeButton("Отмена",null);


        calendar = Calendar.getInstance();
        //calendar.setTime(new Date());

        //Получение ссылок на элементы разметки
        publicationsSpinner = view.findViewById(R.id.publicationsSpinner);
        extDuration = view.findViewById(R.id.etxDuration);
        btnDate = view.findViewById(R.id.appointmentDateDialog);
        swtRecoverInput = view.findViewById(R.id.swtSaveInput);
        swtRecoverInput.setVisibility(View.VISIBLE);


        sharedPreferences = activity.getSharedPreferences(Parameters.DIALOG_SETTINGS_FILE, Context.MODE_PRIVATE);
        setFields(view);


        //Задать обработчик клика по edit text
        btnDate.setOnClickListener(this::onDateClickListener);


        return builder.create();
    }

    private DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            //Получить выбранные в spinners элементы
            int chosenPublicationId = ((StringWithId) publicationsSpinner.getSelectedItem()).getId();
            Integer duration = Utils.tryParseInt(extDuration.getText().toString());

            //Сохранить значения в локальный файл настроек
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (sendingDate != null)
                editor.putString("date_start",Utils.dateFormat.format(sendingDate));

            editor.putInt("publication_id",chosenPublicationId);

            String durationStr = String.valueOf(duration);

            editor.putString("subscription_duration",!durationStr.equals("null") ? durationStr : "");
            editor.putBoolean("recover_input",swtRecoverInput.isChecked());

            editor.apply();

            //Вызвать обработчик, заданный извне
            listener.onOkClickListener(0,sendingDate,chosenPublicationId, duration != null ? duration : 0);

        }
    };

    //Обработчик выхода из диалога
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    //Задать обработчика извне
    public void setListener(SubscriptionListener listener){
        if (listener == null) return;

        this.listener = listener;
    }

    //Заполнить элементы ввода
    private void setFields(View view){

        //Задать дату
        //btnDate.setText(Utils.dateFormat.format(calendar.getTime()));

        CommonTask task = new CommonTask();

        //Заполнить spinners в отдельной  задаче
        task.execute(
                ()->{

                    //Создать набор записей строка + id
                    List<StringWithId> publications = Utils.publicationsRepository.getAll()
                            .stream()
                            .map(pub ->
                                    new StringWithId(
                                            String.format("%s  \"%s\"",pub.getPublicationType(),pub.getPublicationName()),
                                            pub.getId())
                            )
                            .collect(Collectors.toList());


                    //Действия после обращения к БД
                    return () -> {

                        //Задать список в spinner
                        Utils.setComplexSpinner(view.getContext(),publicationsSpinner,publications,
                                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);

                        //Прочитать значения из файла настроек после заполнения спиннера
                        fillFieldsFromPreferences(sharedPreferences);
                        return null;
                    };
                }
        );//execute
    }

    //Заполнить поля из файла настроек
    private void fillFieldsFromPreferences(SharedPreferences preferences){

        //Получение флага восстановления значений
        boolean recoverInput = preferences.getBoolean("recover_input",false);

        swtRecoverInput.setChecked(recoverInput);

        if (!recoverInput)
            return;

        //Получение даты
        String date = preferences.getString("date_start","01.01.2022");
        calendar.setTime(Utils.tryParseDate(date));
        sendingDate = calendar.getTime();
        btnDate.setText(date);

        int lastPubId = preferences.getInt("publication_id",1)/* Utils.tryParseInt(preferences.getString("publication_id","1"))*/;
        //Выбранный элемент выпадающего списка
        Utils.setSelectedById(publicationsSpinner,lastPubId);

        extDuration.setText(preferences.getString("subscription_duration","1"));

    }

    //Открыть диалог с датой
    private void onDateClickListener(View view){

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog = new DatePickerDialog(
                getContext(),android.R.style.Theme_DeviceDefault_Dialog,dateChangedListener
                ,year,month,day
        );

        //Задать фон диалога
        dateDialog.show();
    }

    //Обработчик выбора даты
    DatePickerDialog.OnDateSetListener dateChangedListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year,month,day);

            //Изменить поле ввода и переданную сущность
            btnDate.setText(Utils.dateFormat.format(calendar.getTime()));
            sendingDate = calendar.getTime();
        }
    };
}
