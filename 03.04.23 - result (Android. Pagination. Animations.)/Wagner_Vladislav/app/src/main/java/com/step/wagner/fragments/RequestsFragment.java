package com.step.wagner.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.step.wagner.R;
import com.step.wagner.adapters.AlbumAdapter;
import com.step.wagner.adapters.AppointmentAdapter;
import com.step.wagner.adapters.BaseAdapter;
import com.step.wagner.adapters.DoctorAdapter;
import com.step.wagner.adapters.PatientAdapter;
import com.step.wagner.adapters.Query5Adapter;
import com.step.wagner.adapters.Query6Adapter;
import com.step.wagner.adapters.Query7Adapter;
import com.step.wagner.async_tasks.DataBaseTask;
import com.step.wagner.fragments.dialogues.Query1Dialog;
import com.step.wagner.fragments.dialogues.Query2Dialog;
import com.step.wagner.fragments.dialogues.Query3Dialog;
import com.step.wagner.fragments.dialogues.Query4Dialog;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.SimpleTuple;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.Receiver;
import com.step.wagner.models.Album;
import com.step.wagner.models.entities.Appointment;
import com.step.wagner.models.entities.Doctor;
import com.step.wagner.models.entities.Patient;
import com.step.wagner.models.queries.Query5;
import com.step.wagner.models.queries.Query6;
import com.step.wagner.models.queries.Query7;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class RequestsFragment extends Fragment {

    RecyclerView recyclerView;

    TextView titleTxv;

    Context fragmentContext;


    public RequestsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Загрузить разметку
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Получить ссылку на recyclerView
        recyclerView = view.findViewById(R.id.rcvResult);

        //Получить ссылку на textView
        titleTxv = view.findViewById(R.id.fragmentTitle);
        fragmentContext = getContext();


    } // onViewCreated

    //Установка коллекции
    public <T> void setCollection(List<T> results, int requestNumber) {

        switch (requestNumber){
            case 1:
                titleTxv.setText("Коллекция альбомов");
                recyclerView.setAdapter(new AlbumAdapter(fragmentContext,(List<Album>) results));
                break;
            case 2:
                titleTxv.setText("Информация об конкретном альбоме");
                recyclerView.setAdapter(new AlbumAdapter(fragmentContext,(List<Album>) results));
                break;
        }
    }

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

    public TextView getTextView(){
        return titleTxv;
    }

    //Установить адаптер в recycler view
    public <T> void setRecyclerViewAdapter(BaseAdapter<T> adapter) {
        if (adapter == null) return;

        recyclerView.setAdapter(adapter);
    }

    public void setTxvValue(String value) {
        if (value.isBlank())
            return;

        titleTxv.setText(value);
    }
}