package com.step.wagner.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.step.wagner.R;
import com.step.wagner.async_tasks.CommonTask;
import com.step.wagner.converters.AppointmentComplexConverter;
import com.step.wagner.converters.DoctorConverter;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.infrastructure.JsonHelper;
import com.step.wagner.intefaces.FragmentReceiver;
import com.step.wagner.models.entities.Appointment;
import com.step.wagner.models.entities.Doctor;

import java.util.List;

public class DoctorsActivity extends AppCompatActivity {

    Button exportToJsonBtn;
    FragmentReceiver fragment;

    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        exportToJsonBtn = findViewById(R.id.exportDoctors);
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
        fragment.tables(Parameters.DOCTORS);

    } // onResume

    //Выгрузка таблицы в JSON
    @SuppressLint("DefaultLocale")
    public void exportToJson(){

        CommonTask commonTask = new CommonTask();

        //Отключение кнопки для индикации загрузки
        exportToJsonBtn.setEnabled(false);

        //Запуск записи таблицы в JSON в отдельной задаче
        commonTask.execute(() -> {

                    //Прочитать таблицу
                    Utils.doctorsRepository.open();

                    List<Doctor> doctors = Utils.doctorsRepository.getAll();

                    Utils.doctorsRepository.close();

                    //Запись в JSON
                    boolean result = JsonHelper.writeToJson(this, doctors, new DoctorConverter(), Parameters.DOCTORS_FILE_NAME);

                    //Что выводить после завершения записи
                    return () -> {
                        exportToJsonBtn.setEnabled(true);
                        Utils.showSnackBar(currentView, String.format("Доктора %s записаны в файл!", result ? "были" : "не были"));

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