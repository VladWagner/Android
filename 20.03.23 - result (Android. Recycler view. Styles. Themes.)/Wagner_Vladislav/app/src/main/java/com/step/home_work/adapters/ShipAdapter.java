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

public class ShipAdapter extends RecyclerView.Adapter<ShipAdapter.ViewHolder> {

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

    public ShipAdapter(Context context,int layoutId,List<Ship> collection) {

        this.inflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.ships = collection;
        this.context = context;

        this.listActivity = ((Activity) context).findViewById(android.R.id.content);
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

        //Задать изображение
        try {

            Utils.setImageView(shipItem.getFileNameFromType(), holder.shipImageView, context);

        } catch (Exception e) {
            Utils.showSnackBar(((Activity) context).findViewById(android.R.id.content),
                    String.format("Ошибка imageView для судна с id: %d", shipItem.getId()));
        }

        holder.shipIdTxv.setText(String.format("Id судна: %d", shipItem.getId()));

        holder.shipTypeTxv.setText(String.format("Тип судна: %s", shipItem.getShipType().getType()));
        holder.shipNameTxv.setText(String.format("Название судна: %s", shipItem.getShipName()));

        holder.loadCapacityTxv.setText(String.format("Грузоподъемность: %s т.", Utils.numbersFormatter.format(shipItem.getLoadCapacity())));

        holder.destinationTxv.setText(String.format("Пункт назначения: %s", shipItem.getDestination()));
        holder.cargoTypeTxv.setText(String.format("Тип груза: %s", shipItem.getCargoType()));

        holder.cargoWeightTxv.setText((String.format("Вес груза: %s т.", Utils.numbersFormatter.format(shipItem.getCargoWeight()))));
        holder.tonPriceTxv.setText((String.format("Цена 1 тонны: %d $", shipItem.getTonPrice())));

        holder.cargoPriceTxv.setText((String.format("Стоимость груза: %s $",
                Utils.numbersFormatter.format(Ship.countCargoPrice(shipItem.getCargoWeight(), shipItem.getTonPrice()))
        )));

        //Вывести признаки
        holder.shipSignsTxv.setText(String.format("Требуется пирс: %s\nТребуется якорная стоянка: %s\nТребуется дозаправка : %s",
                shipItem.isDockNeeds() ? "да" : "нет",
                shipItem.isAnchorage() ? "да" : "нет",
                shipItem.isRefuelingNeeds() ? "да" : "нет"
        ));



        //Обработчик клика по элементу
        holder.mainLayout.setOnClickListener(v -> startShipFormActivity(position));

        //Обработчики кнопок удаления и редактирования
        holder.btnEdit.setOnClickListener(v -> startShipFormActivity(position));
        holder.btnDelete.setOnClickListener(v -> deleteShip(position));

        //Изменение стомисти и груза
        holder.btnIncreaseWeight.setOnClickListener(v -> changeCargoWeight(v, holder.cargoWeightTxv, holder.cargoPriceTxv, position));
        holder.btnReduceWeight.setOnClickListener(v -> changeCargoWeight(v, holder.cargoWeightTxv, holder.cargoPriceTxv, position));

        holder.btnIncreasePrice.setOnClickListener(v -> changeCargoPrice(v, holder.tonPriceTxv, holder.cargoPriceTxv, position));
        holder.btnReducePrice.setOnClickListener(v -> changeCargoPrice(v, holder.tonPriceTxv, holder.cargoPriceTxv, position));

    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //region Поля
        public final ImageView shipImageView;

        public final LinearLayout mainLayout;

        //id
        public final TextView shipIdTxv;

        //Тип корабля
        public final TextView shipTypeTxv;

        //Тип корабля
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

        //Полная стоимость груза
        public final TextView cargoPriceTxv;

        //Признаки судна
        public final TextView shipSignsTxv;

        //Кнопки удаления и редактирования
        public final Button btnEdit;
        public final Button btnDelete;

        //Кнопки
        public final Button btnReduceWeight;
        public final Button btnIncreaseWeight;

        public final Button btnReducePrice;
        public final Button btnIncreasePrice;

        //endregion
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.shipImageView = itemView.findViewById(R.id.shipImage);
            this.mainLayout = itemView.findViewById(R.id.mainLl);
            this.shipIdTxv = itemView.findViewById(R.id.shipId);
            this.shipTypeTxv = itemView.findViewById(R.id.shipType);
            this.shipNameTxv = itemView.findViewById(R.id.shipName);
            this.loadCapacityTxv = itemView.findViewById(R.id.loadCapacity);
            this.destinationTxv = itemView.findViewById(R.id.destination);
            this.cargoTypeTxv = itemView.findViewById(R.id.cargoType);
            this.cargoWeightTxv = itemView.findViewById(R.id.cargoWeight);
            this.tonPriceTxv = itemView.findViewById(R.id.tonPrice);
            this.cargoPriceTxv = itemView.findViewById(R.id.cargoPrice);

            this.shipSignsTxv = itemView.findViewById(R.id.shipSigns);


            this.btnEdit = itemView.findViewById(R.id.btnEdit);
            this.btnDelete = itemView.findViewById(R.id.btnDelete);

            //Кнопки
            this.btnReduceWeight = itemView.findViewById(R.id.btnReduceWeight);
            this.btnIncreaseWeight = itemView.findViewById(R.id.btnIncreaseWeight);
            this.btnReducePrice = itemView.findViewById(R.id.btnReducePrice);
            this.btnIncreasePrice = itemView.findViewById(R.id.btnIncreasePrice);

        }//ctor

    }//viewHolder


    //Переход на активность редактирования
    private void startShipFormActivity(int index) {
        Intent intent = new Intent(context, ShipActivity.class);

        Ship ship = ships.get(index);

        //Передать объект корабля в активность
        intent.putExtra(Ship.class.getCanonicalName(), ship);

        //Запустить активность
        ((Activity) context).startActivityForResult(intent, Parameters.SHIP_ACTIVITY_ID);
    }

    //Удаление
    @SuppressLint("DefaultLocale")
    private void deleteShip(int index){
        int id = ships.get(index).getId();

        ships.remove(index);

        Utils.showSnackBar(listActivity,String.format("Судно с id: %d удалено",id));
        notifyItemRemoved(index);
    }

    //Изменение значений веса
    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
    private void changeCargoWeight(View btn, TextView field, TextView cargoPriceField, int index) {
        try {

            Ship ship = ships.get(index);
            //Текущее значение в поле
            int currValue = ship.getCargoWeight();

            switch (btn.getId()) {

                case R.id.btnIncreaseWeight:
                    ship.setCargoWeight(currValue + Ship.WEIGHT_DELTA);
                    field.setText(String.format("Вес груза: %s т.", Utils.numbersFormatter.format(currValue + Ship.WEIGHT_DELTA)));

                    //Пересчитать стоимость груза
                    cargoPriceField.setText((String.format("Стоимость груза: %s $",
                            Utils.numbersFormatter.format(Ship.countCargoPrice(ship.getCargoWeight(), ship.getTonPrice()))
                    )));
                    break;

                case R.id.btnReduceWeight:
                    if (currValue >= Ship.WEIGHT_DELTA) {
                        ship.setCargoWeight(currValue - Ship.WEIGHT_DELTA);
                        field.setText(String.format("Вес груза: %s т.", Utils.numbersFormatter.format(currValue - Ship.WEIGHT_DELTA)));

                        //Пересчитать стоимость груза
                        cargoPriceField.setText((String.format("Стоимость груза: %s $",
                                Utils.numbersFormatter.format(Ship.countCargoPrice(ship.getCargoWeight(), ship.getTonPrice()))
                        )));
                    }
                    break;

            }//switch

        } catch (Exception e) {
            Utils.showSnackBar(listActivity, e.getMessage());
        }//try-catch
    }

    //Изменение значений стоимости
    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
    private void changeCargoPrice(View btn, TextView field, TextView cargoPriceField, int index) {
        try {

            Ship ship = ships.get(index);

            //Текущее значение в поле
            int currValue = ship.getTonPrice();

            switch (btn.getId()) {

                case R.id.btnIncreasePrice:
                    ship.setTonPrice(currValue + Ship.PRICE_DELTA);
                    field.setText(String.format("Цена 1 тонны: %d $", currValue + Ship.PRICE_DELTA));

                    //Пересчитать стоимость груза
                    cargoPriceField.setText((String.format("Стоимость груза: %s $",
                            Utils.numbersFormatter.format(Ship.countCargoPrice(ship.getCargoWeight(), ship.getTonPrice()))
                    )));
                    break;

                case R.id.btnReducePrice:
                    if (currValue >= Ship.WEIGHT_DELTA) {
                        ship.setTonPrice(currValue - Ship.PRICE_DELTA);
                        field.setText(String.format("Цена 1 тонны: %d $", currValue - Ship.PRICE_DELTA));

                        //Пересчитать стоимость груза
                        cargoPriceField.setText((String.format("Стоимость груза: %s $",
                                Utils.numbersFormatter.format(Ship.countCargoPrice(ship.getCargoWeight(), ship.getTonPrice()))
                        )));
                    }
                    break;

            }//switch

        } catch (Exception e) {
            Utils.showSnackBar(listActivity, e.getMessage());
        }//try-catch
    }


}
