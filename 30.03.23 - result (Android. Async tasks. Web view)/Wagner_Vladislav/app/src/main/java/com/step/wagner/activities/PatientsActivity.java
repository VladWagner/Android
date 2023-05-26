package com.step.wagner.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.step.wagner.R;
import com.step.wagner.async_tasks.CommonTask;
import com.step.wagner.converters.DoctorConverter;
import com.step.wagner.converters.PatientConverter;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.infrastructure.JsonHelper;
import com.step.wagner.intefaces.FragmentReceiver;
import com.step.wagner.models.entities.Doctor;
import com.step.wagner.models.entities.Patient;

import java.util.List;

public class PatientsActivity extends AppCompatActivity {

    Button exportToJsonBtn;
    FragmentReceiver fragment;

    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients);

        exportToJsonBtn = findViewById(R.id.exportPatients);
        exportToJsonBtn.setOnClickListener(v -> exportToJson());

        currentView = this.findViewById(android.R.id.content);
    }

    //Получить результат запроса и задать результат во фрагмент
    @Override
    public void onResume() {
        super.onResume();

        fragment = (FragmentReceiver) getSupportFragmentManager().findFragmentById(R.id.receiverFragment);

        if (fragment == null)
            return;

        //Вывод таблиц во фрагменте
        fragment.tables(Parameters.PATIENTS);

    } // onResume

    //Выгрузка таблицы в JSON
    public void exportToJson(){

        CommonTask commonTask = new CommonTask();

        //Отключение кнопки для индикации загрузки
        exportToJsonBtn.setEnabled(false);

        //Запуск записи таблицы в JSON в отдельной задаче
        commonTask.execute(() -> {

            //Прочитать таблицу
            Utils.patientsRepository.open();

            List<Patient> patients = Utils.patientsRepository.getAll();

            Utils.patientsRepository.close();

            //Запись в JSON
            boolean result = JsonHelper.writeToJson(this, patients, new PatientConverter(), Parameters.PATIENTS_FILE_NAME);

            //Что выводить после завершения записи
            return () -> {
                exportToJsonBtn.setEnabled(true);
                Utils.showSnackBar(currentView, String.format("Пациенты %s записаны в файл!", result ? "были" : "не были"));

                return null;
            };
        });//execute

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