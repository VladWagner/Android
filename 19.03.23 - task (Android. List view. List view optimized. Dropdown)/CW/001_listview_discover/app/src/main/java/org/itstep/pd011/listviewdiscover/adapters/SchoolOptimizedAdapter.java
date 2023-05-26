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

public class SchoolOptimizedAdapter extends ArrayAdapter<School> {
    private LayoutInflater inflater;     // загрузчик разметки элемента - из контекста создания - активность или фрагмент
    private int            layout;       // ид разметки элемента списка
    private List<School> schools;      // коллекция данных

    // для создания адаптера в точке вызова
    public SchoolOptimizedAdapter(@NonNull Context context, int resource, @NonNull List<School> schools) {
        super(context, resource, schools);

        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.schools = schools;
    } // SchoolButtonsAdapter

    // Формирование каждого элемента списка
    // position    - индкес элемента коллекции для отображения
    // convertView - ссылка на элемент списка (при первом обращении - null)
    // parent      - ссылка на сам ListView
    public View getView(int position, View convertView, ViewGroup parent) {
        // связать разметку и ссылку на View

        /*
        // 1й вариант оптимизации - читаем разметку из ресурсов только при первом обращении
        final View view = convertView == null
            ? inflater.inflate(this.layout, parent, false)
            : convertView;
        */

        // 2й вариант оптимизации - использование ViewHolder - внутренний класс
        final ViewHolder viewHolder;

        // создание и сохранение ViewHolder при первом обращении, чтение из
        // места сохранения при последующих обращениях
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView, position);

            // сохранить все ссылки на элементы разметки
            // в поле tag convertView, тип tag - Object
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.position = position;
        } // if

        // получить данные - ссылку на элемент из коллекции
        School school = schools.get(position);

        // вывести поля объекта в элементы интерфейса
        // поместить данные из очередного элемента коллекции в элементы
        // отображения
        viewHolder.imgSchool.setImageResource(school.getIdImage());
        viewHolder.txvName.setText(school.getName());
        viewHolder.txvCost.setText(String.format(Locale.UK, "Цена: %.2f", school.getCost()));
        viewHolder.txvAmount.setText(String.format(Locale.UK, "Количество: %d", school.getAmount()));

        // вернуть сформированное представление элемента списка
        return convertView;
    } // getView

    // класс ViewHolder - хранит ссылки на элементы разметки
    // исключает повторные операции поиска элементов в разметке
    private class ViewHolder {
        // элементы интерфейса из разметки элемента
        final ImageView imgSchool;
        final TextView txvName;
        final TextView txvCost;
        final TextView txvAmount;

        final Button btnEdit;
        final Button btnRemove;
        final LinearLayout llTextViews;

        private int position;

        public ViewHolder(View view, int position) {
            // связать разметку и ссылки на элементы отображения
            imgSchool = view.findViewById(R.id.imgItem);
            txvName = view.findViewById(R.id.txvName);
            txvCost = view.findViewById(R.id.txvCost);
            txvAmount = view.findViewById(R.id.txvAmount);

            btnEdit = view.findViewById(R.id.btnEditItem);
            btnRemove = view.findViewById(R.id.btnRemoveItem);

            llTextViews = view.findViewById(R.id.llTextViews);

            // позиция элемента в коллекции
            this.position = position;

            // назначаем обработчика клика по элементам разметки, т.к. клик в
            // основной активности блокируется слушателями кнопками
            llTextViews.setOnClickListener(v->onClickListner(v, this.position));
            imgSchool.setOnClickListener(v->onClickListner(v, this.position));

            // обработчики кликов по кнопкам
            btnEdit.setOnClickListener(v -> editItem(this.position));
            btnRemove.setOnClickListener(v -> removeItem(v, this.position));
        } // ViewHolder
    } // class ViewHolder

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

        notifyDataSetChanged();
    } // editItem

    // удаление из коллекции
    private void removeItem(View view, int position) {
        String str = schools.get(position).getName();

        schools.remove(position);
        notifyDataSetChanged();

        Snackbar.make(view, String.format("Удален '%s'", str), Snackbar.LENGTH_LONG).show();
    } // removeItem
}
