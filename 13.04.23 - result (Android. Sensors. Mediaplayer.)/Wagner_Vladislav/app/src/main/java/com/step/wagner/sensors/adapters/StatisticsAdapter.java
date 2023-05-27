package com.step.wagner.sensors.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import com.step.wagner.sensors.R;
import com.step.wagner.sensors.fragments.dialogues.StatisticsDetailsDialog;
import com.step.wagner.sensors.infrastructure.Utils;
import com.step.wagner.sensors.models.entities.Statistics;

import java.util.List;

public class StatisticsAdapter extends BaseAdapter<Statistics> {

    FragmentManager fragmentManager;

    public StatisticsAdapter(@NonNull Context context, @NonNull List<Statistics> collection, FragmentManager fragmentManager) {
        super(context, collection);
         this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        View itemView = inflater.inflate(R.layout.processing_history_item, recyclerView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Statistics statisticsItem = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;

        holder.txvDataCount.setText(String.format("Количество данных: %d",statisticsItem.getAmount()));
        holder.txvStartDateTime.setText(String.format("Дата и время опроса: %s", Utils.dbDateTimeFormat.format(statisticsItem.getCollectingStartTime())));
        holder.txvProcessingDateTime.setText(String.format("Дата и время обработки: %s", Utils.dbDateTimeFormat.format(statisticsItem.getHandlingTime())));

        //Задать обработчик клика для вывода детальной информации
        holder.cardView.setOnClickListener(cv -> details(position));
    }

    //ViewHolder наследуется от ViewHolder базового класса для возможности применения полиморфизма
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Id записи
        public final TextView txvStatId;
        //Количество ранее прочитанных данных
        public final TextView txvDataCount;

        //Дата и время запроса
        public final TextView txvStartDateTime;

        //Значения акселерометра
        public final TextView txvProcessingDateTime;

        public final CardView cardView;

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txvStatId = itemView.findViewById(R.id.statisticsItemIdTxv);
            this.txvDataCount = itemView.findViewById(R.id.dataCountTxv);
            this.txvStartDateTime = itemView.findViewById(R.id.startDateTimeTxv);
            this.txvProcessingDateTime = itemView.findViewById(R.id.processingDateTimeTxv);
            this.cardView = itemView.findViewById(R.id.mainCv);

        }
    }

    private void details(int index){

        Statistics statisticsItem = collection.get(index);

        //Подготовить и запустить диалог
        Bundle bundle = new Bundle();

        bundle.putParcelable("statistics",statisticsItem);

        StatisticsDetailsDialog sdDialog = new StatisticsDetailsDialog();

        sdDialog.setArguments(bundle);

        sdDialog.show(fragmentManager,"statistics_details_dialog");

    }

}
