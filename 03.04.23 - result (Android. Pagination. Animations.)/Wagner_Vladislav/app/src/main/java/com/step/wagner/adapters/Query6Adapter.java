package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.step.wagner.R;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.queries.Query6;

import java.util.List;

public class Query6Adapter extends BaseAdapter<Query6> {

    public Query6Adapter(@NonNull Context context, @NonNull List<Query6> collection) {

        super(context,collection);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        View itemView = inflater.inflate(R.layout.query_6_item, recyclerView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Query6 query6Item = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;

        holder.appointmentDate.setText(String.format("Дата приема: %s",Utils.dateFormat.format(query6Item.getAppointmentDate())));

        holder.amountTxv.setText(String.format("Кол-во записей: %d",query6Item.getAmount()) );
        holder.maxPriceTxv.setText(String.format("Макс. стоимость приёма: %d",query6Item.getMaxPrice()) );

    }

    //ViewHolder
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Дата приёма
        public final TextView appointmentDate;


        //Процент отчислений
        public final TextView amountTxv;

        //Зарплата
        public final TextView maxPriceTxv;

        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.appointmentDate =    itemView.findViewById(R.id.appointmentDate);
            this.amountTxv =         itemView.findViewById(R.id.amount);
            this.maxPriceTxv =         itemView.findViewById(R.id.maxPrice);


        }
    }


}
