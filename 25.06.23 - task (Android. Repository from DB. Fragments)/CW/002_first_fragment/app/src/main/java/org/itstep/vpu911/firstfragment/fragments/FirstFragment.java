package org.itstep.vpu911.firstfragment.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.itstep.vpu911.firstfragment.R;

import java.util.Date;
import java.util.Locale;

/*
 * onAttach(): при выполнении этого метода фрагмент ассоциируется с определенной activity.
 *             На этом этапе фрагмент и activity еще не полностью инициализированы.
 * onCreate(): происходит создание фрагмента. Этот метод вызывается после вызова
 *             соответствующего метода onCreate() у activity.
 * onCreateView(): фрагмент создает визуальный интерфейс
 * onActivityCreated(): вызывается после создания activity. С этого момента к компонентам
 *             интерфейса можно обращаться через метод findViewById()
 * onStart():  вызывается, когда фрагмент становится видимым
 * onResume(): фрагмент становится активным
 * onPause():  фрагмент продолжает оставаться видимым, но уже не активен
 * onStop():   фрагмент больше не является видимым
 * onDestroyView(): уничтожается интерфейс, представляющий фрагмент
 * onDestroy(): окончательно уничтожение фрагмента
 *
 * В коде класса фрагмента мы можем переопределить все или часть из этих методов.
 * Поскольку фрагменты часто используются для определенных целей, например,
 * для вывода списка каких-нибудь объектов, то по умолчанию нам доступны классы,
 * производные от Fragment, уже обладающие определенными возможностями:
 *     ListFragment: управляет списком элементов с помощью одного из адаптеров
 *     DialogFragment: используется для создания диалоговых окон
 *     PreferenceFragment: используется для управления настройками приложения
 *
 * */

public class FirstFragment extends Fragment {

    // элементы интерфейса фрагмента
    TextView txvFragment1Info;
    Button btnFragment1Action;

    // требуемый конструктор по умолчанию - связь с разметкой, т.к.
    // обрабатываестя событие onViewCreated
    public FirstFragment() {
        super(R.layout.fragment_first);
    } // FirstFragment


    // переопределение обработчика события создания визуального представления
    // фрагмента
    // Объект LayoutInflater используется для установки ресурса разметки для создания интерфейса
    // Параметр ViewGroup container устанавливает контейнер интерфейса
    // Параметр Bundle savedInstanceState передает ранее сохраненное состояние
    @Override  // создание представления фрагмента
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txvFragment1Info = view.findViewById(R.id.txvFragment1Info);
        btnFragment1Action = view.findViewById(R.id.btnFragment1Action);

        // onClick - обработчик
        btnFragment1Action.setOnClickListener(v -> {
            // формат даты
            // http://proglang.su/java/date-and-time
            String curDate =  String.format(Locale.UK, "Сегодня: %1$td/%1$tm/%1$tY", new Date());
            txvFragment1Info.setText(curDate);
        });
    } // onViewCreated


}