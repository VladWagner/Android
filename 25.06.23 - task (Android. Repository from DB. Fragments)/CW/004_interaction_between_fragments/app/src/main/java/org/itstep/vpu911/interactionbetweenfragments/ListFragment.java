package org.itstep.vpu911.interactionbetweenfragments;

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


// фрагмент - источник данных
public class ListFragment extends Fragment {

    // ссылка на активность, в которой находится фрагмент
    private OnSendDataListener activityRetranslator;

    String[] countries = {
            "Бразилия", "Аргентина", "Колумбия", "Чили",
            "Венесуэла", "Уругвай", "Парагвай", "Колумбия"
    };

    // обязательный конструктор
    public ListFragment() {}

    // при подключении к активности, context -  ссылка на активнсоть
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            activityRetranslator = (OnSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        } // try-catch
    } // onAttach

    // получение разметки фрагмента
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    } // onCreateView

    // связь с элементами разметки
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // получаем элемент ListView
        ListView countriesList = view.findViewById(R.id.countriesList);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                countries);
        // устанавливаем для списка адаптер
        countriesList.setAdapter(adapter);

        // добавляем для списка слушатель
        countriesList.setOnItemClickListener((parent, v, position, id) -> {
            // получаем выбранный элемент
            String selectedItem = (String)parent.getItemAtPosition(position);
            // Посылаем данные Activity для ретрансляции в DatailFragment
            activityRetranslator.onSendData(selectedItem);
        });
    } // onViewCreated
} // class ListFragment