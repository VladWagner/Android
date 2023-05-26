package com.step.home_work.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.step.home_work.R;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.models.Animal;
import com.step.home_work.models.ship.Ship;
import com.step.home_work.infrastructure.Utils;

public class ShipActivity extends AppCompatActivity {

    private Ship ship;

    private RadioGroup shipTypeRg;


    //Грузоподъемность
    private EditText loadCapacityField;

    //Пункт назанчения
    private EditText destinationField;

    //Тип груза
    private EditText cargoTypeField;

    //Вес груза
    private EditText cargoWeightField;

    //Стоимость 1 тонны
    private EditText tonPriceField;

    //Поле вывода
    private TextView cargoPriceView;

    //Якорная стоянка
    private Switch anchorageSwt;

    //Требуется лоцман
    private Switch pilotSwt;

    //ЯТребуется дозаправка топливом
    private Switch refuelingSwt;

    //Изображение
    private ImageView shipImageView;

    //Кнопки
    private Button btnCountPrice;
    private Button btnClean;
    private Button btnExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship);

        //Получить объект
        Intent intent = getIntent();
        ship = intent.getParcelableExtra(Ship.class.getCanonicalName());

        shipTypeRg = findViewById(R.id.rgType);
        loadCapacityField = findViewById(R.id.loadCapacity);
        destinationField = findViewById(R.id.destination);
        cargoTypeField = findViewById(R.id.cargoType);
        cargoWeightField = findViewById(R.id.cargoWeight);
        tonPriceField = findViewById(R.id.tonPrice);
        shipImageView = findViewById(R.id.shipImage);

        anchorageSwt = findViewById(R.id.swtAnchorage);
        pilotSwt = findViewById(R.id.swtPilot);
        refuelingSwt = findViewById(R.id.swtRefueling);

        setEditFields();

        cargoPriceView = findViewById(R.id.cargoPrice);
        btnCountPrice = findViewById(R.id.btnCountPrice);
        btnClean = findViewById(R.id.btnCleanShip);
        btnExit = findViewById(R.id.btnExit);

        shipTypeRg.setOnCheckedChangeListener((group, checkedId) -> onTypeRbChanged(group, checkedId));

        Button btnIncreaseWeight = findViewById(R.id.btnIncreaseWeight);
        Button btnReduceWeight = findViewById(R.id.btnReduceWeight);

        //Обработчик событий изменения полей
        //Слушатель событий ввода
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    countCargoPrice();

                } catch (Exception e) {
                    showSnackBar(e.getMessage());
                }
            }
        };//watcher

        cargoWeightField.addTextChangedListener(watcher);
        tonPriceField.addTextChangedListener(watcher);

        //Обработчики кнопок
        btnCountPrice.setOnClickListener(v -> countCargoPrice());
        btnClean.setOnClickListener(v -> setEditFields());

        btnIncreaseWeight.setOnClickListener(v -> changeCargoWeight(R.string.btn_increase));
        btnReduceWeight.setOnClickListener(v -> changeCargoWeight(R.string.btn_reduce));

        btnExit.setOnClickListener(v -> returnFromActivity());

        //Задать картинку
        try {
            Utils.setImageView(ship.getFileNameFromType(), shipImageView, getApplicationContext());
        } catch (Exception e) {
            showSnackBar(e.getMessage());
        }

    }

    private void onTypeRbChanged(RadioGroup group, int checkedId) {
        try {
            switch (checkedId) {
                case R.id.radioBulker:
                    Utils.setImageView(Parameters.BULK, shipImageView, getApplicationContext());
                    break;
                case R.id.radioContainerShip:
                    Utils.setImageView(Parameters.CONTAINERS_CARRIER, shipImageView, getApplicationContext());
                    break;
            }
        } catch (Exception e) {
            showSnackBar(e.getMessage());
        }
    }

    //region Сохранение состояний
    @Override
    // сохранение состояния при повороте устройства
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(Ship.class.getCanonicalName(), ship);


        super.onSaveInstanceState(outState);
    } // onSaveInstanceState

    @Override
    // Восстановление значений
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ship = savedInstanceState.getParcelable(Ship.class.getCanonicalName());
        setEditFields();

        //Установить изображения
        try {
            Utils.setImageView(ship.getFileNameFromType(),shipImageView,getApplicationContext());
        } catch (Exception e) {
            finish();
        }

    } // onRestoreInstanceState
    //endregion

    //Рассчитать стоимость груза
    @SuppressLint("DefaultLocale")
    private void countCargoPrice() {

        try {
            long price = Ship.countCargoPrice(
                    Integer.parseInt(cargoWeightField.getText().toString()),
                    Integer.parseInt(tonPriceField.getText().toString())
            );

            cargoPriceView.setText(String.format("Стоимость груза: %s.00 руб.", Utils.numbersFormatter.format(price)));
        } catch (Exception e) {
            //Если что-то пощло не так - сбросить значения к базовым
            cargoPriceView.setText(R.string.general_price);
        }
    }

    //Задать занчения в поля
    @SuppressLint("DefaultLocale")
    private void setEditFields() {

        //Установить значения радиокнопок
        shipTypeRg.findViewById(R.id.radioBulker).setActivated(ship.getShipType().contains("Балкер"));
        shipTypeRg.findViewById(R.id.radioContainerShip).setActivated(ship.getShipType().contains("Контейнеровоз"));

        loadCapacityField.setText(String.format("%d", ship.getLoadCapacity()));
        destinationField.setText(String.format("%s", ship.getDestination()));
        cargoTypeField.setText(String.format("%s", ship.getCargoType()));
        cargoWeightField.setText(String.format("%d", ship.getCargoWeight()));
        tonPriceField.setText(String.format("%d", ship.getTonPrice()));

        //Установить switch
        anchorageSwt.setChecked(ship.isAnchorage());
        pilotSwt.setChecked(ship.isPilotNeeds());
        refuelingSwt.setChecked(ship.isRefuelingNeeds());

    }

    private void getFieldsValues() throws Exception {
        try {

            //Установить название
            if (shipTypeRg.findViewById(R.id.radioBulker).isActivated())
                ship.setShipType("Балкер");
            else if (shipTypeRg.findViewById(R.id.radioContainerShip).isActivated())
                ship.setShipType("Контейнеровоз");

            //Грузоподъемность корабля
            String fieldVal = loadCapacityField.getText().toString();

            if (fieldVal.isBlank())
                throw new Exception("Грузоподъемность корабля введёна некорректно!");

            ship.setLoadCapacity(Integer.parseInt(fieldVal));

            ship.setAnchorage(anchorageSwt.isChecked());
            ship.setPilot(pilotSwt.isChecked());
            ship.setRefueling(refuelingSwt.isChecked());

            //Пункт назначения
            ship.setDestination(destinationField.getText().toString());

            //Тип груза
            ship.setCargoType(cargoTypeField.getText().toString());

            //Вес груза
            fieldVal = cargoWeightField.getText().toString();

            if (fieldVal.isBlank())
                throw new Exception("Вес груза введён некорректно!");

            ship.setCargoWeight(Integer.parseInt(fieldVal));

            //Стоимость 1 тонны груза
            fieldVal = tonPriceField.getText().toString();

            if (fieldVal.isBlank())
                throw new Exception("Стоимость одной тонны груза некорректно!");

            ship.setTonPrice(Integer.parseInt(fieldVal));

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    //Увеличение/уменьшение веса груза
    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
    private void changeCargoWeight(int btnType) {
        try {

            String text = cargoWeightField.getText().toString();

            //Если поля очищены, то ввести 0
            if (text.isBlank()) {
                cargoWeightField.setText("0");
                return;
            }

            //Текущее значение в поле
            int currValue = Integer.parseInt(cargoWeightField.getText().toString());

            switch (btnType) {

                case R.string.btn_increase:
                    cargoWeightField.setText(String.format("%d", currValue + Ship.WEIGHT_DELTA));
                    break;

                case R.string.btn_reduce:
                    if (currValue >= Ship.WEIGHT_DELTA)
                        cargoWeightField.setText(String.format("%d", currValue - Ship.WEIGHT_DELTA));
                    break;

            }//switch

        } catch (Exception e) {
            showSnackBar(e.getMessage());
        }//try-catch
    }

    //Очистить поля
    private void cleanEditFields() {

        loadCapacityField.setText("");
        destinationField.setText("");
        cargoTypeField.setText("");
        cargoWeightField.setText("0");
        tonPriceField.setText("");

    }

    //Возврать из активности
    private void returnFromActivity() {
        //Получить значения из полей ввода
        try {
            getFieldsValues();
        } catch (Exception e) {
            showSnackBar(e.getMessage());
            return;
        }

        Intent intent = new Intent();

        //Задать изменённую модель
        intent.putExtra(Ship.class.getCanonicalName(), ship);

        setResult(Parameters.RESULT_OK, intent);

        //Выход из активности
        finish();

    }

    //Вывести snackBar
    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Закрыть", (v) -> {
        });

        snackbar.show();
    }
}