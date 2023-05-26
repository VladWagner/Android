package com.step.home_work.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.step.home_work.R;
import com.step.home_work.adapters.AppointmentAdapter;
import com.step.home_work.adapters.DoctorAdapter;
import com.step.home_work.adapters.PatientAdapter;
import com.step.home_work.adapters.Query5Adapter;
import com.step.home_work.adapters.Query6Adapter;
import com.step.home_work.adapters.Query7Adapter;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.intefaces.FragmentReceiver;
import com.step.home_work.models.entities.Appointment;
import com.step.home_work.models.entities.Doctor;
import com.step.home_work.models.entities.Patient;
import com.step.home_work.models.queries.Query5;
import com.step.home_work.models.queries.Query6;
import com.step.home_work.models.queries.Query7;
import com.step.home_work.repositories.BaseRepository;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReceiverFragment extends Fragment implements FragmentReceiver {

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
        switch (queryNumber) {
            case 1:

                //Запуск запроса в отдельном потоке
                new Thread(() ->
                {
                    String pattern = "Т%";
                    Utils.patientsRepository.open();

                    List<Patient> patients = Utils.patientsRepository.query1(pattern);

                    Utils.patientsRepository.close();

                    //Задать заголовок
                    titleTxv.post(() -> {
                        titleTxv.setText(String.format("Запрос 1: выборка пациентов по фамилии начинающейся на %s",pattern));
                    });

                    //Задать коллекцию в recyclerView
                    recyclerView.post(() -> {
                        recyclerView.setAdapter(new PatientAdapter(fragmentContext, patients));
                    });

                }).start();

                break;

            //Запрос 2
            case 2:

                //Запуск запроса 2
                new Thread(
                        () -> {
                            double percent = Utils.getRandom(1.5, 5.);
                            Utils.doctorsRepository.open();

                            List<Doctor> doctors = Utils.doctorsRepository.query2(percent);

                            Utils.doctorsRepository.close();

                            //Задать заголовок
                            titleTxv.post(() -> {
                                titleTxv.setText(String.format("Запрос 2: доктора с процентом отчислений > %.2f", percent));
                            });

                            //Задать коллекцию в recyclerView
                            recyclerView.post(() -> {
                                recyclerView.setAdapter(new DoctorAdapter(fragmentContext, doctors));
                            });
                        }
                ).start();

                break;

            //Запрос 3
            case 3:

                new Thread(() -> {
                    String dateMin = "01.10.2021";
                    String dateMax = "01.01.2022";

                    //Получить параметры даты
                    Date dateStart;
                    Date dateEnd;
                    try {
                        dateStart = Utils.dateFormat.parse(dateMin);
                        dateEnd = Utils.dateFormat.parse(dateMax);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    Utils.appointmentsRepository.open();

                    List<Appointment> appointments = Utils.appointmentsRepository.query3(dateStart, dateEnd);

                    Utils.appointmentsRepository.close();

                    //Задать заголовок
                    titleTxv.post(() -> {
                        titleTxv.setText(String.format("Запрос 3: приёмы в диапазоне дат %s → %s", dateMin, dateMax));
                    });

                    //Задать коллекцию в recyclerView
                    recyclerView.post(() -> {
                        recyclerView.setAdapter(new AppointmentAdapter(fragmentContext, appointments, false));
                    });

                }).start();

                break;

            //Запрос 4
            case 4:

                new Thread(() -> {
                    String speciality = "терапевт";
                    Utils.doctorsRepository.open();

                    List<Doctor> doctors = Utils.doctorsRepository.query4(speciality);

                    Utils.doctorsRepository.close();

                    //Задать заголовок
                    titleTxv.post(() -> {
                        titleTxv.setText(String.format("Запрос 4: доктора со специальностью %s", speciality));
                    });

                    //Задать коллекцию в recyclerView
                    recyclerView.post(() -> {
                        recyclerView.setAdapter(new DoctorAdapter(fragmentContext, doctors));
                    });
                }).start();

                break;

            //Запрос 5
            case 5:

                new Thread(() -> {
                    Utils.doctorsRepository.open();

                    List<Query5> query5Result = Utils.doctorsRepository.query5();

                    Utils.doctorsRepository.close();

                    //Задать заголовок
                    titleTxv.post(() -> {
                        titleTxv.setText("Запрос 5: зарплата варчей");
                    });

                    //Задать коллекцию в recyclerView
                    recyclerView.post(() -> {
                        recyclerView.setAdapter(new Query5Adapter(fragmentContext, query5Result));
                    });
                }).start();

                break;

            //Запрос 6
            case 6:

                new Thread(() -> {
                    Utils.appointmentsRepository.open();

                    List<Query6> query6Result = Utils.appointmentsRepository.query6();

                    Utils.appointmentsRepository.close();

                    query6Result.sort((q1,q2) -> q2.getAmount() - q1.getAmount());

                    //Задать заголовок
                    titleTxv.post(() -> {
                        titleTxv.setText("Запрос 6: группировку по полю дата приема");
                    });

                    //Задать коллекцию в recyclerView
                    recyclerView.post(() -> {
                        recyclerView.setAdapter(new Query6Adapter(fragmentContext, query6Result));
                    });
                }).start();

                break;

            //Запрос 7
            case 7:

                new Thread(() -> {
                    Utils.doctorsRepository.open();

                    List<Query7> query7Result = Utils.doctorsRepository.query7();

                    Utils.doctorsRepository.close();

                    query7Result.sort((q1,q2) -> q2.getAmount() - q1.getAmount());

                    //Задать заголовок
                    titleTxv.post(() -> {
                        titleTxv.setText("Запрос 7: группировку по полю специальность");
                    });

                    //Задать коллекцию в recyclerView
                    recyclerView.post(() -> {
                        recyclerView.setAdapter(new Query7Adapter(fragmentContext, query7Result));
                    });
                }).start();
                break;
        }//switch

    }

    //Вывод таблиц
    @Override
    public void tables(String entityType) {

        switch (entityType) {
            case Parameters.APPOINTMENTS:
                new Thread(() -> {
                    Utils.appointmentsRepository.open();

                    List<Appointment> appointments = Utils.appointmentsRepository.getAll();

                    Utils.appointmentsRepository.close();

                    //Задать заголовок
                    titleTxv.post(() -> {
                        titleTxv.setText("Таблица приемов");
                    });


                    //Задать коллекцию в recyclerView
                    recyclerView.post(() -> {
                        recyclerView.setAdapter(new AppointmentAdapter(fragmentContext, appointments, true));
                    });

                }).start();
                break;
            case Parameters.DOCTORS:
                new Thread(() -> {
                    Utils.doctorsRepository.open();

                    List<Doctor> doctors = Utils.doctorsRepository.getAll();

                    Utils.doctorsRepository.close();

                    //Задать заголовок
                    titleTxv.post(() -> {
                        titleTxv.setText("Таблица докторов");
                    });

                    //Задать коллекцию в recyclerView
                    recyclerView.post(() -> {
                        recyclerView.setAdapter(new DoctorAdapter(fragmentContext, doctors));
                    });

                }).start();
                break;
            case Parameters.PATIENTS:
                new Thread(() -> {
                    Utils.patientsRepository.open();

                    List<Patient> patients = Utils.patientsRepository.getAll();

                    Utils.patientsRepository.close();

                    //Задать заголовок
                    titleTxv.post(() -> {
                        titleTxv.setText("Таблица пациентов");
                    });

                    //Задать коллекцию в recyclerView
                    recyclerView.post(() -> {
                        recyclerView.setAdapter(new PatientAdapter(fragmentContext, patients));
                    });

                }).start();
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

}