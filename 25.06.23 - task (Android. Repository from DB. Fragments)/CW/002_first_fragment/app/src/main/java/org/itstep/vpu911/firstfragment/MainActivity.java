package org.itstep.vpu911.firstfragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import org.itstep.vpu911.firstfragment.fragments.CalcFragment;
import org.itstep.vpu911.firstfragment.fragments.FirstFragment;
import org.itstep.vpu911.firstfragment.fragments.Vpu911Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // программное создание фрагмента - создание фрагмента в дополнение
        // к заданному в разметке
        if (savedInstanceState == null) { // признак первого запуска активности
            getSupportFragmentManager()
                .beginTransaction()    // начать транзакцию по созданию фрагментов
                // создание фрагмента FirstFragment в заданном разметкой контейнере
                .add(R.id.container, new FirstFragment())
                .add(R.id.container, new FirstFragment())
                .add(R.id.container, new CalcFragment())
                .add(R.id.container, new Vpu911Fragment())
                .commit();             // завершить транзакцию по созданию фрагментов
        } // if

        // выход из приложеия по клику на кнопке "Выход"
        findViewById(R.id.btnExit).setOnClickListener(v -> finish());
    } // onCreate
} // class MainActivity