package com.step.home_work.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.step.home_work.R;
import com.step.home_work.intefaces.FragmentReceiver;
import com.step.home_work.models.entities.Appointment;
import com.step.home_work.models.entities.Doctor;
import com.step.home_work.models.entities.Patient;
import com.step.home_work.models.queries.Query5;
import com.step.home_work.models.queries.Query6;
import com.step.home_work.models.queries.Query7;

import java.util.List;

public class QueryResultActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);

        //Если ориентация горизонтальная, тогда выходим из активность
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            finish();
            return;
        }

        findViewById(R.id.btnExit).setOnClickListener(v -> finish());

        intent = getIntent();
    }//OnCreate

    //Получить результат запроса и задать результат во фрагмент
    @Override
    public void onResume() {
        super.onResume();

        FragmentReceiver fragment = (FragmentReceiver) getSupportFragmentManager().findFragmentById(R.id.receiverFragment);

        if (fragment == null)
            return;

        //Получить номер запрсоа
        int queryNum = intent.getIntExtra("query_number",1);
        String title = intent.getStringExtra("title");

        switch (queryNum){
            case 1:
                //Получить коллекцию
                List<Patient> query1Result = intent.getParcelableArrayListExtra(Patient.class.getCanonicalName());
                fragment.query1(query1Result,title);
                break;
            case 2:
                //Получить коллекцию
                List<Doctor> query2Result = intent.getParcelableArrayListExtra(Doctor.class.getCanonicalName());
                fragment.query2(query2Result, title);
                break;
            case 3:
                //Получить коллекцию
                List<Appointment> query3Result = intent.getParcelableArrayListExtra(Appointment.class.getCanonicalName());
                fragment.query3(query3Result,title);
                break;
            case 4:
                //Получить коллекцию
                List<Doctor> query4Result = intent.getParcelableArrayListExtra(Doctor.class.getCanonicalName());
                fragment.query4(query4Result,title);
                break;
            case 5:
                //Получить коллекцию
                List<Query5> query5Result = intent.getParcelableArrayListExtra(Query5.class.getCanonicalName());
                fragment.query5(query5Result,title);
                break;
            case 6:
                //Получить коллекцию
                List<Query6> query6Result = intent.getParcelableArrayListExtra(Query6.class.getCanonicalName());
                fragment.query6(query6Result,title);
                break;
            case 7:
                //Получить коллекцию
                List<Query7> query7Result = intent.getParcelableArrayListExtra(Query7.class.getCanonicalName());
                fragment.query7(query7Result,title);
                break;
        }//switch

    } // onResume

}