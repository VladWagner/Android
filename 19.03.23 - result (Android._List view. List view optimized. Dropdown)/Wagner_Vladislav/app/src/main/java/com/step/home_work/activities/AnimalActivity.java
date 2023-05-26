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
import com.step.home_work.models.Animal;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;

import java.util.Locale;
import java.util.stream.Collectors;

public class AnimalActivity extends AppCompatActivity {

    private Animal animal;

    //Порода
    private Spinner breedSpinner;

    //Кличка
    private EditText animalNameField;

    //ФИО владельца
    private EditText ownerSnpField;

    //Вес
    private EditText animalWeightField;

    //Возраст
    private EditText animalAgeField;

    //Изображение
    private ImageView animalImageView;

    //Кнопки
    private Button btnClean;
    private Button btnExit;

    //Специальная диета
    private Switch dietSwt;

    //Специальный уход
    private Switch specialCareSwt;

    //Увеличение/уменьшение
    private Button btnIncreaseWeight;
    private Button btnReduceWeight;

    private Button btnIncreaseAge;
    private Button btnReduceAge;

    private View currentView;
    private Animal old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        //Получить объект
        Intent intent = getIntent();
        animal = intent.getParcelableExtra(Animal.class.getCanonicalName());
        old = animal.clone();

        breedSpinner =        findViewById(R.id.breedSpinner);
        animalNameField =   findViewById(R.id.animalName);
        ownerSnpField =     findViewById(R.id.ownerSnp);
        animalWeightField = findViewById(R.id.animalWeight);
        animalAgeField =    findViewById(R.id.animalAge);
        dietSwt =           findViewById(R.id.swtDiet);
        specialCareSwt =    findViewById(R.id.swtCare);

        animalImageView =   findViewById(R.id.animalImage);

        //Id элемента
        ((TextView)findViewById(R.id.animalId)).setText(String.format(Locale.UK,"Id животного: %d",animal.getId()));

        //Заполнить поля и картинку
        setEditFields();

        try {
            Utils.setImageView(animal.getFileName(),animalImageView,getApplicationContext());
        } catch (Exception e) {
            showSnackBar(e.getLocalizedMessage());
        }

        //Текущее представление
        currentView = this.findViewById(android.R.id.content);

        //Кнопки
        btnExit =  findViewById(R.id.btnExit);
        btnClean = findViewById(R.id.btnCleanAnimal);

        btnIncreaseAge =    findViewById(R.id.btnIncreaseAge);
        btnReduceAge =      findViewById(R.id.btnReduceAge);
        btnIncreaseWeight = findViewById(R.id.btnIncreaseWeight);
        btnReduceWeight =   findViewById(R.id.btnReduceWeight);

        //Обработчик выбора в выпадающем списке
        breedSpinner.setOnItemSelectedListener(listenerBreedsSpinner);

        btnExit.setOnClickListener(v -> returnFromActivity());
        btnClean.setOnClickListener(v -> cleanEditFields());
        dietSwt.setOnCheckedChangeListener((button, result) -> animal.setDiet(result));
        specialCareSwt.setOnCheckedChangeListener((button, result) -> animal.setSpecialCare(result));

        setTextWatchers();

        //Обработчики на кнопки увеличения/уменьшения
        btnIncreaseAge.setOnClickListener(v -> changeAnimalAge(R.string.btn_increase));
        btnReduceAge.setOnClickListener(v -> changeAnimalAge(R.string.btn_reduce));

        btnIncreaseWeight.setOnClickListener(v -> changeAnimalWeight(R.string.btn_increase));
        btnReduceWeight.setOnClickListener(v -> changeAnimalWeight(R.string.btn_reduce));

    }//onCreate

    //Выбор элемента в выпадающем списке пород
    AdapterView.OnItemSelectedListener listenerBreedsSpinner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> listView, View view, int position, long l) {
            try {
                animal.setBreed(listView.getItemAtPosition(position).toString());

                Utils.setImageView(animal.getFileName(),animalImageView,getApplicationContext());
            } catch (Exception e) {
                showSnackBar(e.getMessage());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };//listenerShipTypes

    //Изменить поле возраста животного
    @SuppressLint("DefaultLocale")
    private void changeAnimalAge(int actionType) {

        try {

            if (animalAgeField.getText().toString().isBlank())
                animalAgeField.setText("1");
            int currentVal = Integer.parseInt(animalAgeField.getText().toString());

            //Тип операции
            switch (actionType) {

                case R.string.btn_increase:
                    animalAgeField.setText(String.format("%d", currentVal + Animal.DELTA));
                    break;

                case R.string.btn_reduce:
                    if (currentVal > Animal.DELTA)
                        animalAgeField.setText(String.format("%d", currentVal - Animal.DELTA));
                    break;
            }
        } catch (NumberFormatException e) {
            showSnackBar(e.getLocalizedMessage());
        }

    }//changeAnimalAge

    //Изменить поле веса животного
    @SuppressLint("DefaultLocale")
    private void changeAnimalWeight(int actionType) {

        try {

            if (animalWeightField.getText().toString().isBlank())
                animalWeightField.setText("1");

            int currentVal = Integer.parseInt(animalWeightField.getText().toString());

            //Тип операции
            switch (actionType) {

                case R.string.btn_increase:
                    animalWeightField.setText(String.format("%d", currentVal + Animal.DELTA));
                    break;

                case R.string.btn_reduce:
                    if (currentVal > Animal.DELTA)
                        animalWeightField.setText(String.format("%d", currentVal - Animal.DELTA));
                    break;
            }
        } catch (NumberFormatException e) {
            //showSnackBar(e.getLocalizedMessage());
            Utils.showErrorMessage(animalWeightField,currentView.getContext(),"Ошибка получения значений!");
        }

    }//changeAnimalWeight

    //Задать значения в EditText
    @SuppressLint("DefaultLocale")
    private void setEditFields(){

        Utils.setSpinner(this,breedSpinner,Utils.breeds.stream().map(b -> b.value1).collect(Collectors.toList()),
                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);

        Utils.setSelected(breedSpinner,animal.getBreed());

        animalNameField.setText(String.format("%s",animal.getName()));
        ownerSnpField.setText(String.format("%s",animal.getOwnerSnp()));
        animalWeightField.setText(String.format("%.2f",animal.getWeight()));
        animalAgeField.setText(String.format("%d",animal.getAge()));

        dietSwt.setChecked(animal.isDiet());
        specialCareSwt.setChecked(animal.isSpecialCare());

    }

    //Получить значения из EditText
    private void getFromFields() throws Exception {
        try {

            //Кличка
            animal.setName(animalNameField.getText().toString());

            //ФИО владельца

            animal.setOwnerSnp(ownerSnpField.getText().toString());

            //Возраст

            animal.setAge(Integer.parseInt(animalAgeField.getText().toString()));

            //Вес
            String fieldVal = animalWeightField.getText().toString();

            if (fieldVal.isBlank())
                throw new Exception("Вес введён некорректно!");

            animal.setWeight(Integer.parseInt(fieldVal));


        }catch (Exception e){
            throw new Exception(e);
        }
    }

    //Очистить поля
    @SuppressLint("DefaultLocale")
    private void cleanEditFields() {

        Utils.setSpinner(this,breedSpinner,Utils.breeds.stream().map(b -> b.value1).collect(Collectors.toList()),
                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);

        Utils.setSelected(breedSpinner,old.getBreed());

        animalNameField.setText(String.format("%s",old.getName()));
        ownerSnpField.setText(String.format("%s",old.getOwnerSnp()));
        animalWeightField.setText(String.format(Locale.UK,"%.2f",old.getWeight()));
        animalAgeField.setText(String.format("%d",old.getAge()));

        dietSwt.setChecked(old.isDiet());
        specialCareSwt.setChecked(old.isSpecialCare());

    }

    //Возврать из активности
    private void returnFromActivity() {
        //Получить значения из полей ввода

        Intent intent = new Intent();

        //Задать изменённую модель
        intent.putExtra(Animal.class.getCanonicalName(), animal);

        setResult(Parameters.RESULT_OK, intent);

        //Вывход из активности
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

        int weightFieldId = 1;
        int ageFieldId = 2;

        //Кличка
        TextWatcher nameWatcher = new TextWatcher() {
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
                    if(!Utils.isValidEditText(animalNameField,currentView.getContext(),
                            "Кличка животного не задана!",
                            (val) -> !val.isBlank())) {

                        btnExit.setEnabled(false);
                        return;
                    }

                    animal.setName(animalNameField.getText().toString());

                    btnExit.setEnabled(true);

                } catch (Exception e) {
                    Utils.showErrorMessage(animalNameField,currentView.getContext(),e.getMessage());

                    btnExit.setEnabled(false);
                }
            }
        };//nameWatcher

        //ФИО владельца
        TextWatcher ownerSnpWatcher = new TextWatcher() {
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
                    if(!Utils.isValidEditText(ownerSnpField,currentView.getContext(),
                            "ФИО владельца не задано!",
                            (val) -> !val.isBlank())) {

                        btnExit.setEnabled(false);
                        return;
                    }

                    animal.setOwnerSnp(ownerSnpField.getText().toString());

                    btnExit.setEnabled(true);

                } catch (Exception e) {
                    Utils.showErrorMessage(ownerSnpField,currentView.getContext(),e.getMessage());

                    btnExit.setEnabled(false);
                }
            }
        };//ownerSnpWatcher

        //Вес животного
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
                    String text = animalWeightField.getText().toString();

                    //Если значение не задано
                    if(!Utils.isValidEditText(animalWeightField,currentView.getContext(),
                            "Вес животного не задан!",
                            (val) -> !val.isBlank())) {

                        changeButtonsState(weightFieldId,false);
                        return;
                    }

                    animal.setWeight(Double.parseDouble(text));

                    //Активировать кнопки
                    changeButtonsState(weightFieldId,true);

                } catch (Exception e) {
                    //showSnackBar(e.getMessage());
                    Utils.showErrorMessage(animalWeightField,currentView.getContext(),e.getLocalizedMessage());

                    changeButtonsState(weightFieldId,true);
                }
            }
        };//weightWatcher

        //Возраст животного
        TextWatcher ageWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {


                    if(!Utils.isValidEditText(animalAgeField,currentView.getContext(),
                            "Возраст животного не задан!",
                            (val) -> !val.isBlank())) {

                        changeButtonsState(ageFieldId,false);
                        return;
                    }

                    animal.setAge(Integer.parseInt(animalAgeField.getText().toString()));

                    changeButtonsState(ageFieldId,true);

                } catch (Exception e) {
                    //showSnackBar(e.getMessage());
                    Utils.showErrorMessage(animalAgeField,currentView.getContext(),e.getMessage());
                    changeButtonsState(ageFieldId,false);
                }
            }
        };//ageWatcher

        animalNameField.addTextChangedListener(nameWatcher);
        ownerSnpField.addTextChangedListener(ownerSnpWatcher);
        animalWeightField.addTextChangedListener(weightWatcher);
        animalAgeField.addTextChangedListener(ageWatcher);
    }//setTextWatchers

    private void changeButtonsState(int fieldId, boolean enabled){
        btnExit.setEnabled(enabled);
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