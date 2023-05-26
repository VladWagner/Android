package org.itstep.pd011.threadsandroiddemo.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.itstep.pd011.threadsandroiddemo.R;
import org.itstep.pd011.threadsandroiddemo.utils.Utils;


/*
 *  Оптимальнее потокам работать с фрагментами, чем напрямую с активностями
 *  */
public class DemoThreadFragment extends Fragment {
    private TextView txvOutput;
    private Button btnThread;         // кнопка запуска "правильного" потока
    private Button btnDelayedThread;  // кнопка запуска "правильного" потока с задержкой

    // обязательный конструктор
    public DemoThreadFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    } // onCreate

    // onCreateView - для связи разметки и кода, назначения обработчиков событий
    // на элементы интерфейса
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // получить ссылку на разметку фрагмента
        View view = inflater.inflate(R.layout.fragment_demo_thread, container, false);

        // получить ссылки на элементы разметки
        txvOutput = view.findViewById(R.id.txvFragmentOutput);
        btnThread = view.findViewById(R.id.btnFragmentThread);
        btnDelayedThread = view.findViewById(R.id.btnFragmentDelayedThread);

        // начальный вывод текущего времени - чтобы скрыть плейсхолдер
        txvOutput.setText(Utils.currentTimeToString());

        // назначение обработчиков клика на кнопки интерфейса
        btnThread.setOnClickListener(v -> new Thread(runnable1).start());
        btnDelayedThread.setOnClickListener(v -> new Thread(runnable2).start());

        // вернуть сформированный объект
        return view;
    } // onCreateView


    // Демонстрация работы с потоком без ошибки, с правильным взаимодействием
    // с элементами интерфейса - при помощи метода post() элемента графического
    // интерфейса
    private Runnable runnable1 = () -> {
        // запретить кнопку запуска потока - с использованием метода post()
        btnThread.post(() -> {
            btnThread.setEnabled(false);                // запрет кнопки
            btnThread.setText("Поток выполняется...");  // смена текста на кнопке
        });

        // для удобства восприятия
        Utils.sleep(5_000);

        // получаем текущее время в строковом формате
        String strTime = Utils.currentTimeToString();

        // !! для TextView не обязательно
        // отображаем в текстовом поле с использованием метода post() для взаимодействия
        // во вторичном потоке с элементами графического интерфейса - :
        txvOutput.post(() -> txvOutput.setText(strTime));
        // txvOutput.setText(strTime);   // так тоже работает, к сожалению...

        // разрешить кнопку запуска потока - с использованием метода post()
        btnThread.post(() -> {
            btnThread.setEnabled(true);
            btnThread.setText(R.string.btn_correct);  // восстановление текста из ресурсов
        });
    };

    // Демонстрация работы с потоком без ошибки, с правильным взаимодействием
    // с элементами интерфейса - при помощи метода postDelayed() элемента графического
    // интерфейса, запуск обмена с задержкой
    private Runnable runnable2 = () -> {
        // для удобства работы выведем текущее время в начале работы потока, тогда время
        // после задержки воспринимается легче, виден интервал срабатывания
        txvOutput.post(() -> {
            txvOutput.setText(Utils.currentTimeToString());
            txvOutput.setTextColor(Color.RED);
        });

        // запретить кнопку на время работы потока
        btnDelayedThread.post(() -> btnDelayedThread.setEnabled(false));

        // отображаем в текстовом поле с использованием метода postDelayed() для взаимодействия
        // во вторичном потоке с элементами графического интерфейса - с задержкой
        // запуска в 3000 мс
        txvOutput.postDelayed(() -> {
            // получаем текущее время в строковом формате
            String strTime = Utils.currentTimeToString();

            txvOutput.setText(strTime + " и это тоже");
            txvOutput.setTextColor(Color.BLUE);
            btnDelayedThread.setEnabled(true);
        }, 3_000);
    };

} // class DemoThreadFragment