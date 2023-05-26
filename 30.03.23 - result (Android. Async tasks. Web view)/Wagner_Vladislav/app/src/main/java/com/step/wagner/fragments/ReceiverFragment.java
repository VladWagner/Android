package com.step.wagner.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.step.wagner.R;
import com.step.wagner.adapters.AppointmentAdapter;
import com.step.wagner.adapters.BaseAdapter;
import com.step.wagner.adapters.DoctorAdapter;
import com.step.wagner.adapters.PatientAdapter;
import com.step.wagner.adapters.Query5Adapter;
import com.step.wagner.adapters.Query6Adapter;
import com.step.wagner.adapters.Query7Adapter;
import com.step.wagner.async_tasks.DataBaseTask;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.SimpleTuple;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.FragmentReceiver;
import com.step.wagner.models.entities.Appointment;
import com.step.wagner.models.entities.Doctor;
import com.step.wagner.models.entities.Patient;
import com.step.wagner.models.queries.Query5;
import com.step.wagner.models.queries.Query6;
import com.step.wagner.models.queries.Query7;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;


public class ReceiverFragment extends Fragment implements FragmentReceiver {

    RecyclerView recyclerView;

    TextView titleTxv;

    Context fragmentContext;
/*
    private DataBaseTask dbTask;*/

    public ReceiverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Загрузить разметку
        return inflater.inflate(R.layout.fragment_receiver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Получить ссылку на recyclerView
        recyclerView = view.findViewById(R.id.rcvResult);

        //Получить ссылку на textView
        titleTxv = view.findViewById(R.id.receiverTitle);
        fragmentContext = getContext();


    } // onViewCreated


    //Запросы
    @SuppressLint("DefaultLocale")
    @Override
    public void queries(int queryNumber) {
        switch (queryNumber) {

            //Запрос 1
            case 1:

                //Создать объект асинхронной задачи
                DataBaseTask<Patient> dbTaskQuery1 = new DataBaseTask<>();

                dbTaskQuery1.linkFragment(this);


                //Запуск выполнения запроса
                dbTaskQuery1.execute(() -> {

                    String pattern = "Т%";
                    Utils.patientsRepository.open();

                    List<Patient> patients = Utils.patientsRepository.query1(pattern);

                    Utils.patientsRepository.close();

                    //Адаптер + строка для задания заголовка после завершения
                    return new SimpleTuple<>(
                            new PatientAdapter(fragmentContext, patients),
                            String.format("Запрос 1: выборка пациентов по фамилии начинающейся на %s", pattern)
                    );
                });

                break;

            //Запрос 2
            case 2:

                //Создать объект асинхронной задачи
                DataBaseTask<Doctor> dbTaskQuery2 = new DataBaseTask<>();

                dbTaskQuery2.linkFragment(this);


                //Запуск выполнения запроса
                dbTaskQuery2.execute(() -> {

                    double percent = Utils.getRandom(1.5, 5.);
                    Utils.doctorsRepository.open();

                    List<Doctor> doctors = Utils.doctorsRepository.query2(percent);

                    Utils.doctorsRepository.close();

                    //Адаптер + строка для задания заголовка после завершения
                    return new SimpleTuple<>(
                            new DoctorAdapter(fragmentContext, doctors),
                            String.format("Запрос 2: доктора с процентом отчислений > %.2f", percent)
                    );
                });
                break;

            //Запрос 3
            case 3:

                //Создать объект асинхронной задачи
                DataBaseTask<Appointment> dbTaskQuery3 = new DataBaseTask<>();

                dbTaskQuery3.linkFragment(this);


                //Запуск выполнения запроса
                dbTaskQuery3.execute(() -> {

                    String dateMin = "01.10.2021";
                    String dateMax = "01.01.2022";

                    //Получить параметры даты
                    Date dateStart = Utils.tryParseDate(dateMin);
                    Date dateEnd = Utils.tryParseDate(dateMax);

                    Utils.appointmentsRepository.open();

                    List<Appointment> appointments = Utils.appointmentsRepository.query3(dateStart, dateEnd);

                    Utils.appointmentsRepository.close();

                    //Адаптер + строка для задания заголовка после завершения
                    return new SimpleTuple<>(
                            new AppointmentAdapter(fragmentContext, appointments, false),
                            String.format("Запрос 3: приёмы в диапазоне дат %s → %s", dateMin, dateMax)
                    );
                });

                break;

            //Запрос 4
            case 4:

                //Создать объект асинхронной задачи
                DataBaseTask<Doctor> dbTaskQuery4 = new DataBaseTask<>();

                dbTaskQuery4.linkFragment(this);


                //Запуск выполнения запроса
                dbTaskQuery4.execute(() -> {

                    String speciality = "терапевт";
                    Utils.doctorsRepository.open();

                    List<Doctor> doctors = Utils.doctorsRepository.query4(speciality);

                    Utils.doctorsRepository.close();

                    //Адаптер + строка для задания заголовка после завершения
                    return new SimpleTuple<>(
                            new DoctorAdapter(fragmentContext, doctors),
                            String.format("Запрос 4: доктора со специальностью %s", speciality)
                    );
                });

                break;

            //Запрос 5
            case 5:

                //Создать объект асинхронной задачи
                DataBaseTask<Query5> dbTaskQuery5 = new DataBaseTask<>();

                dbTaskQuery5.linkFragment(this);

                //Запуск выполнения запроса
                dbTaskQuery5.execute(() -> {

                    Utils.doctorsRepository.open();

                    List<Query5> query5Result = Utils.doctorsRepository.query5();

                    Utils.doctorsRepository.close();

                    //Адаптер + строка для задания заголовка после завершения
                    return new SimpleTuple<>(
                            new Query5Adapter(fragmentContext, query5Result),
                            "Запрос 5: зарплата варчей"
                    );
                });

                break;

            //Запрос 6
            case 6:

                //Создать объект асинхронной задачи
                DataBaseTask<Query6> dbTaskQuery6 = new DataBaseTask<>();

                dbTaskQuery6.linkFragment(this);

                //Запуск выполнения запроса
                dbTaskQuery6.execute(() -> {

                    Utils.appointmentsRepository.open();

                    List<Query6> query6Result = Utils.appointmentsRepository.query6();

                    Utils.appointmentsRepository.close();

                    query6Result.sort((q1, q2) -> q2.getAmount() - q1.getAmount());

                    //Адаптер + строка для задания заголовка после завершения
                    return new SimpleTuple<>(
                            new Query6Adapter(fragmentContext, query6Result),
                            "Запрос 6: группировку по полю дата приема"
                    );
                });

                break;

            //Запрос 7
            case 7:

                //Создать объект асинхронной задачи
                DataBaseTask<Query7> dbTaskQuery7 = new DataBaseTask<>();

                dbTaskQuery7.linkFragment(this);

                //Запуск выполнения запроса
                dbTaskQuery7.execute(() -> {

                    Utils.doctorsRepository.open();

                    List<Query7> query7Result = Utils.doctorsRepository.query7();

                    Utils.doctorsRepository.close();

                    query7Result.sort((q1, q2) -> q2.getAmount() - q1.getAmount());

                    //Адаптер + строка для задания заголовка после завершения
                    return new SimpleTuple<>(
                            new Query7Adapter(fragmentContext, query7Result),
                            "Запрос 7: группировку по полю специальность"
                    );
                });

                break;
        }//switch

    }

    //Вывод таблиц
    @Override
    public void tables(String entityType) {

        switch (entityType) {
            case Parameters.APPOINTMENTS:

                //Создать объект асинхронной задачи
                DataBaseTask<Appointment> DBTaskAppointments = new DataBaseTask<>();

                DBTaskAppointments.linkFragment(this);

                //Запуск выполнения запроса
                DBTaskAppointments.execute(() -> {

                    Utils.appointmentsRepository.open();

                    List<Appointment> appointments = Utils.appointmentsRepository.getAll();

                    Utils.appointmentsRepository.close();

                    //Адаптер + строка для задания заголовка после завершения
                    return new SimpleTuple<>(
                            new AppointmentAdapter(fragmentContext, appointments, true),
                            "Таблица приемов"
                    );
                });

                break;
            case Parameters.DOCTORS:

                //Создать объект асинхронной задачи
                DataBaseTask<Doctor> DBTaskDoctors = new DataBaseTask<>();

                DBTaskDoctors.linkFragment(this);

                //Запуск выполнения запроса
                DBTaskDoctors.execute(() -> {

                    Utils.doctorsRepository.open();

                    List<Doctor> doctors = Utils.doctorsRepository.getAll();

                    Utils.doctorsRepository.close();

                    //Адаптер + строка для задания заголовка после завершения
                    return new SimpleTuple<>(
                            new DoctorAdapter(fragmentContext, doctors),
                            "Таблица докторов"
                    );
                });

                break;
            case Parameters.PATIENTS:

                //Создать объект асинхронной задачи
                DataBaseTask<Patient> DBTaskPatients = new DataBaseTask<>();

                DBTaskPatients.linkFragment(this);

                //Запуск выполнения запроса
                DBTaskPatients.execute(() -> {

                    Utils.patientsRepository.open();

                    List<Patient> patients = Utils.patientsRepository.getAll();

                    Utils.patientsRepository.close();

                    //Адаптер + строка для задания заголовка после завершения
                    return new SimpleTuple<>(
                            new PatientAdapter(fragmentContext, patients),
                            "Таблица пациентов"
                    );
                });

                break;
        }

    }

    //Добавление записей
    @Override
    public <T> void addInTable(T entity) {

        //Использовать switch для проверки типов не получилось

        if (entity.getClass().getName().equalsIgnoreCase(Appointment.class.getName())) {
            Utils.appointmentsRepository.open();

            Utils.appointmentsRepository.insert((Appointment) entity);

            //Получить записи после добавления
            List<Appointment> appointments = Utils.appointmentsRepository.getAll();

            Utils.appointmentsRepository.close();

            //Сортировка по убыванию
            appointments.sort((a1, a2) -> a2.getId() - a1.getId());


            recyclerView.setAdapter(new AppointmentAdapter(fragmentContext, appointments, true));
        }

    }

    //Установка коллекции, полученной не из БД
    @Override
    public <T> void setCollection(List<T> entities, Class<T> entityType) {

        if (entityType.getName().equalsIgnoreCase(Appointment.class.getName())) {

            //Задать коллекцию в recyclerView
            recyclerView.post(() -> {
                recyclerView.setAdapter(new AppointmentAdapter(fragmentContext, (List<Appointment>) entities, false));
            });
        }
    }

    //Установить адаптер в recycler vie
    public <T> void setRecyclerViewAdapter(BaseAdapter<T> adapter) {
        if (adapter == null) return;

        recyclerView.setAdapter(adapter);
    }

    public void setTxvValue(String value) {
        if (value.isBlank())
            return;

        titleTxv.setText(value);
    }
}