package org.itstep.vpu911.fragmentlifecycle;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;


// фрагмент для изучения жизненного цикла
public class ContentFragment extends Fragment {

    private final static String TAG = "ContentFragment";

    // элементы интерфейса фрагмента
    TextView txvFragment1Info;
    Button btnFragment1Action;

    // обязательный конструктор
    public ContentFragment(){
        Log.d(TAG, "Constructor");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    // переопределение обработчика события создания визуального представления
    // фрагмента
    // Объект LayoutInflater используется для установки ресурса разметки для создания интерфейса
    // Параметр ViewGroup container устанавливает контейнер интерфейса
    // Параметр Bundle savedInstanceState передает ранее сохраненное состояние
    @Override  // создание представления фрагмента
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");

        txvFragment1Info = view.findViewById(R.id.txvFragment1Info);
        btnFragment1Action = view.findViewById(R.id.btnFragment1Action);

        // onClick
        btnFragment1Action.setOnClickListener(v -> {
            // формат даты
            // http://proglang.su/java/date-and-time
            String curDate =  String.format(Locale.UK, "Сегодня: %1$td/%1$tm/%1$tY", new Date());
            txvFragment1Info.setText(curDate);
        });

    } // onViewCreated


    // при первом запуске savedInstanceState == null
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored");
    }

    // для подключения к активности, context - ссылка на активность
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }
}