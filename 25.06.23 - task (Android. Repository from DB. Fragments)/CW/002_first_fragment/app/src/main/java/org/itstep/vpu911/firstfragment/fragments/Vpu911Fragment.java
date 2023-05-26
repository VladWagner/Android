package org.itstep.vpu911.firstfragment.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.TextView;

import org.itstep.vpu911.firstfragment.R;



public class Vpu911Fragment extends Fragment {

    // обязательный элемент
    public Vpu911Fragment() {
       super(R.layout.fragment_vpu911);
    }

    // переопределение обработчика события создания визуального представления
    // фрагмента
    // Объект LayoutInflater используется для установки ресурса разметки для создания интерфейса
    // Параметр ViewGroup container устанавливает контейнер интерфейса
    // Параметр Bundle savedInstanceState передает ранее сохраненное состояние
    @Override  // создание представления фрагмента
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txvVpu911 = view.findViewById(R.id.txvVpu911);
        view.findViewById(R.id.btnHello).setOnClickListener(v -> txvVpu911.setText("Привет, ВПУ911"));
        view.findViewById(R.id.btnBye).setOnClickListener(v -> txvVpu911.setText("Пока!"));
    } // onViewCreated
}