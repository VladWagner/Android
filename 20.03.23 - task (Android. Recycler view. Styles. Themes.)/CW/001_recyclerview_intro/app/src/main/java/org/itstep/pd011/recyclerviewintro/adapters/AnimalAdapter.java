package org.itstep.pd011.recyclerviewintro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;


import org.itstep.pd011.recyclerviewintro.R;
import org.itstep.pd011.recyclerviewintro.models.Animal;

import java.util.List;
import java.util.Locale;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {

    private final LayoutInflater inflater;    // загрузчик разметки
    private final List<Animal> animals;       // коллекция данных

    // В конструкторе получаем контекст создания адаптера (чтобы
    // в свою очередь получить из него загрузчик разметки) и ссылку
    // на коллекцию отображаемых данных
    public AnimalAdapter(Context context, List<Animal> animals) {
        this.animals = animals;
        this.inflater = LayoutInflater.from(context);
    } // AnimalAdapter

    // возвращает экземпляр ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.animal_item, parent, false));
    } // onCreateViewHolder

    // возвращает количество записей в коллекции
    @Override
    public int getItemCount() {
        return animals.size();
    }

    // Привязчик данных к элементам интерфейса
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // получить элемент коллекции
        Animal animal = animals.get(position);

        // записать значения полей в элементы интерфейса пользователя
        holder.imgAnimal.setImageResource(animal.getPhotoId());
        holder.txvAnimalName.setText(animal.getName());
        holder.txvAnimalWeight.setText(String.format(Locale.UK, "Вес %.3f кг", animal.getWeight()));

        // назначить обработчики кликов по элементам
        // holder.imgAnimal.setOnClickListener(v -> clickHandler(v, position));
        // holder.txvAnimalName.setOnClickListener(v -> clickHandler(v, position));
        // holder.txvAnimalWeight.setOnClickListener(v -> clickHandler(v, position));

        // достаточно слушать только контейнер
        holder.llAnimal.setOnClickListener(v -> clickHandler(v, position));
    } // onBindViewHolder


    // пример обработчка события клика по элементам интерфейса
    private void clickHandler(View view, int position) {
        String text = String.format("Клик по элементу %d", position);
        Snackbar sb = Snackbar.make(view,  text, Snackbar.LENGTH_INDEFINITE);
        sb.setAction("OK", v -> {});
        sb.show();
    } // clickHandler

    // класс для хранения элементов интерфейса
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // ссылки на элементы интерфейса
        final LinearLayout llAnimal;
        final ImageView imgAnimal;
        final TextView txvAnimalName;
        final TextView txvAnimalWeight;

        // связь разметки и ссылок на элементы интерфейса
        public ViewHolder(@NonNull View view) {
            super(view);

            llAnimal = view.findViewById(R.id.layout_item);
            imgAnimal = view.findViewById(R.id.imgAnimal);
            txvAnimalName = view.findViewById(R.id.txvAnimalName);
            txvAnimalWeight = view.findViewById(R.id.txvAnimalWeight);
        } // ViewHolder
    } // class ViewHolder
}
