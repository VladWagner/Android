package org.itstep.vpu911.interactionfragmentsportland;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// активность, выводящая список значений
public class ListFragment extends Fragment {

    // Фрагменты не могут напрямую взаимодействовать между собой.
    // Для этого надо обращаться к контексту, в качестве которого
    // выступает класс Activity. Для обращения к activity, как правило,
    // создается вложенный интерфейс
    interface OnFragmentSendDataListener {
        void onSendData(String data);
    } // OnFragmentSendDataListener

    // ссылка на активность, в которой находится фрагмент
    private OnFragmentSendDataListener activivtyRetranslator;
    String[] countries = {
            "Бразилия", "Аргентина", "Колумбия",
            "Чили", "Уругвай", "Парагвай"
    };

    // обязательный конструктор
    public ListFragment() {}

    // при подключении к активности, context -  ссылка на активнсоть
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            activivtyRetranslator = (OnFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        } // try-catch
    } // onAttach

    // создание разметки фрагмента
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

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
            activivtyRetranslator.onSendData(selectedItem);
        });
        return view;
    } // onCreateView
} // class ListFragment