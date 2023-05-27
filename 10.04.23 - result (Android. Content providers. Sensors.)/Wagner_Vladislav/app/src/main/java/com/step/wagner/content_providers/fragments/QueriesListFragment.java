package com.step.wagner.content_providers.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.step.wagner.content_providers.R;
import com.step.wagner.content_providers.interfaces.*;

import java.util.ArrayList;
import java.util.List;


public class QueriesListFragment extends Fragment {

    Sender activity;

    ListView queriesLsv;

    List<String> queries;

    //Контекст активности
    Context activityContext;

    @SuppressLint("DefaultLocale")
    public QueriesListFragment() {

        //Заполнить список
        queries = new ArrayList<>();
        /*for (int i = 1; i <= 2; i++)
            queries.add(String.format("Запрос %d", i));*/

        queries.add("Запрос 2");
        queries.add("Таблица изданий");
        queries.add("Таблица подписок");
    }

    //Загрузка фрагмента в активность
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            activity = (Sender) context;
            activityContext = context;

        } catch (Exception e) {
            throw new ClassCastException(String.format("%s должен реализовывать интерфейс Sender", context.toString()));
        } // try-catch
    } // onAttach

    //Загрузка разметки
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Если вертикальная ориентация, тогда добавить пункты
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

        }

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