package com.step.home_work.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.step.home_work.R;
import com.step.home_work.activities.ShipActivity;
import com.step.home_work.activities.ShipsListActivity;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.ship.Ship;

import java.util.List;

public class ShipsAdapter extends ArrayAdapter<Ship> {

    //Загрузчик разметки
    private LayoutInflater inflater;

    //Id ресурса
    private int layoutId;

    //Ссылка на коллекцию
    private List<Ship> ships;

    //Представление, где находится listView
    private Context context;

    //Активность, выводящая список
    private View listActivity;

    public ShipsAdapter(@NonNull Context context, int layout, @NonNull List<Ship> collection) {
        super(context, layout, collection);

        this.inflater= LayoutInflater.from(context);
        this.layoutId = layout;
        this.ships = collection;
        this.context = context;

        this.listActivity = ((Activity) context).findViewById(android.R.id.content);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, @Nullable View itemView, @NonNull ViewGroup listView) {

        final ViewHolder viewHolder;

        if (itemView == null){
            itemView = inflater.inflate(this.layoutId,listView,false);
            viewHolder = new ViewHolder(itemView,position);

            itemView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) itemView.getTag();
            viewHolder.index = position;

            //Назначить обработчики по индексу
            //viewHolder.setListeners();
        }

        Ship shipItem = ships.get(position);

        //Задать изображение
        try {

            Utils.setImageView(shipItem.getFileNameFromType(),viewHolder.shipImageView,context);

        } catch (Exception e) {
            Utils.showSnackBar(((Activity) context).findViewById(android.R.id.content),
                    String.format("Ошибка imageView для судна с id: %d",shipItem.getId()));
        }

        viewHolder.shipIdTxv.setText(String.format("Id судна: %d",shipItem.getId()));

        viewHolder.shipTypeTxv.setText(String.format("Тип судна: %s",shipItem.getShipType()));
        viewHolder.loadCapacityTxv.setText(String.format("Грузоподъемность: %s т.", Utils.numbersFormatter.format(shipItem.getLoadCapacity()) ));

        viewHolder.destinationTxv.setText(String.format("Пункт назначения: %s",shipItem.getDestination()));
        viewHolder.cargoTypeTxv.setText(String.format("Тип груза: %s",shipItem.getCargoType()));

        viewHolder.cargoWeightTxv.setText((String.format("Вес груза: %s т.",Utils.numbersFormatter.format(shipItem.getCargoWeight())) ) );
        viewHolder.tonPriceTxv.setText((String.format("Цена 1 тонны: %d $",shipItem.getTonPrice()) ) );

        //Вывести признаки
        viewHolder.shipSignsTxv.setText(String.format("Требуется пирс: %s\nТребуется якорная стоянка: %s\nТребуется дозаправка : %s",
                shipItem.isDockNeeds() ? "да" : "нет",
                shipItem.isAnchorage() ? "да" : "нет",
                shipItem.isRefuelingNeeds() ? "да" : "нет"
        ));


        return itemView;

    }

    private class ViewHolder{

        //region Поля
        public final ImageView shipImageView;

        public final LinearLayout mainLayout;

        //id
        public final TextView shipIdTxv;

        //Тип корабля
        public final TextView shipTypeTxv;

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

        //Признаки судна
        public final TextView shipSignsTxv;

        //Кнопки
        public final Button btnReduceWeight;
        public final Button btnIncreaseWeight;

        public final Button btnReducePrice;
        public final Button btnIncreasePrice;

        //Индекс выводимого элемента
        public int index;


        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(View itemView, int index) {

            //Поля вывода
            this.shipImageView =     itemView.findViewById(R.id.shipImage);
            this.mainLayout =        itemView.findViewById(R.id.mainLl);
            this.shipIdTxv =         itemView.findViewById(R.id.shipId);
            this.shipTypeTxv =       itemView.findViewById(R.id.shipType);
            this.loadCapacityTxv =   itemView.findViewById(R.id.loadCapacity);
            this.destinationTxv =    itemView.findViewById(R.id.destination);
            this.cargoTypeTxv =      itemView.findViewById(R.id.cargoType);
            this.cargoWeightTxv =    itemView.findViewById(R.id.cargoWeight);
            this.tonPriceTxv =      itemView.findViewById(R.id.tonPrice);
            this.shipSignsTxv =      itemView.findViewById(R.id.shipSigns);

            //Кнопки
            this.btnReduceWeight =   itemView.findViewById(R.id.btnReduceWeight);
            this.btnIncreaseWeight = itemView.findViewById(R.id.btnIncreaseWeight);
            this.btnReducePrice =    itemView.findViewById(R.id.btnReducePrice);
            this.btnIncreasePrice =  itemView.findViewById(R.id.btnIncreasePrice);

            this.index = index;

            //Назначить обработчики
            setListeners();

        }

        public void setListeners(){

            //Обработчик клика по элементу
            mainLayout.setOnClickListener(v -> startShipFormActivity(index));
            cargoWeightTxv.setOnClickListener(v -> startShipFormActivity(index));
            tonPriceTxv.setOnClickListener(v -> startShipFormActivity(index));

            //Изменение стомисти и груза
            btnIncreaseWeight.setOnClickListener(v -> changeCargoWeight(R.string.btn_increase,cargoWeightTxv,index,v));
            btnReduceWeight.setOnClickListener(v -> changeCargoWeight(R.string.btn_reduce,cargoWeightTxv,index,v));

            btnIncreasePrice.setOnClickListener(v -> changeCargoPrice(R.string.btn_increase, tonPriceTxv,index,v));
            btnReducePrice.setOnClickListener(v -> changeCargoPrice(R.string.btn_reduce,tonPriceTxv,index,v));
        }

    }//ViewHolder

    //Переход на активность редактирования
    private void startShipFormActivity(int index/*, View view*/){
        Intent intent = new Intent(context, ShipActivity.class);

        Ship ship = ships.get(index);

        //Передать объект корабля в активность
        intent.putExtra(Ship.class.getCanonicalName(),ship);

        //Запустить активность
        ((Activity) context).startActivityForResult(intent, Parameters.SHIP_ACTIVITY_ID);
    }


    //Изменение значений веса
    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
    private void changeCargoWeight(int btnType, TextView field, int index, View view) {
        try {

            Ship ship = ships.get(index);
            //Текущее значение в поле
            int currValue = ship.getCargoWeight();

            switch (btnType) {

                case R.string.btn_increase:
                    ship.setCargoWeight(currValue + Ship.WEIGHT_DELTA);
                    field.setText(String.format("Вес груза: %s т.",Utils.numbersFormatter.format(currValue + Ship.WEIGHT_DELTA) ));
                    break;

                case R.string.btn_reduce:
                    if (currValue >= Ship.WEIGHT_DELTA) {
                        ship.setCargoWeight(currValue - Ship.WEIGHT_DELTA);
                        field.setText(String.format("Вес груза: %s т.",Utils.numbersFormatter.format(currValue - Ship.WEIGHT_DELTA)) );
                    }
                    break;

            }//switch

        } catch (Exception e) {
            Utils.showSnackBar(listActivity,e.getMessage());
        }//try-catch
    }

    //Изменение значений стоимости
    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
    private void changeCargoPrice(int btnType, TextView field, int index, View view) {
        try {

            Ship ship = ships.get(index);
            //Текущее значение в поле
            int currValue = ship.getTonPrice();

            switch (btnType) {

                case R.string.btn_increase:
                    ship.setTonPrice(currValue + Ship.PRICE_DELTA);
                    field.setText(String.format("Цена 1 тонны: %d $",currValue + Ship.PRICE_DELTA));
                    break;

                case R.string.btn_reduce:
                    if (currValue >= Ship.WEIGHT_DELTA) {
                        ship.setTonPrice(currValue - Ship.PRICE_DELTA);
                        field.setText(String.format("Цена 1 тонны: %d $",currValue - Ship.PRICE_DELTA));
                    }
                    break;

            }//switch

        } catch (Exception e) {
            Utils.showSnackBar(listActivity,e.getMessage());
        }//try-catch
    }


}
