package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.step.wagner.R;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.entities.Collecting;

import java.util.List;

public class CollectingAdapter extends BaseAdapter<Collecting> {


    public CollectingAdapter(@NonNull Context context, @NonNull List<Collecting> collection) {
        super(context, collection);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        View itemView = inflater.inflate(R.layout.collecting_item, recyclerView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Collecting collectionItem = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;

        holder.txvCollectingId.setText(String.format("Id опроса: %d",collectionItem.getId()));
        holder.txvStartDateTime.setText(String.format("Начало опроса: %s", Utils.dbDateTimeFormat.format(collectionItem.getStartDateTime())));
        holder.txvAccelerometer.setText(String.format("Акселерометр:\n X: %.3f\n Y: %.3f\n Z: %.3f",
                collectionItem.getAccelerometer().getAxisX(),
                collectionItem.getAccelerometer().getAxisY(),
                collectionItem.getAccelerometer().getAxisZ()
        ));
        holder.txvApproximation.setText(String.format("Приближение: %.3f см", collectionItem.getApproximation()));
        holder.txvLightSensor.setText(String.format("Освещённость: %.3f ", collectionItem.getLight()));
        holder.txvReceivingTime.setText(String.format("Момент получения данных: %s", Utils.timeFormat.format(collectionItem.getReceivingTime())));
        holder.txvSensorsTypes.setText(String.format("Использованные датчики: %s", collectionItem.getSensorsTypes()));

    }

    //ViewHolder наследуется от ViewHolder базового класса для возможности применения полиморфизма
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Id записи
        public final TextView txvCollectingId;
        //Дата и время запроса
        public final TextView txvStartDateTime;

        //Значения акселерометра
        public final TextView txvAccelerometer;

        //Значение датчика приближения
        public final TextView txvApproximation;

        //Значение датчика освещённости
        public final TextView txvLightSensor;

        //Время завершения опроса
        public final TextView txvReceivingTime;

        //Заданные датчики для прослушивания
        public final TextView txvSensorsTypes;


        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txvCollectingId = itemView.findViewById(R.id.collectingIdTxv);
            this.txvStartDateTime = itemView.findViewById(R.id.startDateTimeTxv);
            this.txvAccelerometer = itemView.findViewById(R.id.accelerometerTxv);
            this.txvApproximation = itemView.findViewById(R.id.approximationTxv);
            this.txvLightSensor = itemView.findViewById(R.id.lightSensorTxv);
            this.txvReceivingTime = itemView.findViewById(R.id.receivingTimeTxv);
            this.txvSensorsTypes = itemView.findViewById(R.id.sensorsTypesTxv);

        }
    }


}
