package com.step.wagner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.step.wagner.R;
import com.step.wagner.async_tasks.CommonTask;
import com.step.wagner.converters.AppointmentComplexConverter;
import com.step.wagner.fragments.dialogues.AppointmentAddDialog;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.infrastructure.JsonHelper;
import com.step.wagner.intefaces.AppointmentListener;
import com.step.wagner.intefaces.Receiver;
import com.step.wagner.models.entities.Appointment;
import com.step.wagner.models.entities.Doctor;
import com.step.wagner.models.entities.Patient;

import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

public class AppointmentsActivity extends AppCompatActivity {

    Button addAppointmentBtn;
    Button exportToJsonBtn;
    Button importFromJsonBtn;
    Receiver fragment;

    View currentView;

    private boolean isJsonShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        addAppointmentBtn = findViewById(R.id.addAppointment);
        addAppointmentBtn.setOnClickListener(v -> addAppointment());

        exportToJsonBtn = findViewById(R.id.btnExportToJson);
        importFromJsonBtn = findViewById(R.id.btnImportFromJson);

        exportToJsonBtn.setOnClickListener(v -> exportToJson());
        importFromJsonBtn.setOnClickListener(v -> importFromJson());

        currentView = this.findViewById(android.R.id.content);

        if (!addAppointmentBtn.isEnabled()) {
            addAppointmentBtn.setEnabled(true);
            exportToJsonBtn.setEnabled(true);
        }
    }

    //Получить результат запроса и задать результат во фрагмент
    @Override
    public void onResume() {
        super.onResume();

        fragment = (Receiver) getSupportFragmentManager().findFragmentById(R.id.receiverFragment);

        if (fragment == null)
            return;

        //Вывод таблиц во фрагменте
        fragment.tables(Parameters.APPOINTMENTS);
        isJsonShown = false;

    } // onResume

    //Включить кнопки, если они были отключены
    @Override
    protected void onRestart() {
        super.onRestart();

        if (!addAppointmentBtn.isEnabled()) {
            addAppointmentBtn.setEnabled(true);
            exportToJsonBtn.setEnabled(true);
        }
    }

    //Кнопка выхода
    @Override
    public void onBackPressed() {

        //Если сейчас выводится коллекция, прочитанная из JSON, тогда прочитать таблицу из БД и вывести её
        if (isJsonShown) {

            fragment.tables(Parameters.APPOINTMENTS);

            addAppointmentBtn.setEnabled(true);
            exportToJsonBtn.setEnabled(true);

            isJsonShown = false;
        } else finish();

    }

    //Формирование и добавление записи
    @SuppressLint("DefaultLocale")
    public void addAppointment() {

        AppointmentAddDialog appointmentAddDialog = new AppointmentAddDialog();

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
                                    Utils.showSnackBar(currentView, "Добавить приём не удалось!");

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
                                fragment.addInTable(appointment);

                                //Вывод сообщения
                                Utils.showSnackBar(currentView, "Запись успешно добавлена");

                                return null;
                            };
                        }
                );//execute

            }//onOkClickListener
        });

        appointmentAddDialog.show(getSupportFragmentManager(), "appointment_add_dialog");


    }

    //Выгрузка колелкции в JSON
    public void exportToJson() {

        CommonTask commonTask = new CommonTask();

        //Отключение кнопки для индикации загрузки
        exportToJsonBtn.setEnabled(false);

        //Запуск записи таблицы в JSON в отдельной задаче
        commonTask.execute(() -> {

            Utils.appointmentsRepository.open();

            List<Appointment> appointments = Utils.appointmentsRepository.getAll();

            Utils.appointmentsRepository.close();

            //Запись целой сущности приёма вместе с пациентами и докторами
            boolean result = JsonHelper.writeToJson(this,
                    appointments, new AppointmentComplexConverter()/*new AppointmentConverter()*/,
                    Parameters.APPOINTMENTS_FILE_NAME);

            //Что выводить после завершения записи
            return () -> {
                exportToJsonBtn.setEnabled(true);
                Utils.showSnackBar(currentView, String.format("Приёмы %s записаны в файл!", result ? "были" : "не были"));

                return null;
            };
        });//execute

    }//exportToJson

    //Загрузка колелкции из JSON
    public void importFromJson() {

        //Чтение объекта с id справочных сущностей
        /*CommonTask commonTask = new CommonTask();

        //Отключение кнопки для индикации загрузки
        importFromJsonBtn.setEnabled(false);

        //Запуск чтения коллекции приёмов
        commonTask.execute(() -> {

            //Прочитать приёмы из файла
            List<Appointment> appointments = JsonHelper.readFromJson(this,
                    new AppointmentConverter(),
                    Appointment.class, Appointment[].class,
                    Parameters.APPOINTMENTS_FILE_NAME);

            //Если прочитать не удалось, тогда уходим
            if (appointments == null){

                //Вернуть лямбду, которая выводит ошибку и разблокирует кнопку
                return () -> {

                    importFromJsonBtn.setEnabled(true);
                    Utils.showSnackBar(currentView, "Прочитать приёмы не удалось!");
                    return null;
                };
            }

            //Если прочитать удалось, тогда задать связанные сущности
            doctorsRepository.open();
            patientsRepository.open();

            for (Appointment appointment : appointments) {
                appointment.setDoctor( doctorsRepository.getById(appointment.getDoctor_id()) );
                appointment.setPatient( patientsRepository.getById(appointment.getPatient_id()) );
            }

            doctorsRepository.close();
            patientsRepository.close();

            //Создание отдельного объекта, реализуюещего функциональный интерфейс - для улучшения читаемости
            Supplier<Void> supplier = () -> {

                //Включить кнопку - индикация завершения чтения
                importFromJsonBtn.setEnabled(true);

                //Отключить кнопки
                addAppointmentBtn.setEnabled(false);

                exportToJsonBtn.setEnabled(false);

                //Задать колекцию фо фрагмент
                fragment.setCollection(appointments, Appointment.class);

                isJsonShown = true;
                Utils.showSnackBar(currentView, "Приёмы были прочитаны из файла");

                return null;
            };

            return supplier;
        });//excute*/

        CommonTask commonTask = new CommonTask();

        //Отключение кнопки для индикации загрузки
        importFromJsonBtn.setEnabled(false);

        //Запуск чтения коллекции приёмов вместе с ссылками на другие сущности, в асинхронной задаче
        commonTask.execute(() -> {

            //Прочитать приёмы из файла
            List<Appointment> appointments = JsonHelper.readFromJson(this,
                    new AppointmentComplexConverter(),
                    Appointment.class, Appointment[].class,
                    Parameters.APPOINTMENTS_FILE_NAME);

            //Создание отдельного объекта, реализуюещего функциональный интерфейс - для улучшения читаемости кода
            Supplier<Void> supplier = () -> {

                //Включить кнопку - индикация завершения чтения
                importFromJsonBtn.setEnabled(true);

                //Если данные прочиать не удалось или если прочитаны были некорректно
                if (appointments == null || appointments.get(0).getDoctor() == null || appointments.get(0).getPatient() == null) {
                    Utils.showSnackBar(currentView, "Прочитать приёмы не удалось!");
                    return null;
                }

                //Отключить кнопки
                addAppointmentBtn.setEnabled(false);

                exportToJsonBtn.setEnabled(false);

                //Задать колекцию фо фрагмент
                fragment.setCollection(appointments, Appointment.class);

                isJsonShown = true;
                Utils.showSnackBar(currentView, "Приёмы были прочитаны из файла");

                return null;
            };

            return supplier;
        });//execute

    }//importFromJson

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.btnExitFromActivity)
            finish();

        return super.onOptionsItemSelected(item);
    }
    //endregion
}