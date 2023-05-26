package org.itstep.pd011.uielemsmultiactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.itstep.pd011.uielemsmultiactivities.models.Animal;

import java.io.IOException;
import java.io.InputStream;

/*
 * в активности выводим картинку из папки assets
 * получаем объект через интерфейс Parcelable
* */
public class ImageView2Activity extends AppCompatActivity {

    private Button btnBack;
    private TextView txvTitle;
    private ImageView imageView;

    // принимаемый параметр
    private Animal animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view2);

        // получение параметра из активности - из экстрасов интента
        Intent intent = getIntent();
        animal = intent.getParcelableExtra(Animal.class.getCanonicalName());

        // чтение файла изображения из assets и вывод его в ImageView
        // получить файл с изображением и поместить его в ImageView
        imageView = findViewById(R.id.imageView2) ;
        try (InputStream inputStream = getApplicationContext().getAssets().open(animal.getFileName())) {
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawable);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } catch (IOException e){
            Snackbar snackbar = Snackbar.make(imageView, "Ошибка чтения файла изображения", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", v-> {});
            snackbar.show();
        } // try-catсh

        // вывод имени файла и других полученных параметров в TextView
        txvTitle = findViewById(R.id.txvTitle2);
        txvTitle.setText(animal.toString());

        // выход из активности
        btnBack = findViewById(R.id.btnBack2);
        btnBack.setOnClickListener(v -> finish());
    }
} // class ImageView2Activity