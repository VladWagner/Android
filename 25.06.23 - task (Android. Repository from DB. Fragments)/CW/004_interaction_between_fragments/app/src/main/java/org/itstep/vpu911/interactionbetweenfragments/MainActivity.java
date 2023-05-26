package org.itstep.vpu911.interactionbetweenfragments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// интерфейс OnSendDataListener, разработанный для передачи данных в активность
// активность - посредник для обменов между фрагментами
public class MainActivity extends AppCompatActivity implements OnSendDataListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    } // onCreate

    // Ретрансляция данных из фрагмента во фрагмент через активность
    // реализация интерфейса ListFragment.OnFragmentSendDataListener  - прием данных
    // из фрагмента-источника, передача данных во фрагмент-приемник
    @Override
    public void onSendData(String data) {
        OnSendDataListener fragment = (OnSendDataListener) getSupportFragmentManager()
                .findFragmentById(R.id.detailFragment);
        if (fragment != null)
            fragment.onSendData(data);
    } // onSendData
}