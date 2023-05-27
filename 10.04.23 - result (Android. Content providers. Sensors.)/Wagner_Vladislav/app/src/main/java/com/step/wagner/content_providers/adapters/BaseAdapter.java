package com.step.wagner.content_providers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.step.wagner.content_providers.R;

import java.util.List;

//Базовй класс адаптера - нужен для передачи адапетров в параметрах
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    //Загрузчик разметки
    protected LayoutInflater inflater;

    //Ссылка на коллекцию
    protected List<T> collection;

    protected Context context;


    public BaseAdapter(@NonNull Context context, @NonNull List<T> collection) {

        this.inflater= LayoutInflater.from(context);
        this.collection = collection;
        this.context = context;


    }

    @NonNull
    @Override
    public abstract ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType);

    @Override
    public int getItemCount() {
        return collection.size();
    }


    //Задать значения в поля и привязать обработчики
    @Override
    public abstract void onBindViewHolder(@NonNull ViewHolder holder, int position);


    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
