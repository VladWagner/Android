package com.step.home_work;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.step.home_work.models.Triangle;
import com.step.home_work.utils.Utils;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView sideAView;
    private TextView sideBView;
    private TextView sideCView;

    private TextView squareView;
    private TextView perimeterView;

    private Button btnSquare;
    private Button btnPerimeter;
    private Button btnGenerate;

    private Triangle triangle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        triangle = new Triangle();
        
        //Получить элементы разметки 
        sideAView = findViewById(R.id.sideA);
        sideBView = findViewById(R.id.sideB);
        sideCView = findViewById(R.id.sideC);

        setTexViews();
        
        squareView = findViewById(R.id.square);
        perimeterView = findViewById(R.id.perimeter);
        
    }
    
    //Сгенерировать новые значения
    public void generate(View view){
        double a  = Utils.getRandom(3.,20.);
        double b  = Utils.getRandom(3.,20.);
        double c  = Utils.getRandom(3.,20.);
        
        while (!Triangle.isTriangle(a,b,c)){
            a  = Utils.getRandom(3.,20.);
            b  = Utils.getRandom(3.,20.);
            c  = Utils.getRandom(3.,20.);
        }
        
        triangle.setSideA(a);
        triangle.setSideB(b);
        triangle.setSideC(c);

        setTexViews();
        showToast("Новые значения сгенерированы");

        //Обнулить текстовые поля вывода результатов
        squareView.setText("Площадь");
        perimeterView.setText("Периметр");
    }//generate


    //Вычислить площадь
    public void countSquare(View view){
        squareView.setText(String.format(Locale.UK,"Площадь: %.3f",triangle.square()));
    }

    //Вычислить периметр
    public void countPerimeter(View view){
        perimeterView.setText(String.format(Locale.UK,"Периметр: %.3f",triangle.perimeter()));
    }

    //Задать значения сторон в textViews
    private void setTexViews(){

        //Поле стороны А
        sideAView.setText(String.format(Locale.UK,"%.3f",triangle.getSideA()));

        //Поле стороны B
        sideBView.setText(String.format(Locale.UK,"%.3f",triangle.getSideB()));

        //Поле стороны C
        sideCView.setText(String.format(Locale.UK,"%.3f",triangle.getSideC()));
    }

    //Вывести toast
    private void showToast(String message){
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

}