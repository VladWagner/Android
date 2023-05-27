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
import android.widget.Button;
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
import com.step.wagner.fragments.dialogues.Query1Dialog;
import com.step.wagner.fragments.dialogues.Query2Dialog;
import com.step.wagner.fragments.dialogues.Query3Dialog;
import com.step.wagner.fragments.dialogues.Query4Dialog;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.SimpleTuple;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.Receiver;
import com.step.wagner.models.entities.Appointment;
import com.step.wagner.models.entities.Doctor;
import com.step.wagner.models.entities.Patient;
import com.step.wagner.models.queries.Query5;
import com.step.wagner.models.queries.Query6;
import com.step.wagner.models.queries.Query7;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ReceiverFragment extends Fragment implements Receiver {

    RecyclerView recyclerView;

    TextView titleTxv;

    Context fragmentContext;


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

        Button setParamsBtn = getActivity().findViewById(R.id.btnSetParams);
        View parentsView = getActivity().findViewById(android.R.id.content);
        switch (queryNumber) {

            //Запрос 1
            case 1:

                Query1Dialog query1Dialog = new Query1Dialog();

                //Задать обработчик задания параметра
                query1Dialog.setListener((String... params) -> {
                    if (/*params[0].isBlank() || */params[0].contains("%"))
                        return;

                    //Создать объект асинхронной задачи
                    DataBaseTask<Patient> dbTaskQuery1 = new DataBaseTask<>();

                    dbTaskQuery1.linkFragment(this);


                    //Запуск выполнения запроса
                    dbTaskQuery1.execute(() -> {

                        Utils.patientsRepository.open();

                        List<Patient> patients = !params[0].isBlank() ? Utils.patientsRepository.query1(params[0] + "%") : new ArrayList<>();

                        Utils.patientsRepository.close();

                        String title = "";

                        if (!params[0].isBlank())
                            title = patients.size() > 0 ? String.format("выборка пациентов по фамилии начинающейся на %s", params[0])
                                    : "ничего не найдено";
                        else
                            title = "параметр не задан!";

                        //Адаптер + строка для задания заголовка после завершения
                        return new SimpleTuple<>(
                                new PatientAdapter(fragmentContext, patients),
                                String.format("Запрос 1: %s", title)
                        );
                    });//execute
                });//setListener

                query1Dialog.show(getParentFragmentManager(), "query_1_dialog");

                break;

            //Запрос 2
            case 2:

                Query2Dialog query2Dialog = new Query2Dialog();

                //Задать обработчик задания параметра
                query2Dialog.setListener((String... params) -> {
                    if (params[0] == null) {
                        Utils.showSnackBar(parentsView, "Параметры заданы некорректно!");

                        return;
                    } else if (params[0].isBlank()) {

                        Utils.showSnackBar(parentsView, "Параметры заданы некорректно!");
                        recyclerView.setAdapter(new DoctorAdapter(getContext(), new ArrayList<>()));

                        titleTxv.setText("Запрос 2: параметр не задан!");

                        return;
                    }

                    //Создать объект асинхронной задачи
                    DataBaseTask<Doctor> dbTaskQuery2 = new DataBaseTask<>();

                    dbTaskQuery2.linkFragment(this);


                    //Запуск выполнения запроса
                    dbTaskQuery2.execute(() -> {

                        //Получить заданный параметр
                        Double percent = Utils.tryParseDouble(params[0]);
                        Utils.doctorsRepository.open();

                        //Если значение задано некорректно, тогда создать пустой список
                        List<Doctor> doctors = percent != null ? Utils.doctorsRepository.query2(percent) : new ArrayList<>();

                        Utils.doctorsRepository.close();

                        String title = doctors.size() > 0 ? String.format("доктора с процентом отчислений > %.2f", percent)
                                : "ничего не найдено";

                        //Адаптер + строка для задания заголовка после завершения
                        return new SimpleTuple<>(
                                new DoctorAdapter(fragmentContext, doctors),
                                String.format("Запрос 2: %s", title)
                        );
                    });//execute
                });//setListener

                query2Dialog.show(getParentFragmentManager(), "query_2_dialog");

                break;

            //Запрос 3
            case 3:

                DataBaseTask<Appointment> dbTaskQuery3 = new DataBaseTask<>();

                dbTaskQuery3.linkFragment(this);

                //Выбрать все записи, чтобы можно было посмотреть существующие записи
                dbTaskQuery3.execute(() -> {

                    Utils.appointmentsRepository.open();

                    List<Appointment> appointments = Utils.appointmentsRepository.getAll();

                    Utils.appointmentsRepository.close();

                    //Адаптер + строка для задания заголовка после завершения
                    return new SimpleTuple<>(
                            new AppointmentAdapter(fragmentContext, appointments, false, getParentFragmentManager()),
                            "Запрос 3: выбраны все записи из таблицы приёмов"
                    );
                });

                Query3Dialog query3Dialog = new Query3Dialog();

                query3Dialog.show(getParentFragmentManager(), "query_3_dialog");


                //Задать обработчик задания параметра
                query3Dialog.setListener((Calendar... params) -> {
                    if ((params[0] == null || params[1] == null)) {
                        Utils.showSnackBar(parentsView, "Параметры заданы некорректно!");

                        return;
                    } else if (params[0].getTimeInMillis() > params[1].getTimeInMillis()) {

                        Utils.showSnackBar(parentsView, "Параметры заданы некорректно!");
                        recyclerView.setAdapter(new AppointmentAdapter(getContext(), new ArrayList<>(), false, getParentFragmentManager()));

                        titleTxv.setText("Минимальная дата > максимальной. Измените диапазон!");

                        return;
                    }

                    DataBaseTask<Appointment> dbTaskQuery3Dialog = new DataBaseTask<>();

                    dbTaskQuery3Dialog.linkFragment(this);

                    //Запуск выполнения запроса
                    dbTaskQuery3Dialog.execute(() -> {

                        Calendar mimDateCalendar = params[0];
                        Calendar maxDateCalendar = params[1];

                        Date minDate = mimDateCalendar.getTime();
                        Date maxDate = maxDateCalendar.getTime();

                        Utils.appointmentsRepository.open();

                        List<Appointment> appointments = mimDateCalendar.getTimeInMillis() < maxDateCalendar.getTimeInMillis() ?
                                Utils.appointmentsRepository.query3(minDate, maxDate) :
                                new ArrayList<>();

                        Utils.appointmentsRepository.close();


                        String title = appointments.size() > 0 ? String.format("приёмы в диапазоне дат %s → %s", Utils.dateFormat.format(minDate), Utils.dateFormat.format(maxDate))
                                : "ничего не найдено";

                        //Адаптер + строка для задания заголовка после завершения
                        return new SimpleTuple<>(
                                new AppointmentAdapter(fragmentContext, appointments, false, getParentFragmentManager()),
                                String.format("Запрос 3: %s", title)
                        );
                    });

                });//setListener
                break;

            //Запрос 4
            case 4:

                Query4Dialog query4Dialog = new Query4Dialog();

                //Задать обработчик задания параметра
                query4Dialog.setListener((String... params) -> {

                    if (params[0] == null) {
                        Utils.showSnackBar(parentsView, "Параметры заданы некорректно!");

                        return;
                    } else if (params[0].isBlank()) {

                        Utils.showSnackBar(parentsView, "Параметры заданы некорректно!");
                        recyclerView.setAdapter(new DoctorAdapter(getContext(), new ArrayList<>()));

                        titleTxv.setText("Запрос 4: параметр не задан!");

                        return;
                    }

                    DataBaseTask<Doctor> dbTaskQuery4 = new DataBaseTask<>();

                    dbTaskQuery4.linkFragment(this);


                    //Запуск выполнения запроса
                    dbTaskQuery4.execute(() -> {

                        Utils.doctorsRepository.open();

                        //Если параметры не заданы, тогда выбрать все записи
                        List<Doctor> doctors = !params[0].isBlank() ? Utils.doctorsRepository.query4(params[0]) : Utils.doctorsRepository.getAll();

                        Utils.doctorsRepository.close();

                        String title = "";

                        if (!params[0].isBlank())
                            //Если параметр задан, но при этом данные найдены не были
                            title = doctors.size() > 0 ? String.format("доктора со специальностью %s", params[0])
                                    : "доктора по заданной специальности не найдены!";

                        //Адаптер + строка для задания заголовка после завершения
                        return new SimpleTuple<>(
                                new DoctorAdapter(fragmentContext, doctors),
                                String.format("Запрос 4: %s", title)
                        );
                    });
                });//setListener


                query4Dialog.show(getParentFragmentManager(), "query_4_dialog");


                break;

            //Запрос 5
            case 5:


                if (setParamsBtn != null)
                    setParamsBtn.setVisibility(View.GONE);

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

                if (setParamsBtn != null)
                    setParamsBtn.setVisibility(View.GONE);

                getActivity().findViewById(R.id.btnSetParams).setVisibility(View.GONE);
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

                if (setParamsBtn != null)
                    setParamsBtn.setVisibility(View.GONE);

                getActivity().findViewById(R.id.btnSetParams).setVisibility(View.GONE);
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
                            new AppointmentAdapter(fragmentContext, appointments, true, getParentFragmentManager()),
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


            recyclerView.setAdapter(new AppointmentAdapter(fragmentContext, appointments, true, getParentFragmentManager()));
        }

    }

    //Установка коллекции, полученной не из БД
    @Override
    public <T> void setCollection(List<T> entities, Class<T> entityType) {

        if (entityType.getName().equalsIgnoreCase(Appointment.class.getName())) {

            //Задать коллекцию в recyclerView
            recyclerView.post(() -> {
                recyclerView.setAdapter(new AppointmentAdapter(fragmentContext, (List<Appointment>) entities, false, getParentFragmentManager()));
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