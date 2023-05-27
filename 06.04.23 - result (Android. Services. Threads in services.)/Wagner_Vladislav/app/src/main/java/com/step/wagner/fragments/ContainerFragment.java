package com.step.wagner.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.step.wagner.R;
import com.step.wagner.adapters.AlbumAdapter;
import com.step.wagner.adapters.BaseAdapter;
import com.step.wagner.models.Album;

import java.util.List;


public class ContainerFragment extends Fragment {

    RecyclerView recyclerView;

    TextView titleTxv;

    Context fragmentContext;


    public ContainerFragment() {
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