package com.step.home_work;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.step.home_work.activities.QueryResultActivity;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.intefaces.ActivitySender;
import com.step.home_work.intefaces.FragmentReceiver;
import com.step.home_work.models.entities.Appointment;
import com.step.home_work.models.entities.Doctor;
import com.step.home_work.models.entities.Patient;
import com.step.home_work.models.queries.Query5;
import com.step.home_work.models.queries.Query6;
import com.step.home_work.models.queries.Query7;
import com.step.home_work.repositories.AppointmentsRepository;
import com.step.home_work.repositories.DoctorsRepository;
import com.step.home_work.repositories.PatientsRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ActivitySender {

    //Репозиторий врачебных приёмов
    private AppointmentsRepository appointmentsRepository;

    //Репозиторий врачей
    private DoctorsRepository doctorsRepository;

    //Репозиторий пациентов
    private PatientsRepository patientsRepository;

    Button exitBtn;

    //Текущее представление
    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentView = this.findViewById(android.R.id.content);

        appointmentsRepository = new AppointmentsRepository(this);
        doctorsRepository = new DoctorsRepository(this);
        patientsRepository = new PatientsRepository(this);

        Button btnExit = findViewById(R.id.btnExit);
        if (btnExit != null)
            btnExit.setOnClickListener((v) -> finish());


    }

    //Открыть подключения к БД
    @Override
    public void onResume() {
        super.onResume();

        //appointmentsRepository.open();
        //doctorsRepository.open();
        //patientsRepository.open();

    } // onResume

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    //endregion


    //Выполнение запроса и передача результата во фрагмент/активность

    @SuppressLint("DefaultLocale")
    @Override
    public void sendQueryResult(int queryNumber) {
        String title;

        FragmentReceiver fragmentReceiver = null;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            fragmentReceiver = (FragmentReceiver) getSupportFragmentManager().findFragmentById(R.id.receiverFragment);

        Intent intent = new Intent(this, QueryResultActivity.class);
        intent.putExtra("query_number", queryNumber);

        switch (queryNumber) {
            case 1:
                patientsRepository.open();

                List<Patient> query1Result = patientsRepository.query1("Т%");

                patientsRepository.close();
                title = "Запрос 1: выборка пациентов по фамилии";

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    intent.putExtra("title", title);
                    intent.putParcelableArrayListExtra(Patient.class.getCanonicalName(), (ArrayList<? extends Parcelable>) query1Result);

                    startActivity(intent);
                }
                else if (fragmentReceiver != null) fragmentReceiver.query1(query1Result,title);
            break;

            //Запрос 2
            case 2:
                double percent = 2.3;
                doctorsRepository.open();

                List<Doctor> query2Result = doctorsRepository.query2(percent);

                doctorsRepository.close();

                title = String.format("Запрос 2: доктора с процентом отчислений > %.2f",percent);

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    intent.putExtra("title", title);
                    intent.putParcelableArrayListExtra(Doctor.class.getCanonicalName(), (ArrayList<? extends Parcelable>) query2Result);

                    startActivity(intent);
                }
                else if  (fragmentReceiver != null) fragmentReceiver.query2(query2Result,title);
            break;

            //Запрос 3
            case 3:

                String dateMin = "01.10.2021";
                String dateMax = "01.01.2022";

                //Получить параметры даты
                Date dateStart;
                Date dateEnd ;
                try {
                    dateStart = Utils.dateFormat.parse(dateMin);
                    dateEnd = Utils.dateFormat.parse(dateMax);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                appointmentsRepository.open();

                List<Appointment> query3Result = appointmentsRepository.query3(dateStart,dateEnd);

                appointmentsRepository.close();

                //Задать заголовок
                title = String.format("Запрос 3: приёмы в диапазоне дат %s → %s",dateMin, dateMax);

                //Если ориентация горизонтальная, то передать
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    intent.putExtra("title", title);

                    intent.putParcelableArrayListExtra(Appointment.class.getCanonicalName(), (ArrayList<? extends Parcelable>) query3Result);

                    startActivity(intent);
                }
                else if  (fragmentReceiver != null) fragmentReceiver.query3(query3Result,title);
            break;

            //Запрос 4
            case 4:
                String speciality = "терапевт";
                doctorsRepository.open();

                List<Doctor> query4Result = doctorsRepository.query4(speciality);

                doctorsRepository.close();

                //Задать заголовок
                title = String.format("Запрос 4: доктора со специальностью %s",speciality);

                //Если ориентация горизонтальная, то передать
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    intent.putExtra("title", title);

                    intent.putParcelableArrayListExtra(Doctor.class.getCanonicalName(), (ArrayList<? extends Parcelable>) query4Result);

                    startActivity(intent);
                }
                else if  (fragmentReceiver != null) fragmentReceiver.query4(query4Result,title);
            break;

            //Запрос 5
            case 5:
                doctorsRepository.open();

                List<Query5> query5Result = doctorsRepository.query5();

                doctorsRepository.close();

                //Задать заголовок
                title = "Запрос 5: зарплата варчей";

                //Если ориентация вертикальная, то передать результат запроса в активность
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    intent.putExtra("title", title);

                    intent.putParcelableArrayListExtra(Query5.class.getCanonicalName(), (ArrayList<? extends Parcelable>) query5Result);

                    startActivity(intent);
                }
                else if  (fragmentReceiver != null) fragmentReceiver.query5(query5Result,title);
            break;

            //Запрос 6
            case 6:
                appointmentsRepository.open();

                List<Query6> query6Result = appointmentsRepository.query6();

                appointmentsRepository.close();

                //Задать заголовок
                title = "Запрос 6: группировку по полю дата приема";

                //Если ориентация вертикальная, то передать результат запроса в активность
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    intent.putExtra("title", title);

                    intent.putParcelableArrayListExtra(Query6.class.getCanonicalName(), (ArrayList<? extends Parcelable>) query6Result);

                    startActivity(intent);
                }
                else if  (fragmentReceiver != null) fragmentReceiver.query6(query6Result,title);
            break;

            //Запрос 7
            case 7:
                doctorsRepository.open();

                List<Query7> query7Result = doctorsRepository.query7();

                doctorsRepository.close();

                //Задать заголовок
                title = "Запрос 7: группировку по полю специальность";

                //Если ориентация вертикальная, то передать результат запроса в активность
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    intent.putExtra("title", title);

                    intent.putParcelableArrayListExtra(Query7.class.getCanonicalName(), (ArrayList<? extends Parcelable>) query7Result);

                    startActivity(intent);
                }
                else if  (fragmentReceiver != null) fragmentReceiver.query7(query7Result,title);
                break;
        }//switch
    }

}