package org.itstep.vpu911.interactionbetweenfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

// фрагмент-приемник данных
public class DetailFragment extends Fragment implements OnSendDataListener {

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
    public void onSendData(String data) {
        txvDetails.setText(data);
    }

}