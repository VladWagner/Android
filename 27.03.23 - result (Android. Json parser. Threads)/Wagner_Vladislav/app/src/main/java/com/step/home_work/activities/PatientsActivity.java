package com.step.home_work.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.step.home_work.R;
import com.step.home_work.converters.PatientConverter;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.infrastructure.JsonHelper;
import com.step.home_work.intefaces.FragmentReceiver;
import com.step.home_work.models.entities.Patient;

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
        new Thread(() -> {

            Utils.patientsRepository.open();

            List<Patient> patients = Utils.patientsRepository.getAll();

            Utils.patientsRepository.close();

            //Запись в JSON
            boolean result = JsonHelper.writeToJson(this, patients, new PatientConverter(), Parameters.PATIENTS_FILE_NAME);

            Utils.showSnackBar(currentView, String.format("Пациенты %s записаны в файл!", result ? "были" : "не были"));
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