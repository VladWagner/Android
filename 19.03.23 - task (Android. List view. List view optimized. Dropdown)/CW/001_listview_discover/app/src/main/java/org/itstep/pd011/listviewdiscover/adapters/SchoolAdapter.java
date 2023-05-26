package org.itstep.pd011.listviewdiscover.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.itstep.pd011.listviewdiscover.R;
import org.itstep.pd011.listviewdiscover.models.School;

import java.util.List;
import java.util.Locale;

public class SchoolAdapter extends ArrayAdapter<School> {
    // поля для работы адаптера
    // загрузчик разметки
    private LayoutInflater inflater;

    // идентификатор разметки одного элемента
    private int layout;

    // ссылка на коллекцию отображаемых данных
    private List<School> schools;

    // конструктор для создания адаптера
    public SchoolAdapter(@NonNull Context context, int resource, @NonNull List<School> schools) {
        super(context, resource, schools);

        layout = resource;
        inflater = LayoutInflater.from(context);
        this.schools = schools;
    } // SchoolAdapter

    // Формирование каждого элемента ListView
    // position    - индкес элемента коллекции для отображения
    // convertView - ссылка на элемент списка (при первом обращении - null)
    // parent      - ссылка на сам ListView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // связать разметку и ссылку на View
        View view = inflater.inflate(this.layout, parent, false);

        // связать разметку и ссылки на элементы отображения
        ImageView imgSchool = view.findViewById(R.id.imgItem);
        TextView txvName = view.findViewById(R.id.txvName);
        TextView txvCost = view.findViewById(R.id.txvCost);
        TextView txvAmount = view.findViewById(R.id.txvAmount);

        // получить данные - ссылку на элемент из коллекции
        School school = schools.get(position);

        // поместить данные из очередного элемента коллекции в элементы
        // отображения
        imgSchool.setImageResource(school.getIdImage());
        txvName.setText(school.getName());
        txvCost.setText(String.format(Locale.UK, "%.2f", school.getCost()));
        txvAmount.setText(String.format(Locale.UK, "%d", school.getAmount()));

        // вернуть сформированный view
        return view;
    } // getView

}
