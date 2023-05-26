package org.itstep.pd011.recyclerviewintro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

// Адаптер для RecyclerView
public class AnimalButtonAdapter extends RecyclerView.Adapter<AnimalButtonAdapter.ViewHolder> {

    private final LayoutInflater inflater;    // загрузчик разметки
    private final List<Animal> animals;       // коллекция данных

    int position;

    // В конструкторе получаем контекст создания адаптера - чтобы
    // в свою очередь получить из него загрузчик разметки и ссылку
    // на коллекцию отображаемых данных
    public AnimalButtonAdapter(Context context, List<Animal> animals) {
        this.animals = animals;
        this.inflater = LayoutInflater.from(context);
    } // AnimalButtonAdapter

    // возвращает экземпляр ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.animal_buttons_item, parent, false));
    } // onCreateViewHolder

    // возвращает количество записей в коллекции
    @Override
    public int getItemCount() {
        return animals.size();
    }

    // Привязчик данных к элементам интерфейса
    @Override
    public void onBindViewHolder(@NonNull AnimalButtonAdapter.ViewHolder holder, int position) {
        Animal animal = animals.get(position);

        // this.position = position;

        holder.imgAnimal.setImageResource(animal.getPhotoId());
        holder.txvAnimalName.setText(animal.getName());
        holder.txvAnimalWeight.setText(String.format(Locale.UK, "Вес %.3f кг", animal.getWeight()));

        // назначить обработчики кликов по элементам
        // holder.imgAnimal.setOnClickListener(v -> clickHandler(v, position));
        // holder.txvAnimalName.setOnClickListener(v -> clickHandler(v, position));
        // holder.txvAnimalWeight.setOnClickListener(v -> clickHandler(v, position));
        holder.llAnimal.setOnClickListener(v -> clickHandler(v, position));

        holder.btnEditAnimal.setOnClickListener(v -> edit(position));
        holder.btnDeleteAnimal.setOnClickListener(v -> delete(position));
    } // onBindViewHolder


    // пример обработчка события клика по элементам интерфейса
    private void clickHandler(View view, int position) {
        String text = "Клик по элементу " + position;
        Snackbar sb = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE);
        sb.setAction("OK", v -> {});
        sb.show();
    } // clickHandler

    // пример редактирования данных в элементе
    private void edit(int position) {
        // ссылка на элемент коллекции
        Animal animal = animals.get(position);

        // редактирование данных - увеличим вес, например
        animal.setWeight(animal.getWeight() + 0.015);

        // применить изменения к адаптеру
        notifyDataSetChanged();
    } // edit

    // пример удаления записи из коллекции
    private void delete(int position) {
        animals.remove(position);    // удаление записи по индексу
        notifyDataSetChanged();      // применить изменения к адаптеру
    } // delete


    // класс для хранения элементов интерфейса
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // ссылки на элементы интерфейса
        final LinearLayout llAnimal;
        final ImageView imgAnimal;
        final TextView txvAnimalName;
        final TextView txvAnimalWeight;

        final Button btnEditAnimal;
        final Button btnDeleteAnimal;

        // связь разметки и ссылок на элементы интерфейса
        public ViewHolder(@NonNull View view) {
            super(view);

            llAnimal = view.findViewById(R.id.layout_item);
            imgAnimal = view.findViewById(R.id.imgAnimal);
            txvAnimalName = view.findViewById(R.id.txvAnimalName);
            txvAnimalWeight = view.findViewById(R.id.txvAnimalWeight);

            btnEditAnimal = view.findViewById(R.id.btnEditAnimal);
            btnDeleteAnimal = view.findViewById(R.id.btnRemoveAnimal);
        } // ViewHolder
    } // class ViewHolder
}

