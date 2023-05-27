package com.step.wagner.sensors.fragments;


import static com.step.wagner.sensors.infrastructure.Utils.statisticsRepository;

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

import com.step.wagner.sensors.R;
import com.step.wagner.sensors.adapters.StatisticsAdapter;
import com.step.wagner.sensors.async_tasks.CommonTask;
import com.step.wagner.sensors.models.entities.Statistics;

import java.util.List;

//Статистика обработок
public class ProcessingHistoryFragment extends Fragment {

    RecyclerView recyclerView;

    TextView titleTxv;

    Context fragmentContext;

    //Кнопка очистки истории
    private Button btnCleanHistory;


    public ProcessingHistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_processing_history, container, false);

        btnCleanHistory = view.findViewById(R.id.btnCleanHistory);

        //Получить ссылку на recyclerView
        recyclerView = view.findViewById(R.id.rcvResult);

        //Получить ссылку на textView
        titleTxv = view.findViewById(R.id.fragmentTitle);

        // Загрузить разметку
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentContext = getContext();

        btnCleanHistory.setOnClickListener(v -> onCleanHistoryClickListener());

        CommonTask task = new CommonTask();

        task.execute(() -> {

            //Получить коллекцию в асинхронной части
            statisticsRepository.open();

            List<Statistics> statisticsList = statisticsRepository.getAll();

            statisticsRepository.close();

            //Задать коллекцию в адаптер в onPostExecuted части
            return () -> {

                if (statisticsList.size() == 0)
                    titleTxv.setVisibility(View.VISIBLE);
                else {

                    //Сортировка по убыванию id
                    statisticsList.sort((v1,v2) -> v2.getId() - v1.getId());

                    recyclerView.setAdapter(new StatisticsAdapter(fragmentContext, statisticsList, getChildFragmentManager()));
                }

                return null;
            };


        });

    } // onViewCreated


    private void onCleanHistoryClickListener(){


        statisticsRepository.open();

        //Очистить таблицу
        statisticsRepository.cleanAll();

        //Прочитать значения
        recyclerView.setAdapter(new StatisticsAdapter(fragmentContext, statisticsRepository.getAll(),getChildFragmentManager()));

        statisticsRepository.close();
    }


}