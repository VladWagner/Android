package com.step.home_work.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.step.home_work.R;
import com.step.home_work.intefaces.ActivitySender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class QueriesListFragment extends Fragment {

    ActivitySender activity;

    ListView queriesLsv;

    List<String> queries;

    @SuppressLint("DefaultLocale")
    public QueriesListFragment() {

        //Заполнить список
        queries = new ArrayList<>();
        for (int i = 1; i <= 7; i++)
            queries.add(String.format("Запрос %d", i));
    }

    //Загрузка фрагмента в активность
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            activity = (ActivitySender) context;

        } catch (Exception e) {
            throw new ClassCastException(String.format("%s должен реализовывать интерфейс ActivitySender", context.toString()));
        } // try-catch
    } // onAttach

    //Загрузка разметки
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_queries_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        queriesLsv = view.findViewById(R.id.queriesLsv);

        //Простой адаптер для отображения списка
        ArrayAdapter<String> simpleAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                queries
        );

        queriesLsv.setAdapter(simpleAdapter);

        //Задать обработчик
        queriesLsv.setOnItemClickListener((adapter, itemView, index,id) -> {
            activity.sendQueryNumber(index+1);
        });

    } // onViewCreated

}