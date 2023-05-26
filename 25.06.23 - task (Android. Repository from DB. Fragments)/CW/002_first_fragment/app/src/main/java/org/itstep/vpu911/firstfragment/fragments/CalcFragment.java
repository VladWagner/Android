package org.itstep.vpu911.firstfragment.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.itstep.vpu911.firstfragment.R;

import java.util.Locale;

import static java.lang.Math.pow;

// фрагмент - демонстрация создания
public class CalcFragment extends Fragment {
    // ссылки на элементы интерфейса
    TextView txvResult;
    EditText edtNumber;
    Button btnCalc;

    // обязательный конструктор
    // в д.с. пустой - т.к. связь с разметкой в onCreateView
    public CalcFragment() {}

    // Объект LayoutInflater используется для установки ресурса разметки для создания интерфейса
    // Параметр ViewGroup container устанавливает контейнер интерфейса
    // Параметр Bundle savedInstanceState передает ранее сохраненное состояние
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calc, container, false);
        txvResult = view.findViewById(R.id.txvResult);
        edtNumber = view.findViewById(R.id.edtNumber);

        // вычисление корня по клику на кнопку
        view.findViewById(R.id.btnCalc).setOnClickListener(v -> {
            String str = edtNumber.getText().toString();
            if (str.isEmpty()) {
                str = "0";
                edtNumber.setText(str);
            }
            double value = Double.parseDouble(str);

            double result = pow(value, 1./3.);
            txvResult.setText(String.format(Locale.UK, "Корень. куб: %.5f", result));
        });
        return view;
    } // onCreateView
}