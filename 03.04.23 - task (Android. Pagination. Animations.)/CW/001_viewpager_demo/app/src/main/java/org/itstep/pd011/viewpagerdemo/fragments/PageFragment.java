package org.itstep.pd011.viewpagerdemo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.itstep.pd011.viewpagerdemo.R;

import java.util.Locale;


// фрагмент, реализующий страницу
public class PageFragment extends Fragment {

    // номер страницы
    private int pageNumber;

    // элементы страницы
    TextView txvPageHeader;
    TextView txvPageText;

    // фабричный метод, возвращающий экземпляр фрагмента
    public static PageFragment newInstance(int page) {
        PageFragment fragment = new PageFragment();

        // номер страницы передать экземпляру фрагмента
        Bundle args = new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);

        // фрагмент сформирован
        return fragment;
    } // newInstance

    // обязательный пустой конструктор фрагмента
    public PageFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // попытка получения параметров запуска - в д.с. номер страницы
        // если параметров нет - первый запуск, первая страница
        // pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
        pageNumber = getArguments().getInt("num");
    } // onCreate

    // настройка контента фрагмента
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // связь с разметкой - возможна различная разметка для разных страниц
        // например, в зависимости от pageNumber
        View result = inflater.inflate(R.layout.fragment_page, container, false);

        // связь элементов интерфейса с разметкой
        txvPageHeader = result.findViewById(R.id.txvPageHeader);
        txvPageText = result.findViewById(R.id.txvPageText);

        // !!!! загрузка контента страницы - основное для прикладной части !!!!
        int t = pageNumber + 1;
        txvPageHeader.setText("Заголовок " + t);
        String text = String.format(Locale.UK, "Текст на странице %d.\n-----------------\n", t);

        // имитация контента для отображения
        switch (t) {
            case 1: case 5: case 8:
                text += "Есть такая штука как ZArchiver, мне очень нравится\n" +
                        "[15:46]\n" +
                        "А еще ES проводник, тоже очень удобный файловый менеджер(изменено)\n";
                break;
            case 2: case 4: case 7:
                text += "Смородин Владислав — Сегодня, в 15:16\n" +
                        "Владислав Константинович, трансляции нет\n";
            default:
                text += "HostiMan - это провайдер, который предоставляет лучший бесплатный" +
                        " хостинг для сайта (free hosting) и профессиональный платный " +
                        "от 139 руб/мес.";
        }
        txvPageText.setText(text);
        return result;
    } // onCreateView
}