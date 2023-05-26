package org.itstep.pd011.uielemsmultiactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.itstep.pd011.uielemsmultiactivities.models.User;

import java.io.IOException;
import java.io.InputStream;

/*
 * в активности выводим картинку из папки assets
 * получаем объект через интерфейс Serializable
 * */

public class ImageView3Activity extends AppCompatActivity {

    private Button btnBack;
    private TextView txvTitle;
    private ImageView imageView;

    // принимаемый параметр
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view3);

        // получение параметра из активности - из экстрасов интента
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra(User.class.getSimpleName());

        // чтение файла изображения из assets и вывод его в ImageView
        // получить файл с изображением и поместить его в ImageView
        imageView = findViewById(R.id.imageView3) ;
        try (InputStream inputStream = getApplicationContext().getAssets().open(user.getFileName())) {
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawable);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } catch (IOException e){
            Snackbar snackbar = Snackbar.make(imageView, "Ошибка чтения файла изображения", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", v-> {});
            snackbar.show();
        } // try-catсh

        // вывод имени файла и других полученных параметров в TextView
        txvTitle = findViewById(R.id.txvTitle3);
        txvTitle.setText(user.toString());

        // выход из активности
        btnBack = findViewById(R.id.btnBack3);
        btnBack.setOnClickListener(v -> finish());
    } // onCreate

} // class ImageView3Activity