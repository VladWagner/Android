package com.step.wagner.content_providers.fragments.dialogues;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.step.wagner.content_providers.R;
import com.step.wagner.content_providers.async_tasks.CommonTask;
import com.step.wagner.content_providers.interfaces.SubscriptionListener;
import com.step.wagner.content_providers.infrastructure.StringWithId;
import com.step.wagner.content_providers.infrastructure.Utils;
import com.step.wagner.content_providers.models.entities.Subscription;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class SubscriptionEditDialog extends DialogFragment {

    private SubscriptionListener listener;
    private Spinner publicationsSpinner;

    private Button btnDate;

    private EditText extDuration;

    Subscription subscription;

    //Calendar для получения даты
    private Calendar calendar;

    public SubscriptionEditDialog(){
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View view = inflater.inflate(R.layout.subscription_dialog,null);

        Bundle bundle = getArguments();

        subscription = bundle.getParcelable("subscription");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(String.format("Редактирование подписки с id: %d", subscription.getId()))
                .setView(view)
                .setPositiveButton("Задать",okClickListener)
                .setNegativeButton("Отмена",null)
                .setCancelable(false) ;

        //Получить ссылки на элементы разметки
        calendar = Calendar.getInstance();
        calendar.setTime(subscription.getDateStart());
        publicationsSpinner = view.findViewById(R.id.publicationsSpinner);
        extDuration = view.findViewById(R.id.etxDuration);
        btnDate = view.findViewById(R.id.appointmentDateDialog);

        //Заполнить поля
        setFields(view);

        //Задать обработчик клика по edit text
        btnDate.setOnClickListener(this::onDateClickListener);

        return builder.create();
    }

    private DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Dialog dialog = getDialog();

            //Получить выбранные в spinners элементы
            int chosenPublicationId = ((StringWithId) publicationsSpinner.getSelectedItem()).getId();
            Integer duration = Utils.tryParseInt(extDuration.getText().toString());

            //Вызвать обработчик, заданный извне
            listener.onOkClickListener(subscription.getId(),calendar.getTime(),chosenPublicationId,duration != null ? duration : 1);

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

    //Заполнить поля
    private void setFields(View view){

        //Задать дату
        btnDate.setText(Utils.dateFormat.format(subscription.getDateStart()));
        extDuration.setText(String.valueOf(subscription.getDuration()));

        CommonTask task = new CommonTask();

        //Заполнить список специальностей в отдельной  задаче
        task.execute(
                ()->{

                    //Название + id издания
                    List<StringWithId> publications = Utils.publicationsRepository.getAll()
                            .stream()
                            .map(pub ->
                                    new StringWithId(
                                            String.format("%s '%s'",pub.getPublicationType(),pub.getPublicationName()),
                                            pub.getId())
                            )
                            .collect(Collectors.toList());


                    //Действия после обращения к БД
                    return () -> {

                        //Задать значения в выпадающий список фио докторов
                        Utils.setComplexSpinner(view.getContext(), publicationsSpinner,publications,
                                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);

                        //Вбрать определённые элементы
                        Utils.setSelectedById(publicationsSpinner, subscription.getPublication().getId());

                        return null;
                    };
                }
        );//execute
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
        //dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    //Обработчик выбора даты
    DatePickerDialog.OnDateSetListener dateChangedListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year,month,day);

            //Изменить поле ввода и переданную сущность
            btnDate.setText(Utils.dateFormat.format(calendar.getTime()));
            subscription.setDateStart(calendar.getTime());
        }
    };
}
