package org.itstep.pd011.listviewdiscover.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import org.itstep.pd011.listviewdiscover.R;
import org.itstep.pd011.listviewdiscover.models.School;

import java.util.List;
import java.util.Locale;

public class SchoolActionAdapter extends ArrayAdapter<School> {
    private LayoutInflater inflater;     // загрузчик из контекст создания - активность или фрагмент
    private int            layout;       // ид разметки элемента списка
    private List<School> schools;      // коллекция данных

    // для создания адаптера в точке вызова
    public SchoolActionAdapter(@NonNull Context context, int resource, @NonNull List<School> schools) {
        super(context, resource, schools);

        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.schools = schools;
    } // SchoolButtonsAdapter

    // Формирование каждого элемента списка
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

        Button btnEdit = view.findViewById(R.id.btnEditItem);
        Button btnRemove = view.findViewById(R.id.btnRemoveItem);

        // получить данные - ссылку на элемент из коллекции
        School school = schools.get(position);

        // поместить данные из очередного элемента коллекции в элементы
        // отображения
        imgSchool.setImageResource(school.getIdImage());
        txvName.setText(school.getName());
        txvCost.setText(String.format(Locale.UK, "Цена: %.2f", school.getCost()));
        txvAmount.setText(String.format(Locale.UK, "Количество: %d", school.getAmount()));

        // обработчики кликов по кнопкам
        btnEdit.setOnClickListener(v -> editItem(position));
        btnRemove.setOnClickListener(v -> removeItem(v, position));

        // назначаем обработчика клика по элементам разметки, т.к. клик в
        // основной активности перехватывается слушателями кнопками
        LinearLayout llTextViews = view.findViewById(R.id.llTextViews);
        llTextViews.setOnClickListener(v->onClickListner(v, position));
        imgSchool.setOnClickListener(v->onClickListner(v, position));

        // вернуть сформированный view
        return view;
    } // getView

    // слушатель клика по остальным элементам - для имитации ожидаемого
    // поведения элемента разметки
    private void onClickListner(View view, int position) {
        String str = schools.get(position).getName();
        Snackbar.make(view, String.format("Выбран '%s'", str), Snackbar.LENGTH_LONG).show();
    }

    // имитируем редактирование
    private void editItem(int position) {
        School school = schools.get(position);

        school.setAmount(school.getAmount() + 1);

        // метод ArrayAdapter - требует перерисовки измененного элемента коллекции
        notifyDataSetChanged();
    } // editItem

    // удаление из колекции
    private void removeItem(View view, int position) {
        String str = schools.get(position).getName();

        schools.remove(position);

        // метод ArrayAdapter - требует перерисовки измененного элемента коллекции
        notifyDataSetChanged();

        Snackbar.make(view, String.format("Удален '%s'", str), Snackbar.LENGTH_LONG).show();
    } // removeItem

}
