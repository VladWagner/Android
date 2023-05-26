package com.step.home_work;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.step.home_work.models.Product;
import com.step.home_work.utils.Utils;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText nameField;
    private EditText amountField;
    private EditText priceField;

    private TextView sumView;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        product = new Product();

        nameField = findViewById(R.id.productName);
        amountField = findViewById(R.id.amount);
        priceField = findViewById(R.id.price);

        sumView = findViewById(R.id.sum);

        Button btnSum = findViewById(R.id.btnSum);
        Button btnGenerate = findViewById(R.id.btnGenerate);

        //Назначить обработчкики
        btnSum.setOnClickListener(this::countSum);
        btnGenerate.setOnClickListener(this::generate);

    }
    
    //Сгенерировать новые значения
    public void generate(View view){
        try{
            //Задать поля
            nameField.setText(Utils.getProdName());
            amountField.setText(String.format(Locale.UK,"%d",Utils.getRandom(1,10)));
            priceField.setText(String.format(Locale.UK,"%d",Utils.getRandom(900,4000)));

            sumView.setText("Сумма: ---");
        }
        catch (Exception e){
            showToast(e.getMessage());
        }
    }//generate


    //Вычислить сумму
    public void countSum(View view){

        try {
            //Наименование
            product.setName(nameField.getText().toString());

            //Количество товаров
            String fieldVal = amountField.getText().toString();

            if (fieldVal.isBlank())
                throw new Exception("Количество товаров некорректно!");

            product.setAmount(Integer.parseInt(fieldVal));

            //Стоимость единицы
            fieldVal = priceField.getText().toString();

            if (fieldVal.isBlank())
                throw new Exception("Стоимость единицы задана некорректно!");

            product.setPrice(Integer.parseInt(fieldVal));

            //Рассчитать сумму
            sumView.setText(String.format(Locale.UK,"Сумма: %d",product.countSum()));

            showToast("Сумма рассчитана");
        }
        catch (Exception e){
            showToast(e.getMessage());
        }
    }


    //Вывести toast
    private void showToast(String message){
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

}