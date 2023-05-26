package com.step.home_work.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.step.home_work.R;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.models.ship.Ship;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.ship.ShipType;

import java.util.Locale;
import java.util.stream.Collectors;

public class ShipActivity extends AppCompatActivity {

    private Ship ship;

    private Spinner shipTypeSpinner;


    //Название судна
    private EditText shipNameField;

    //Грузоподъемность
    private EditText loadCapacityField;

    //Пункт назанчения
    private EditText destinationField;

    //Тип груза
    private Spinner cargoTypeSpinner;

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

    //Требуется дозаправка топливом
    private Switch refuelingSwt;

    //Изображение
    private ImageView shipImageView;

    //Кнопки
    private Button btnCountPrice;
    private Button btnClean;
    private Button btnExit;


    Button btnIncreaseWeight;
    Button btnReduceWeight;
    Button btnIncreasePrice;
    Button btnReducePrice;


    private Ship oldShip;
    private View currentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship);

        //Получить объект
        Intent intent = getIntent();
        ship = intent.getParcelableExtra(Ship.class.getCanonicalName());
        oldShip = ship.clone();


        //Id элемента
        ((TextView)findViewById(R.id.shipId)).setText(String.format(Locale.UK,"Id судна: %d",ship.getId()));

        shipTypeSpinner = findViewById(R.id.dropDownType);

        shipNameField = findViewById(R.id.shipName);
        loadCapacityField = findViewById(R.id.loadCapacity);
        destinationField = findViewById(R.id.destination);

        cargoTypeSpinner = findViewById(R.id.dropDownCargoType);

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

        //Текущее представление
        currentView = this.findViewById(android.R.id.content);

        //Обработчики выбора элемента в выпадающем списке
        shipTypeSpinner.setOnItemSelectedListener(listenerShipTypes);
        cargoTypeSpinner.setOnItemSelectedListener(listenerCargoTypes);

        //Обработчики кнопок
        btnCountPrice.setOnClickListener(v -> countCargoPrice());
        btnClean.setOnClickListener(v -> cleanEditFields());

        setTextWatchers();

        //Кнопки +/-
        btnIncreaseWeight = findViewById(R.id.btnIncreaseWeight);
        btnReduceWeight = findViewById(R.id.btnReduceWeight);

        btnIncreasePrice = findViewById(R.id.btnIncreasePrice);
        btnReducePrice = findViewById(R.id.btnReducePrice);

        btnIncreaseWeight.setOnClickListener(v -> changeCargoWeight(R.string.btn_increase));
        btnReduceWeight.setOnClickListener(v -> changeCargoWeight(R.string.btn_reduce));

        //Стоимость груза
        btnIncreasePrice.setOnClickListener(v -> changeCargoPrice(R.string.btn_increase));
        btnReducePrice.setOnClickListener(v -> changeCargoPrice(R.string.btn_reduce));

        btnExit.setOnClickListener(v -> returnFromActivity());

        //Задать картинку
        try {
            Utils.setImageView(ship.getFileNameFromType(), shipImageView, getApplicationContext());
        } catch (Exception e) {
            finish();
        }

    }

    //Выбор элемента в выпадающем списке типов судов
    AdapterView.OnItemSelectedListener listenerShipTypes = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> listView, View view, int position, long l) {
            try {
                String chosenType = listView.getItemAtPosition(position).toString();
                ship.setShipType(
                        new ShipType(chosenType,Utils.getShipTypeId(chosenType))
                );

                //Установить значения выпадающего списка типов грузов в зависимости от типа судна
                Utils.setSpinner(currentView.getContext(),cargoTypeSpinner, Utils.getCargoList(ship.getShipType().getTypeId()),
                        android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);

                Utils.setImageView(ship.getFileNameFromType(), shipImageView, getApplicationContext());

            } catch (Exception e) {
                showSnackBar(e.getMessage());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };//listenerShipTypes

    //Выбор элемента в выпадающем списке типов грузов
    AdapterView.OnItemSelectedListener listenerCargoTypes = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> listView, View view, int position, long l) {
            try {
                ship.setCargoType(listView.getItemAtPosition(position).toString());

            } catch (Exception e) {
                showSnackBar(e.getMessage());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };//listenerShipTypes

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
            long price = Ship.countCargoPrice(ship.getCargoWeight(),ship.getTonPrice());

            cargoPriceView.setText(String.format("Стоимость груза: %s.00 $.", Utils.numbersFormatter.format(price)));
        } catch (Exception e) {
            //Если что-то пощло не так - сбросить значения к базовым
            cargoPriceView.setText(R.string.general_price);
        }
    }

    //Задать занчения в поля
    @SuppressLint("DefaultLocale")
    private void setEditFields() {

        //Установить значения выпадающего списка типов суден
        Utils.setSpinner(this,shipTypeSpinner, Utils.shipTypes.stream().map(ShipType::getType).collect(Collectors.toList()),
                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);

        Utils.setSelected(shipTypeSpinner,ship.getShipType().getType());

        //Установить значения выпадающего списка типов грузов
        Utils.setSpinner(this,cargoTypeSpinner, Utils.getCargoList(ship.getShipType().getTypeId()),
                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);


        Utils.setSelected(cargoTypeSpinner,ship.getCargoType());

        shipNameField.setText(String.format(ship.getShipName()));
        loadCapacityField.setText(String.format("%d", ship.getLoadCapacity()));
        destinationField.setText(String.format(ship.getDestination()));


        cargoWeightField.setText(String.format("%d", ship.getCargoWeight()));
        tonPriceField.setText(String.format("%d", ship.getTonPrice()));

        //Установить switch
        anchorageSwt.setChecked(ship.isAnchorage());
        pilotSwt.setChecked(ship.isDockNeeds());
        refuelingSwt.setChecked(ship.isRefuelingNeeds());

    }

    //Сбросить поля к начальным значениям
    @SuppressLint("DefaultLocale")
    private void cleanEditFields() {

        Utils.setSelected(shipTypeSpinner,oldShip.getShipType().getType());

        loadCapacityField.setText(String.format("%d", oldShip.getLoadCapacity()));
        destinationField.setText(oldShip.getDestination());

        //Перезаписать значения в список, в зависимости от типа корабля
        Utils.setSpinner(this,cargoTypeSpinner, Utils.getCargoList(oldShip.getShipType().getTypeId()),
                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);

        Utils.setSelected(cargoTypeSpinner,ship.getCargoType());

        cargoWeightField.setText(String.format("%d", oldShip.getCargoWeight()));
        tonPriceField.setText(String.format("%d", oldShip.getTonPrice()));

        //Установить switch
        anchorageSwt.setChecked(oldShip.isAnchorage());
        pilotSwt.setChecked(oldShip.isDockNeeds());
        refuelingSwt.setChecked(oldShip.isRefuelingNeeds());


        try {
            Utils.setImageView(ship.getFileNameFromType(), shipImageView, getApplicationContext());
        } catch (Exception e) {
            showSnackBar(e.getMessage());
        }

    }

    private void getFieldsValues() throws Exception {
        try {

            ship.setAnchorage(anchorageSwt.isChecked());
            ship.setDock(pilotSwt.isChecked());
            ship.setRefueling(refuelingSwt.isChecked());



        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    //Увеличение/уменьшение веса груза
    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
    private void changeCargoWeight(int btnType) {
        try {

            if (cargoWeightField.getText().toString().isBlank())
                cargoWeightField.setText("1");

            //Текущее значение в поле
            //int currValue = ship.getCargoWeight();
            int currValue = Integer.parseInt(cargoWeightField.getText().toString());

            switch (btnType) {

                case R.string.btn_increase:
                    cargoWeightField.setText(String.format("%d", currValue + Ship.WEIGHT_DELTA));
                    break;

                case R.string.btn_reduce:
                    if (currValue >= Ship.WEIGHT_DELTA) {
                        cargoWeightField.setText(String.format("%d", currValue - Ship.WEIGHT_DELTA));
                    }
                    break;

            }//switch

        } catch (Exception e) {
            showSnackBar(e.getMessage());
        }//try-catch
    }

    //Увеличение/уменьшение стоимости груза
    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
    private void changeCargoPrice(int btnType) {
        try {

            if (tonPriceField.getText().toString().isBlank())
                tonPriceField.setText("1");

            //Текущее значение в поле
            //int currValue = ship.getTonPrice();
            int currValue = Integer.parseInt(tonPriceField.getText().toString());

            switch (btnType) {

                case R.string.btn_increase:
                    tonPriceField.setText(String.format("%d", currValue + Ship.PRICE_DELTA));
                    break;

                case R.string.btn_reduce:
                    if (currValue >= Ship.PRICE_DELTA) {
                        tonPriceField.setText(String.format("%d", currValue - Ship.PRICE_DELTA));
                    }
                    break;

            }//switch

        } catch (Exception e) {
            showSnackBar(e.getMessage());
        }//try-catch
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

    //Обработчики событий изменения полей
    private void setTextWatchers(){

        //Грузоподъемность
        TextWatcher loadCapacityWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {

                    //Если значение не задано
                    if(!Utils.isValidEditText(loadCapacityField,currentView.getContext(),
                            "Грузоподъемность не задана!",
                            (val) -> !val.isBlank())) {

                        btnExit.setEnabled(false);
                        return;
                    }

                    ship.setLoadCapacity(Integer.parseInt(loadCapacityField.getText().toString()));

                    btnExit.setEnabled(true);

                } catch (Exception e) {
                    Utils.showErrorMessage(loadCapacityField,currentView.getContext(),e.getMessage());

                    btnExit.setEnabled(false);
                }
            }
        };//weightWatcher

        //Название судна
        TextWatcher shipNameWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {

                    //Если значение не задано
                    if(!Utils.isValidEditText(shipNameField,currentView.getContext(),
                            "Название судна не задано!",
                            (val) -> !val.isBlank())) {

                        btnExit.setEnabled(false);
                        return;
                    }

                    ship.setShipName(shipNameField.getText().toString());

                    btnExit.setEnabled(true);

                } catch (Exception e) {
                    Utils.showErrorMessage(loadCapacityField,currentView.getContext(),e.getMessage());

                    btnExit.setEnabled(false);
                }
            }
        };//shipNameWatcher

        //Пункт назначения
        TextWatcher destinationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {

                    //Если значение не задано
                    if(!Utils.isValidEditText(destinationField,currentView.getContext(),
                            "Пункт назначения не задан!",
                            (val) -> !val.isBlank())) {

                        btnExit.setEnabled(false);
                        return;
                    }

                    ship.setDestination(destinationField.getText().toString());

                    btnExit.setEnabled(true);

                } catch (Exception e) {
                    Utils.showErrorMessage(destinationField,currentView.getContext(),e.getMessage());

                    btnExit.setEnabled(false);
                }
            }
        };//weightWatcher

        //Вес груза
        TextWatcher weightWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    String text = cargoWeightField.getText().toString();

                    //Если значение не задано
                    if(!Utils.isValidEditText(cargoWeightField,currentView.getContext(),
                            "Не задан вес груза!",
                            (val) -> !val.isBlank())) {

                        changeButtonsState(false);
                        cargoPriceView.setText(R.string.general_price);
                        return;
                    }

                    ship.setCargoWeight(Integer.parseInt(text));
                    //Пересчитать стоимость
                    countCargoPrice();

                    changeButtonsState(true);

                } catch (Exception e) {
                    //showSnackBar(e.getMessage());
                    Utils.showErrorMessage(cargoWeightField,currentView.getContext(),e.getMessage());

                    changeButtonsState(false);
                    cargoPriceView.setText(R.string.general_price);
                }
            }
        };//weightWatcher

        //Стоимость 1 тонны груза
        TextWatcher priceWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {


                    if(!Utils.isValidEditText(tonPriceField,currentView.getContext(),
                            "Не задана стоимость груза!",
                            (val) -> !val.isBlank())) {

                        changeButtonsState(false);
                        cargoPriceView.setText(R.string.general_price);
                        return;
                    }

                    int parsed = Integer.parseInt(tonPriceField.getText().toString());
                    ship.setTonPrice(parsed);

                    countCargoPrice();

                    changeButtonsState(true);

                } catch (Exception e) {
                    //showSnackBar(e.getMessage());
                    Utils.showErrorMessage(tonPriceField,currentView.getContext(),e.getMessage());
                    changeButtonsState(false);
                    cargoPriceView.setText(R.string.general_price);
                }
            }
        };//priceWatcher


        shipNameField.addTextChangedListener(shipNameWatcher);
        cargoWeightField.addTextChangedListener(weightWatcher);
        tonPriceField.addTextChangedListener(priceWatcher);
        loadCapacityField.addTextChangedListener(loadCapacityWatcher);
        destinationField.addTextChangedListener(destinationWatcher);
    }//setTextWatchers


    private void changeButtonsState(boolean enabled){

        btnExit.setEnabled(enabled);
        btnCountPrice.setEnabled(enabled);
    }

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.btnExitFromActivity)
            finish();

        return super.onOptionsItemSelected(item);
    }
    //endregion

}