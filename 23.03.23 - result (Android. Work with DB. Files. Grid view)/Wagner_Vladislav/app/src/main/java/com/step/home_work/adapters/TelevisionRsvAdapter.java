package com.step.home_work.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.step.home_work.R;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.Television.Television;

import java.util.List;

public class TelevisionRsvAdapter extends RecyclerView.Adapter<TelevisionRsvAdapter.ViewHolder> {

    //Загрузчик разметки
    private LayoutInflater inflater;

    //Id ресурса
    private int layoutId;

    //Ссылка на коллекцию
    private List<Television> televisions;

    //Представление, где находится listView
    private Context context;

    //Активность, выводящая список
    private View listActivity;

    public TelevisionRsvAdapter(@NonNull Context context, int layout, @NonNull List<Television> collection) {

        this.inflater= LayoutInflater.from(context);
        this.layoutId = layout;
        this.televisions = collection;
        this.context = context;

        this.listActivity = ((Activity) context).findViewById(android.R.id.content);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(layoutId, listView, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return televisions.size();
    }


    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Television televisionItem = televisions.get(position);

        //Задать изображение
        try {

            Utils.setImageView(televisionItem.getFileName(),holder.tvImageView,context);

        } catch (Exception e) {
            Utils.showSnackBar(listActivity,String.format("Ошибка imageView для телевизора %s", televisionItem.getProducer()));
        }

        holder.producerTxv.setText(String.format("Производитель: %s",televisionItem.getProducer()));
        holder.diagonalTxv.setText(String.format("Диагональ: %.2f",televisionItem.getDiagonal()));
        holder.resolutionVertTxv.setText(String.format("Вертикальное разрешение: %d",televisionItem.getResolutionVert()));
        holder.resolutionHorizonTxv.setText(String.format("Горизонтальное разрешение: %d",televisionItem.getResolutionHorizon()));
        holder.priceTxv.setText(String.format("Цена: %s ₽",Utils.numbersFormatter.format(televisionItem.getPrice()) ) );

    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //region Поля
        public final ImageView tvImageView;

        //Порода
        public final TextView producerTxv;

        //Кличка
        public final TextView diagonalTxv;

        //Возраст
        public final TextView resolutionVertTxv;

        //Вес
        public final TextView resolutionHorizonTxv;

        //ФИО владельца
        public final TextView priceTxv;
        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.tvImageView =    itemView.findViewById(R.id.tvImage);
            this.producerTxv =         itemView.findViewById(R.id.producer);
            this.diagonalTxv =          itemView.findViewById(R.id.diagonal);
            this.resolutionVertTxv =          itemView.findViewById(R.id.resolutionVert);
            this.priceTxv =      itemView.findViewById(R.id.price);
            this.resolutionHorizonTxv =  itemView.findViewById(R.id.resolutionHorizon);


        }
    }


}
