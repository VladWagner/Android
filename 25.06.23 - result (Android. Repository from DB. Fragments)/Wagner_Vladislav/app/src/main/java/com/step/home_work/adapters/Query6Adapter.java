package com.step.home_work.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.step.home_work.R;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.queries.Query6;

import java.util.List;

public class Query6Adapter extends RecyclerView.Adapter<Query6Adapter.ViewHolder> {

    //Загрузчик разметки
    private LayoutInflater inflater;


    //Ссылка на коллекцию
    private List<Query6> query6Result;

    //Представление, где находится rcv
    private Context context;


    public Query6Adapter(@NonNull Context context, @NonNull List<Query6> collection) {

        this.inflater= LayoutInflater.from(context);
        this.query6Result = collection;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(R.layout.query6_item, listView, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return query6Result.size();
    }


    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Query6 query6Item = query6Result.get(position);


        holder.appointmentDate.setText(String.format("Дата приема: %s",Utils.dateFormat.format(query6Item.getDate())));

        holder.amountTxv.setText(String.format("Кол-во записей: %d",query6Item.getAmount()) );
        holder.maxPriceTxv.setText(String.format("Макс. стоимость приёма: %d",query6Item.getMaxPrice()) );

    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

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
