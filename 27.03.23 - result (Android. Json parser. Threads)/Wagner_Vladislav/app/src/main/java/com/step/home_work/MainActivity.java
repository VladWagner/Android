package com.step.home_work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.step.home_work.activities.AppointmentsActivity;
import com.step.home_work.activities.DoctorsActivity;
import com.step.home_work.activities.PatientsActivity;
import com.step.home_work.activities.QueryResultActivity;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.intefaces.ActivitySender;
import com.step.home_work.intefaces.FragmentReceiver;
import com.step.home_work.repositories.AppointmentsRepository;
import com.step.home_work.repositories.DoctorsRepository;
import com.step.home_work.repositories.PatientsRepository;

public class MainActivity extends AppCompatActivity implements ActivitySender {

    Button exitBtn;

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

        exitBtn = findViewById(R.id.btnExit);
        if (exitBtn != null)
            exitBtn.setOnClickListener((v) -> finish());


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
        FragmentReceiver fragmentReceiver = null;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            fragmentReceiver = (FragmentReceiver) getSupportFragmentManager().findFragmentById(R.id.receiverFragment);

        Intent intent = new Intent(this, QueryResultActivity.class);
        intent.putExtra("query_number", queryNumber);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            startActivity(intent);
        else if (fragmentReceiver != null) fragmentReceiver.queries(queryNumber);

    }

}