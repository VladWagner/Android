package com.step.wagner.fragments.pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.step.wagner.R;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.intefaces.Receiver;

public class TablePageFragment extends Fragment {

    //Номер страницы
    private int pageIndex;

    //Фрагмент для вывода таблиц
    private Receiver receiverFragment;

    public TablePageFragment() {
    }


    //Фабричный метод
    public static TablePageFragment newInstance(int pageIndex){
        TablePageFragment tablePageFragment = new TablePageFragment();

        //Передать индекс страницы
        Bundle params = new Bundle();
        params.putInt("index",pageIndex);

        tablePageFragment.setArguments(params);

        return tablePageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        pageIndex = bundle != null ? bundle.getInt("index") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tables_page, container, false);

        receiverFragment = (Receiver) getChildFragmentManager().findFragmentById(R.id.outputFragment);

        //Выборка таблицы по номеру страницы
        switch (pageIndex){
            case 1:
                receiverFragment.tables(Parameters.DOCTORS);
                break;
            case 2:
                receiverFragment.tables(Parameters.PATIENTS);
                break;
        }

        return view;
    }
}