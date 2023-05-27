package com.step.wagner.fragments.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.step.wagner.R;
import com.step.wagner.async_tasks.CommonTask;
import com.step.wagner.fragments.dialogues.AppointmentAddDialog;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.AppointmentListener;
import com.step.wagner.intefaces.Receiver;
import com.step.wagner.models.entities.Appointment;
import com.step.wagner.models.entities.Doctor;
import com.step.wagner.models.entities.Patient;

import java.util.Date;

public class AppointmentsPageFragment extends Fragment {

    //Фрагмент для вывода даблицы и добавления записей
    Receiver receiverFragment;

    //Кнокпа добавления
    private Button btnAdd;


    public AppointmentsPageFragment() {
    }


    //Фабричный метод
    public static AppointmentsPageFragment newInstance(){
        return new AppointmentsPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_appointments_page, container, false);

        (view.findViewById(R.id.addAppointment)).setOnClickListener(v -> addAppointment());

        //Получение фрагмента для вывода и добавления элемента таблицу
        receiverFragment = (Receiver) getChildFragmentManager().findFragmentById(R.id.outputFragment);

        receiverFragment.tables(Parameters.APPOINTMENTS);

        return view;
    }

    private void addAppointment(){
        AppointmentAddDialog appointmentAddDialog = new AppointmentAddDialog();

        View parentsView = getActivity().findViewById(android.R.id.content);
        //Обработчик клика в диалоге
        appointmentAddDialog.setListener(new AppointmentListener() {
            @Override
            public void onOkClickListener(int id, Date appointmentDate, int doctorId, int patientId) {

                CommonTask task = new CommonTask();
                //Добавление запись приёма в одельной задаче
                task.execute(
                        ()->{

                            //Если полученные значения некорреткны
                            if (appointmentDate == null || doctorId == 0 || patientId == 0)
                                return () -> {
                                    //Вывод сообщения
                                    Utils.showSnackBar(parentsView, "Добавить приём не удалось!");

                                    return null;
                                };

                            //Получить фио докторов и пациентов
                            Utils.doctorsRepository.open();
                            Utils.patientsRepository.open();

                            Doctor doctor = Utils.doctorsRepository.getById(doctorId);
                            Patient patient = Utils.patientsRepository.getById(patientId);

                            Utils.doctorsRepository.close();
                            Utils.patientsRepository.close();


                            Appointment appointment = new Appointment(id,appointmentDate,patient,doctor);

                            //Действия после обращения к БД
                            return () -> {

                                //Добавить приём в БД
                                receiverFragment.addInTable(appointment);

                                //Вывод сообщения
                                Utils.showSnackBar(parentsView, "Запись успешно добавлена");

                                return null;
                            };
                        }
                );//execute

            }//onOkClickListener
        });

        appointmentAddDialog.show(getParentFragmentManager(), "appointment_add_dialog");
    }

}