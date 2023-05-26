package com.step.home_work.activities;

import static com.step.home_work.infrastructure.Utils.doctorsRepository;
import static com.step.home_work.infrastructure.Utils.patientsRepository;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.step.home_work.R;
import com.step.home_work.converters.AppointmentComplexConverter;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.infrastructure.JsonHelper;
import com.step.home_work.intefaces.FragmentReceiver;
import com.step.home_work.models.entities.Appointment;
import com.step.home_work.models.entities.Doctor;
import com.step.home_work.models.entities.Patient;

import java.util.List;

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
    public void onBackPressed(){

        //Если сейчас выводится коллекция, прочитанная из JSON, тогда прочитать таблицу из БД и вывести её
        if (isJsonShown){

            fragment.tables(Parameters.APPOINTMENTS);

            addAppointmentBtn.setEnabled(true);
            exportToJsonBtn.setEnabled(true);

            isJsonShown = false;
        }
        else finish();

    }

    //Формирование и добавление записи
    @SuppressLint("DefaultLocale")
    public void addAppointment() {

        //Открыть соединения
        doctorsRepository.open();
        patientsRepository.open();

        //Получить списки первичных ключей
        List<Integer> doctorsIds = doctorsRepository.getIdsList();
        List<Integer> patientsIds = doctorsRepository.getIdsList();

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
        new Thread(() -> {

            Utils.appointmentsRepository.open();

            List<Appointment> appointments = Utils.appointmentsRepository.getAll();

            Utils.appointmentsRepository.close();

            //Запись целой сущности приёма вместе с пациентами и докторами
            boolean result = JsonHelper.writeToJson(this,
                    appointments, new AppointmentComplexConverter()/*new AppointmentConverter()*/,
                    Parameters.APPOINTMENTS_FILE_NAME);

            Utils.showSnackBar(currentView, String.format("Приёмы %s записаны в файл!", result ? "были" : "не были"));
        }).start();

    }

    //Загрузка колелкции из JSON
    public void importFromJson() {

        /*new Thread(() -> {
            List<Appointment> appointments = JsonHelper.readFromJson(this,
                    new AppointmentConverter(),
                    Appointment.class, Appointment[].class,
                    Parameters.APPOINTMENTS_FILE_NAME);

            if (appointments == null){
                Utils.showSnackBar(currentView, "Прочитать приёмы не удалось!");
                return;
            }

            addAppointmentBtn.post(() -> {
                    addAppointmentBtn.setEnabled(false);}
            );


            exportToJsonBtn.post(() -> {
                exportToJsonBtn.setEnabled(false);
            });

            //Задать связанные сущности
            doctorsRepository.open();
            patientsRepository.open();

            for (Appointment appointment : appointments) {
                appointment.setDoctor( doctorsRepository.getById(appointment.getDoctor_id()) );
                appointment.setPatient( patientsRepository.getById(appointment.getPatient_id()) );
            }

            doctorsRepository.close();
            patientsRepository.close();

            fragment.setCollection(appointments,Appointment.class);

            Utils.showSnackBar(currentView, "Приёмы были прочитаны из файла");
        }).start();*/

        //Чтение объекта вместе с ссылками на другие сущности
        new Thread(() -> {
            List<Appointment> appointments = JsonHelper.readFromJson(this,
                    new AppointmentComplexConverter(),
                    Appointment.class, Appointment[].class,
                    Parameters.APPOINTMENTS_FILE_NAME);

            if (appointments == null) {
                Utils.showSnackBar(currentView, "Прочитать приёмы не удалось!");
                return;
            }

            addAppointmentBtn.post(() -> {
                addAppointmentBtn.setEnabled(false);
            });


            exportToJsonBtn.post(() -> {
                exportToJsonBtn.setEnabled(false);
            });

            fragment.setCollection(appointments, Appointment.class);

            isJsonShown = true;
            Utils.showSnackBar(currentView, "Приёмы были прочитаны из файла");
        }).start();

    }


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