package org.itstep.vpu911.interactionfragmentsportland;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


// фрагмент вывода детальной информации, приемник данных из другой активности
public class DetailFragment extends Fragment {

    // элемент вывода принятых данных
    private TextView txvDetails;

    // обязательный конструктор
    public DetailFragment() {}

    // связь с разметкой
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        txvDetails = view.findViewById(R.id.txvDetails);

        return view;
    } // onCreateView

    // обновление текстового поля значением, полученным из активности MainActivity
    // (данные переданные из фрагмента ListFragment)
    public void setSelectedItem(String data) {
        txvDetails.setText(data);
    }
} // class DetailFragment