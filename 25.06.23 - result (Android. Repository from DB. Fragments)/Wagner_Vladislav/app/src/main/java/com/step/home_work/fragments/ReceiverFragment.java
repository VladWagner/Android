package com.step.home_work.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.step.home_work.R;
import com.step.home_work.adapters.AppointmentAdapter;
import com.step.home_work.adapters.DoctorAdapter;
import com.step.home_work.adapters.PatientAdapter;
import com.step.home_work.adapters.Query5Adapter;
import com.step.home_work.adapters.Query6Adapter;
import com.step.home_work.adapters.Query7Adapter;
import com.step.home_work.intefaces.FragmentReceiver;
import com.step.home_work.models.entities.Appointment;
import com.step.home_work.models.entities.Doctor;
import com.step.home_work.models.entities.Patient;
import com.step.home_work.models.queries.Query5;
import com.step.home_work.models.queries.Query6;
import com.step.home_work.models.queries.Query7;

import java.util.List;


public class ReceiverFragment extends Fragment implements FragmentReceiver {

    //Recycler view для вывода результата запроса
    RecyclerView recyclerView;

    TextView titleTxv;

    public ReceiverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Загрузить разметку
        return inflater.inflate(R.layout.fragment_receiver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Получить ссылку на recyclerView
        recyclerView = view.findViewById(R.id.rcvResult);

        //Получить ссылку на textView
        titleTxv = view.findViewById(R.id.receiverTitle);

    } // onViewCreated


    //region Запросы
    @Override
    public void query1(List<Patient> patients, String title) {
        titleTxv.setText(title);

        //Задать коллекцию в адаптер
        PatientAdapter patientAdapter = new PatientAdapter(getContext(),patients);
        recyclerView.setAdapter(patientAdapter);
    }

    @Override
    public void query2(List<Doctor> doctors, String title) {
        titleTxv.setText(title);

        //Задать коллекцию в адаптер
        DoctorAdapter doctorAdapter = new DoctorAdapter(getContext(),doctors);
        recyclerView.setAdapter(doctorAdapter);
    }

    @Override
    public void query3(List<Appointment> appointments, String title) {
        titleTxv.setText(title);

        //Задать коллекцию в адаптер
        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(getContext(),appointments);
        recyclerView.setAdapter(appointmentAdapter);
    }

    @Override
    public void query4(List<Doctor> doctors, String title) {
        titleTxv.setText(title);

        //Задать коллекцию в адаптер
        DoctorAdapter doctorAdapter = new DoctorAdapter(getContext(),doctors);
        recyclerView.setAdapter(doctorAdapter);
    }

    @Override
    public void query5(List<Query5> query5, String title) {
        titleTxv.setText(title);

        //Задать коллекцию в адаптер
        Query5Adapter query5Adapter = new Query5Adapter(getContext(),query5);
        recyclerView.setAdapter(query5Adapter);

    }

    @Override
    public void query6(List<Query6> query6, String title) {
        titleTxv.setText(title);

        //Задать коллекцию в адаптер
        Query6Adapter query6Adapter = new Query6Adapter(getContext(),query6);
        recyclerView.setAdapter(query6Adapter);

    }

    @Override
    public void query7(List<Query7> query7, String title) {
        titleTxv.setText(title);

        //Задать коллекцию в адаптер
        Query7Adapter query7Adapter = new Query7Adapter(getContext(),query7);
        recyclerView.setAdapter(query7Adapter);
    }
    //endregion
}