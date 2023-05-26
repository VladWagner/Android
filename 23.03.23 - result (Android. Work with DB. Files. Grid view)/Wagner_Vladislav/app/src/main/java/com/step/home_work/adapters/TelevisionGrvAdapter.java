package com.step.home_work.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.step.home_work.R;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.Television.Television;

import java.util.List;

public class TelevisionGrvAdapter extends ArrayAdapter<Television> {

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

    public TelevisionGrvAdapter(@NonNull Context context, int layout, @NonNull List<Television> collection) {
        super(context, layout, collection);

        this.inflater= LayoutInflater.from(context);
        this.layoutId = layout;
        this.televisions = collection;
        this.context = context;

        this.listActivity = ((Activity) context).findViewById(android.R.id.content);

    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, @Nullable View itemView, @NonNull ViewGroup listView) {

        final ViewHolder viewHolder;

        if (itemView == null){
            itemView = inflater.inflate(this.layoutId,listView,false);
            viewHolder = new ViewHolder(itemView);

            itemView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) itemView.getTag();
            viewHolder.index = position;

        }

        Television televisionItem = televisions.get(position);

        //Задать изображение
        try {

            Utils.setImageView(televisionItem.getFileName(),viewHolder.tvImageView,context);

        } catch (Exception e) {
            Utils.showSnackBar(listActivity,String.format("Ошибка imageView для телевизора %s", televisionItem.getProducer()));
        }

        viewHolder.producerTxv.setText(String.format("Производитель: %s",televisionItem.getProducer()));
        viewHolder.diagonalTxv.setText(String.format("Диагональ: %.2f",televisionItem.getDiagonal()));
        viewHolder.resolutionVertTxv.setText(String.format("Вертикальное разрешение: %d",televisionItem.getResolutionVert()));
        viewHolder.resolutionHorizonTxv.setText(String.format("Горизонтальное разрешение: %d",televisionItem.getResolutionHorizon()));
        viewHolder.priceTxv.setText(String.format(String.format("Цена: %d",televisionItem.getPrice()) ));

        return itemView;

    }

    private class ViewHolder{

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


        //Индекс выводимого элемента
        public int index;
        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(View itemView) {

            //Поля вывода
            this.tvImageView =    itemView.findViewById(R.id.tvImage);
            this.producerTxv =         itemView.findViewById(R.id.producer);
            this.diagonalTxv =          itemView.findViewById(R.id.diagonal);
            this.resolutionVertTxv =          itemView.findViewById(R.id.resolutionVert);
            this.priceTxv =      itemView.findViewById(R.id.price);
            this.resolutionHorizonTxv =  itemView.findViewById(R.id.resolutionHorizon);



        }


    }//ViewHolder



}
