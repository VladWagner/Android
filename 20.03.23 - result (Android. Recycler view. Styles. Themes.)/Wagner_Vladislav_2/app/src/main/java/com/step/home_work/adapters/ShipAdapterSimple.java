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
import com.step.home_work.activities.ShipActivity;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.ship.Ship;

import java.util.List;

public class ShipAdapterSimple extends RecyclerView.Adapter<ShipAdapterSimple.ViewHolder> {

    //Загрузчик разметки
    private LayoutInflater inflater;

    //Id ресурса
    private int layoutId;

    //Ссылка на коллекцию
    private List<Ship> ships;

    public ShipAdapterSimple(Context context, int layoutId, List<Ship> collection) {

        this.inflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.ships = collection;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(layoutId, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return ships.size();
    }

    //Задать значения в поля и назначить обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Ship shipItem = ships.get(position);

        holder.shipTypeTxv.setText(String.format("Тип судна: %s", shipItem.getShipType().getType()));
        holder.shipNameTxv.setText(String.format("Название судна: %s", shipItem.getShipName()));

        holder.loadCapacityTxv.setText(String.format("Грузоподъемность: %s т.", Utils.numbersFormatter.format(shipItem.getLoadCapacity())));

        holder.destinationTxv.setText(String.format("Пункт назначения: %s", shipItem.getDestination()));
        holder.cargoTypeTxv.setText(String.format("Тип груза: %s", shipItem.getCargoType()));

        holder.cargoWeightTxv.setText((String.format("Вес груза: %s т.", Utils.numbersFormatter.format(shipItem.getCargoWeight()))));
        holder.tonPriceTxv.setText((String.format("Цена 1 тонны: %d $", shipItem.getTonPrice())));



    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //region Поля

        //Тип корабля
        public final TextView shipTypeTxv;

        //Название судна
        public final TextView shipNameTxv;

        //Грузоподъемность
        public final TextView loadCapacityTxv;

        //Пункт назанчения
        public final TextView destinationTxv;

        //Тип груза
        public final TextView cargoTypeTxv;

        //Вес груза
        public final TextView cargoWeightTxv;

        //Стоимость 1 тонны
        public final TextView tonPriceTxv;



        //endregion
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.shipTypeTxv = itemView.findViewById(R.id.shipType);
            this.shipNameTxv = itemView.findViewById(R.id.shipName);
            this.loadCapacityTxv = itemView.findViewById(R.id.loadCapacity);
            this.destinationTxv = itemView.findViewById(R.id.destination);
            this.cargoTypeTxv = itemView.findViewById(R.id.cargoType);
            this.cargoWeightTxv = itemView.findViewById(R.id.cargoWeight);
            this.tonPriceTxv = itemView.findViewById(R.id.tonPrice);

        }//ctor

    }//viewHolder



}
