package com.step.home_work.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.step.home_work.R;
import com.step.home_work.activities.AnimalActivity;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.Animal;

import java.util.List;
import java.util.Locale;

public class AnimalAdapterSimple extends RecyclerView.Adapter<AnimalAdapterSimple.ViewHolder> {

    //Загрузчик разметки
    private LayoutInflater inflater;

    //Id ресурса
    private int layoutId;

    //Ссылка на коллекцию
    private List<Animal> animals;


    public AnimalAdapterSimple(@NonNull Context context, int layout, @NonNull List<Animal> collection) {

        this.inflater= LayoutInflater.from(context);
        this.layoutId = layout;
        this.animals = collection;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(layoutId, listView, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }


    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Animal animalItem = animals.get(position);
        holder.breedTxv.setText(String.format("Порода: %s",animalItem.getBreed()));
        holder.nameTxv.setText(String.format("Кличка: %s",animalItem.getName()));

        holder.ageTxv.setText(String.format("Возраст: %d лет",animalItem.getAge()));
        holder.animalWeightTxv.setText(String.format("Вес: %.2f кг.",animalItem.getWeight()));

        holder.ownerSnpTxv.setText((String.format("Владелец:%s",animalItem.getOwnerSnp()) ) );


    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //region Поля

        //Порода
        public final TextView breedTxv;

        //Кличка
        public final TextView nameTxv;

        //Возраст
        public final TextView ageTxv;

        //ФИО владельца
        public final TextView ownerSnpTxv;

        //Вес
        public final TextView animalWeightTxv;


        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.breedTxv =         itemView.findViewById(R.id.breed);
            this.nameTxv =          itemView.findViewById(R.id.name);
            this.ageTxv =          itemView.findViewById(R.id.age);
            this.ownerSnpTxv =      itemView.findViewById(R.id.ownerSnp);
            this.animalWeightTxv =  itemView.findViewById(R.id.weight);


        }
    }

}
