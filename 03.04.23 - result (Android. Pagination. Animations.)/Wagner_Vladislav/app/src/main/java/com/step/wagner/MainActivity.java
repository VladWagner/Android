package com.step.wagner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.step.wagner.activities.AboutActivity;
import com.step.wagner.activities.WebRequestsActivity;
import com.step.wagner.activities.AllTablesActivity;
import com.step.wagner.activities.AppointmentsActivity;
import com.step.wagner.activities.DoctorsActivity;
import com.step.wagner.activities.PatientsActivity;
import com.step.wagner.activities.QueryResultActivity;
import com.step.wagner.async_tasks.CommonTask;
import com.step.wagner.converters.AppointmentComplexConverter;
import com.step.wagner.converters.DoctorConverter;
import com.step.wagner.converters.PatientConverter;
import com.step.wagner.infrastructure.JsonHelper;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.Sender;
import com.step.wagner.intefaces.Receiver;
import com.step.wagner.models.entities.Appointment;
import com.step.wagner.models.entities.Doctor;
import com.step.wagner.models.entities.Patient;
import com.step.wagner.repositories.AppointmentsRepository;
import com.step.wagner.repositories.DoctorsRepository;
import com.step.wagner.repositories.PatientsRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Sender {

    //Текущее представление
    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentView = this.findViewById(android.R.id.content);

        //Инициализация репозиториев при попадании в активность
        if (Utils.appointmentsRepository == null || Utils.doctorsRepository == null || Utils.patientsRepository == null ) {
            Utils.appointmentsRepository = new AppointmentsRepository(this);
            Utils.doctorsRepository = new DoctorsRepository(this);
            Utils.patientsRepository = new PatientsRepository(this);
        }
    }

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.appointmentsBtn:

                startActivity(new Intent(this, AppointmentsActivity.class));

                break;
            case R.id.doctorsBtn:

                startActivity(new Intent(this, DoctorsActivity.class));

                break;
            case R.id.patientsBtn:

                startActivity(new Intent(this, PatientsActivity.class));

                break;
            case R.id.requestToWebServerBtn:

                startActivity(new Intent(this, WebRequestsActivity.class));

                break;
            case R.id.exportToJsonBtn:
                exportToJson();
                break;

            case R.id.aboutBtn:

                startActivity(new Intent(this, AboutActivity.class));

                break;
            case R.id.menuBtnExit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    //Выполнение запроса и вызов фрагмента/активности
    @SuppressLint("DefaultLocale")
    @Override
    public void sendQueryNumber(int queryNumber) {
        Receiver receiver = null;

        //Создаём объект фрагмента-получателя, если ориентация горизонтальная,
        //поскольку дальше фрагмент будет использоваться в этой активности
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            receiver = (Receiver) getSupportFragmentManager().findFragmentById(R.id.receiverFragment);

        Intent intent = new Intent(this, QueryResultActivity.class);
        intent.putExtra("query_number", queryNumber);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            startActivity(intent);
        else if (receiver != null) receiver.queries(queryNumber);

    }

    @Override
    public void sendTableNumber(int tableNumber) {
        switch (tableNumber){
            case 8:
                startActivity(new Intent(this, AppointmentsActivity.class));
                break;
            case 9:
                startActivity(new Intent(this, DoctorsActivity.class));
                break;
            case 10:
                startActivity(new Intent(this, PatientsActivity.class));
                break;

            //Постраничный вывод всех таблиц
            case 11:
                startActivity(new Intent(this, AllTablesActivity.class));
                break;
        }
    }

    //Выгрузить табилцы в JSON
    private void exportToJson(){
        CommonTask commonTask = new CommonTask();


        //Запуск записи таблиц в JSON в отдельной задаче
        commonTask.execute(() -> {

            //Открыть соединения
            Utils.appointmentsRepository.open();
            Utils.doctorsRepository.open();
            Utils.patientsRepository.open();

            List<Appointment> appointments = Utils.appointmentsRepository.getAll();

            //Запись целой сущности приёма вместе с пациентами и докторами
            boolean resultAppointments = JsonHelper.writeToJson(this,
                    appointments, new AppointmentComplexConverter()/*new AppointmentConverter()*/,
                    Parameters.APPOINTMENTS_FILE_NAME);

            //Запись докторов
            List<Doctor> doctors = Utils.doctorsRepository.getAll();

            boolean resultDoctors = JsonHelper.writeToJson(this, doctors, new DoctorConverter(), Parameters.DOCTORS_FILE_NAME);

            //Запись пациентов
            List<Patient> patients = Utils.patientsRepository.getAll();

            boolean resultPatients = JsonHelper.writeToJson(this, patients, new PatientConverter(), Parameters.PATIENTS_FILE_NAME);

            //Закрыть соединения
            Utils.appointmentsRepository.close();
            Utils.doctorsRepository.close();
            Utils.patientsRepository.close();

            //Что выводить после завершения операции
            return () -> {
                Utils.showSnackBar(currentView, String.format("%s были записаны в файлы!", resultAppointments && resultDoctors && resultPatients ? "Все таблицы" : "Не все таблицы"));

                return null;
            };
        });//execute
    }

}