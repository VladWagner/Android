package com.step.wagner.activities;

import static com.step.wagner.infrastructure.Utils.doctorsRepository;
import static com.step.wagner.infrastructure.Utils.patientsRepository;

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
import com.step.wagner.converters.AppointmentConverter;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.infrastructure.JsonHelper;
import com.step.wagner.intefaces.FragmentReceiver;
import com.step.wagner.models.entities.Appointment;
import com.step.wagner.models.entities.Doctor;
import com.step.wagner.models.entities.Patient;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class AppointmentsActivity extends AppCompatActivity {

    Button addAppointmentBtn;
    Button exportToJsonBtn;
    Button importFromJsonBtn;
    FragmentReceiver fragment;

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

        fragment = (FragmentReceiver) getSupportFragmentManager().findFragmentById(R.id.receiverFragment);

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

        //Открыть соединения
        doctorsRepository.open();
        patientsRepository.open();

        //Получить списки первичных ключей
        List<Integer> doctorsIds = doctorsRepository.getIdsList();
        List<Integer> patientsIds = patientsRepository.getIdsList();

        //Получить сущности по Id
        int doctorId = doctorsIds.get(Utils.getRandom(0, doctorsIds.size()));
        int patientId = patientsIds.get(Utils.getRandom(0, patientsIds.size()));

        Doctor doctor = doctorsRepository.getById(doctorId);
        Patient patient = patientsRepository.getById(patientId);

        //Закрыть соединения
        doctorsRepository.close();
        patientsRepository.close();

        Appointment appointment = new Appointment(0,
                Utils.getRandomDate(),
                patient,
                doctor
        );

        fragment.addInTable(appointment);

        //Вывод сообщения
        Utils.showSnackBar(currentView, "Запись успешно добавлена");

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
        });//exуcute

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