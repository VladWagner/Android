package org.itstep.pd011.uielemsmultiactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

/*
* в активности полуаем параметры из другой активности
* */
public class ImageView1Activity extends AppCompatActivity {

    Button btnBack;
    private ImageView imageView;
    TextView txvTitle;

    // принимаемые параметры
    private String fileName;
    private int age;
    private double weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view1);

        // получение параметра из активности - из экстрасов интента
        Intent intent = getIntent();
        fileName = intent.getStringExtra(MainActivity.NAME_PARAM1);
        age      = intent.getIntExtra(MainActivity.NAME_PARAM2, 0);
        weight   = intent.getDoubleExtra(MainActivity.NAME_PARAM3, 0);

        // чтение файла изображения из assets и вывод его в ImageView
        // получить файл с изображением и поместить его в ImageView
        imageView = findViewById(R.id.imageView1) ;
        try (InputStream inputStream = getApplicationContext().getAssets().open(fileName)) {
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawable);

            // программное управление режимом масштвьирования
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } catch (IOException e){
            Snackbar snackbar = Snackbar.make(imageView, "Ошибка чтения файла изображения",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", v-> {});
            snackbar.show();
        } // try-catсh

        // вывод имени файла и других полученных параметров в TextView
        txvTitle = findViewById(R.id.txvTitle1);
        txvTitle.setText(String.format(
            Locale.UK, "Имя файла: '%s'\nВозраст, лет: %d\nВес, кг: %.3f",
            fileName, age, weight
        ));

        // выход из активности
        findViewById(R.id.btnBack1).setOnClickListener(v -> finish());
    } // onCreate
} // class ImageView1Activity