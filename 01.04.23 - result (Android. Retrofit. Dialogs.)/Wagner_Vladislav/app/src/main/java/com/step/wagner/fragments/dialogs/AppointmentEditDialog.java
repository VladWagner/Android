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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.step.wagner.R;
import com.step.wagner.async_tasks.CommonTask;
import com.step.wagner.infrastructure.StringWithId;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.AppointmentListener;
import com.step.wagner.models.entities.Appointment;

import java.io.BufferedReader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentEditDialog extends DialogFragment {

    //Callback для клика по кнопке задать
    private AppointmentListener listener;
    private Spinner doctorsSpinner;
    private Spinner patientsSpinner;

    private Button dateField;

    Appointment appointment;

    //Calendar для получения даты
    private Calendar calendar;

    public AppointmentEditDialog(){
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        //LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.appointment_dialog,null);
        ScrollView scrollView = (ScrollView) inflater.inflate(R.layout.appointment_dialog,null);

        Bundle bundle = getArguments();

        appointment = bundle.getParcelable("appointment");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(String.format("Редактирование приёма с id: %d",appointment.getId()))
                .setView(scrollView)
                .setPositiveButton("Задать",okClickListener)
                .setNegativeButton("Отмена",null/*(DialogInterface dialogInterface, int i) ->listener.onOkClickListener(" ")*/)
                .setCancelable(false) ;


        calendar = Calendar.getInstance();
        calendar.setTime(appointment.getAppointmentDate());
        doctorsSpinner = scrollView.findViewById(R.id.doctorsSpinner);
        patientsSpinner = scrollView.findViewById(R.id.patientsSpinner);
        dateField = scrollView.findViewById(R.id.appointmentDateDialog);

        //setFields(linearLayout);
        setFields(scrollView);

        //Задать обработчик клика по edit text
        dateField.setOnClickListener(this::onDateClickListener);

        return builder.create();
    }

    private DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Dialog dialog = getDialog();

            //Получить выбранные в spinners элементы
            int chosenDoctorId = ((StringWithId)doctorsSpinner.getSelectedItem()).getId();
            int chosenPatientId = ((StringWithId)patientsSpinner.getSelectedItem()).getId();

            //Вызвать обработчик, заданный извне
            listener.onOkClickListener(appointment.getId(),calendar.getTime(),chosenDoctorId,chosenPatientId);

        }
    };

    //Обработчик выхода из диалога
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    //Задать обработчика извне
    public void setListener(AppointmentListener listener){
        if (listener == null) return;

        this.listener = listener;
    }

    //Заполнить поля
    private void setFields(ScrollView scrollView /*LinearLayout linearLayout*/){

        //Задать дату
        dateField.setText(Utils.dateFormat.format(appointment.getAppointmentDate()));

        CommonTask task = new CommonTask();

        //Заполнить список специальностей в отдельной  задаче
        task.execute(
                ()->{

                    //Получить фио докторов и пациентов
                    Utils.doctorsRepository.open();
                    Utils.patientsRepository.open();

                    //ФИО + id докторов
                    List<StringWithId> doctors = Utils.doctorsRepository.getAll()
                            .stream()
                            .map(doc ->
                                    new StringWithId(
                                            String.format("%s.%s.%s",doc.getSurname(),doc.getName().charAt(0),doc.getPatronymic().charAt(0))
                                            ,doc.getId()
                                    ))
                            .collect(Collectors.toList());

                    //ФИО + id пациентов
                    List<StringWithId> patients = Utils.patientsRepository.getAll()
                            .stream()
                            .map(patient ->
                                    new StringWithId(
                                            String.format("%s.%s.%s",patient.getSurname(),patient.getName().charAt(0),patient.getPatronymic().charAt(0))
                                            ,patient.getId()
                                    ))
                            .collect(Collectors.toList());

                    Utils.doctorsRepository.close();
                    Utils.patientsRepository.close();

                    //Действия после обращения к БД
                    return () -> {

                        //Задать значения в выпадающий список фио докторов
                        Utils.setParticularSpinner(scrollView.getContext(), doctorsSpinner,doctors,
                                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);

                        //Задать значения в выпадающий список фио пациентов
                        Utils.setParticularSpinner(scrollView.getContext(), patientsSpinner,patients,
                                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);

                        //Вбрать определённые элементы
                        Utils.setSelectedById(doctorsSpinner,appointment.getDoctor().getId());
                        Utils.setSelectedById(patientsSpinner,appointment.getPatient().getId());

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
                getContext(),android.R.style.Theme_Holo_Dialog_MinWidth,dateChangedListener
                ,year,month,day
        );

        //Задать фон диалога
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    //Обработчик выбора даты
    DatePickerDialog.OnDateSetListener dateChangedListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            calendar.set(year,month,day);

            //Изменить поле ввода и переданную сущность
            dateField.setText(Utils.dateFormat.format(calendar.getTime()));
            appointment.setAppointmentDate(calendar.getTime());
        }
    };
}
