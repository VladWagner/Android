package org.itstep.pd011.gridviewdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;


import org.itstep.pd011.gridviewdemo.R;
import org.itstep.pd011.gridviewdemo.models.Animal;

import java.util.List;
import java.util.Locale;

public class AnimalGridAdapter extends ArrayAdapter<Animal>  {
    private Context context;               // пример использования Context в адаптере
    private LayoutInflater layoutInflater; // загрузчик разметки
    private int layout;                    // ид разметки одного элемента
    private List<Animal> animals;          // ссылка на коллекцию данных

    // context  - контект адаптера
    // resource - идентификатор разметки
    public AnimalGridAdapter(@NonNull Context context, int resource, @NonNull List<Animal> animals) {
        super(context, resource, animals);

        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.layout = resource;
        this.animals = animals;
    } // AnimalGridAdapter

    // Запись данных в элемент разметки
    // convertView - старое состояние элемента разметки
    // parent      - ссылка на GridView
    public View getView(int position, View convertView, ViewGroup parent) {
        /* Использование ViewHolder */
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView, position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.position = position;
        } // if

        // получить ссылку на элемент коллекции
        Animal animal = animals.get(position);

        // вывести данные в элементы разметки view
        viewHolder.imgAnimal.setImageResource(animal.getPhotoId());
        viewHolder.txvName.setText(animal.getName());
        viewHolder.txvWeight.setText(String.format(Locale.UK, "%.2f кг", animal.getWeight()));

        // вернуть сформированное представление
        return convertView;
    } // getView

    // внутренний класс - для хранения ссылок на элементы разметки
    // исключаем повторные операции поиска элементов в разметке по id
    private class ViewHolder {
        final ImageView imgAnimal;
        final TextView txvName;
        final TextView txvWeight;
        final LinearLayout llAnimal;

        private int position;

        public ViewHolder(View view, int position) {
            // получить элементы отображения
            imgAnimal = view.findViewById(R.id.imgAnimal);
            txvName = view.findViewById(R.id.txvAnimalName);
            txvWeight = view.findViewById(R.id.txvWeight);
            llAnimal = view.findViewById(R.id.llAnimal);

            this.position = position;

            llAnimal.setOnClickListener(v-> clickListner(llAnimal, this.position));

            // Избыточно, достаточно контейнер слушать
            // imgAnimal.setOnClickListener(v-> clickListner(imgAnimal, this.position));
            // txvName.setOnClickListener(v-> clickListner(txvName, this.position));
            // txvWeight.setOnClickListener(v-> clickListner(txvWeight, this.position));
        } // ViewHolder
    } // class ViewHolder

    // реализация слушателя события клика по элементу списка
    private void clickListner(View view, int position) {
        String text = "Клик по " + animals.get(position).getName();
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", v-> {});
        snackbar.show();
    };  // clickListner

}

