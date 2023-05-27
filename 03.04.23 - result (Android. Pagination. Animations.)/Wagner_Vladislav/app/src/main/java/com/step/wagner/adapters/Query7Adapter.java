package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.step.wagner.R;
import com.step.wagner.models.queries.Query7;

import java.util.List;

public class Query7Adapter extends BaseAdapter<Query7> {

    public Query7Adapter(@NonNull Context context, @NonNull List<Query7> collection) {

        super(context,collection);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        View itemView = inflater.inflate(R.layout.query_7_item, recyclerView, false);

        return new ViewHolder(itemView);
    }


    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Query7 query7Item = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;

        holder.specialityTxv.setText(String.format("Специальность доктора: %s",query7Item.getSpeciality()));

        holder.amountTxv.setText(String.format("Кол-во записей: %d",query7Item.getAmount()) );
        holder.avgPriceTxv.setText(String.format("Средний %s отчислений: %.2f","%",query7Item.getAvgPercent()) );

    }

    //ViewHolder
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Дата приёма
        public final TextView specialityTxv;


        //Процент отчислений
        public final TextView amountTxv;

        //Зарплата
        public final TextView avgPriceTxv;

        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.specialityTxv =    itemView.findViewById(R.id.speciality);
            this.amountTxv =        itemView.findViewById(R.id.amount);
            this.avgPriceTxv =      itemView.findViewById(R.id.avgPercent);


        }
    }


}
