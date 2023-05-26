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
import com.step.home_work.models.queries.Query7;

import java.util.List;

public class Query7Adapter extends RecyclerView.Adapter<Query7Adapter.ViewHolder> {

    //Загрузчик разметки
    private LayoutInflater inflater;


    //Ссылка на коллекцию
    private List<Query7> query7Result;

    //Представление, где находится rcv
    private Context context;


    public Query7Adapter(@NonNull Context context, @NonNull List<Query7> collection) {

        this.inflater= LayoutInflater.from(context);
        this.query7Result = collection;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(R.layout.query7_item, listView, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return query7Result.size();
    }


    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Query7 query7Item = query7Result.get(position);


        holder.specialityTxv.setText(String.format("Специальность доктора: %s",query7Item.getSpeciality()));

        holder.amountTxv.setText(String.format("Кол-во записей: %d",query7Item.getAmount()) );
        holder.avgPriceTxv.setText(String.format("Средний %s отчислений: %.2f","%",query7Item.getAvgPercent()) );

    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

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
