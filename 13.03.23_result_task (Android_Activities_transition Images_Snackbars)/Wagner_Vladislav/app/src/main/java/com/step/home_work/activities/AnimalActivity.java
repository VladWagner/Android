package com.step.home_work.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.step.home_work.R;
import com.step.home_work.models.Animal;
import com.step.home_work.models.Parameters;
import com.step.home_work.models.ship.Ship;
import com.step.home_work.utils.Utils;

public class AnimalActivity extends AppCompatActivity {

    private Animal animal;

    //Порода
    private EditText breedField;

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

    //Увеличение/уменьшение
    private Button btnIncreaseWeight;
    private Button btnReduceWeight;

    private Button btnIncreaseAge;
    private Button btnReduceAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        //Получить объект
        Intent intent = getIntent();
        animal = intent.getParcelableExtra(Animal.class.getCanonicalName());

        breedField =        findViewById(R.id.breed);
        animalNameField =   findViewById(R.id.animalName);
        ownerSnpField =     findViewById(R.id.ownerSnp);
        animalWeightField = findViewById(R.id.animalWeight);
        animalAgeField =    findViewById(R.id.animalAge);

        animalImageView = findViewById(R.id.animalImage);

        //Заполнить поля и картинку
        setEditFields();

        try {
            Utils.setImageView(animal.getFileName(),animalImageView,getApplicationContext());
        } catch (Exception e) {
            showSnackBar(e.getLocalizedMessage());
        }

        //Кнопки
        btnExit =  findViewById(R.id.btnExit);
        btnClean = findViewById(R.id.btnCleanAnimal);

        btnIncreaseAge =    findViewById(R.id.btnIncreaseAge);
        btnReduceAge =      findViewById(R.id.btnReduceAge);
        btnIncreaseWeight = findViewById(R.id.btnIncreaseWeight);
        btnReduceWeight =   findViewById(R.id.btnReduceWeight);

        btnExit.setOnClickListener(v -> returnFromActivity());
        btnClean.setOnClickListener(v -> cleanEditFields());

        //Обработчики на кнопки увеличения/уменьшения
        btnIncreaseAge.setOnClickListener(v -> changeAnimalAge(R.string.btn_increase));
        btnReduceAge.setOnClickListener(v -> changeAnimalAge(R.string.btn_reduce));

        btnIncreaseWeight.setOnClickListener(v -> changeAnimalWeight(R.string.btn_increase));
        btnReduceWeight.setOnClickListener(v -> changeAnimalWeight(R.string.btn_reduce));

    }

    //Изменить поле возраста животного
    @SuppressLint("DefaultLocale")
    private void changeAnimalAge(int actionType) {

        try {

            String text= animalAgeField.getText().toString();

            //Если поле полностью очищено
            if (text.isBlank()) {
                animalAgeField.setText("0");
                return;
            }

            int currentVal = Integer.parseInt(text);

            //Тип операции
            switch (actionType) {

                case R.string.btn_increase:
                    animalAgeField.setText(String.format("%d", currentVal + Animal.DELTA));
                    break;

                case R.string.btn_reduce:
                    if (currentVal >= Animal.DELTA)
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

            String text= animalWeightField.getText().toString();

            //Если поле полностью очищено
            if (text.isBlank()) {
                animalWeightField.setText("0");
                return;
            }

            int currentVal = Integer.parseInt(text);

            //Тип операции
            switch (actionType) {

                case R.string.btn_increase:
                    animalWeightField.setText(String.format("%d", currentVal + Animal.DELTA));
                    break;

                case R.string.btn_reduce:
                    if (currentVal >= Animal.DELTA)
                        animalWeightField.setText(String.format("%d", currentVal - Animal.DELTA));
                    break;
            }
        } catch (NumberFormatException e) {
            showSnackBar(e.getLocalizedMessage());
        }

    }//changeAnimalWeight

    //Задать значения в EditText
    @SuppressLint("DefaultLocale")
    private void setEditFields(){

        breedField.setText(String.format("%s",animal.getBreed()));
        animalNameField.setText(String.format("%s",animal.getName()));
        ownerSnpField.setText(String.format("%s",animal.getOwnerSnp()));
        animalWeightField.setText(String.format("%d",animal.getWeight()));
        animalAgeField.setText(String.format("%d",animal.getAge()));

    }

    //Получить значения из EditText
    private void getFromFields() throws Exception {
        try {

            //Порода
            String fieldVal = breedField.getText().toString();

            if (fieldVal.isBlank())
                throw new Exception("Порода введёна некорректно!");

            animal.setBreed(fieldVal);

            //Кличка
            fieldVal = animalNameField.getText().toString();

            if (fieldVal.isBlank())
                throw new Exception("Кличка введёна некорректно!");

            animal.setName(fieldVal);

            //ФИО владельца
            fieldVal = ownerSnpField.getText().toString();

            if (fieldVal.isBlank())
                throw new Exception("ФИО введёно некорректно!");

            animal.setOwnerSnp(fieldVal);

            //Возраст
            fieldVal = animalAgeField.getText().toString();

            if (fieldVal.isBlank())
                throw new Exception("Возраст введён некорректно!");

            animal.setAge(Integer.parseInt(fieldVal));

            //Вес
            fieldVal = animalWeightField.getText().toString();

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

        breedField.setText("");
        animalNameField.setText("");
        ownerSnpField.setText("");
        animalWeightField.setText("0");
        animalAgeField.setText("0");

    }

    //Возврать из активности
    private void returnFromActivity() {
        //Получить значения из полей ввода
        try {
            getFromFields();
        } catch (Exception e) {
            showSnackBar(e.getMessage());
            return;
        }

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

}