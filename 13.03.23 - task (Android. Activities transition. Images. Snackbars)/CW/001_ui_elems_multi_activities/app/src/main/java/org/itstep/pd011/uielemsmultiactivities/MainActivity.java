package org.itstep.pd011.uielemsmultiactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.itstep.pd011.uielemsmultiactivities.models.Animal;
import org.itstep.pd011.uielemsmultiactivities.models.User;

public class MainActivity extends AppCompatActivity {

    Button btnSnackbar1, btnSnackbar2, btnSnackbar3, btnExit,
            btnImageView1, btnImageView2, btnImageView3;

    // имя передаваемого в другую активность параметра
    public static final String NAME_PARAM1 = "fileName";
    public static final String NAME_PARAM2 = "age";
    public static final String NAME_PARAM3 = "weight";
    public static final String NAME_PARAM4 = "animal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSnackbar1 = findViewById(R.id.btnSnackbar1);
        btnSnackbar1.setOnClickListener(this::showSnackbar1);

        btnSnackbar2 = findViewById(R.id.btnSnackbar2);
        btnSnackbar2.setOnClickListener(this::showSnackbar2);

        btnSnackbar3 = findViewById(R.id.btnSnackbar3);
        btnSnackbar3.setOnClickListener(this::showSnackbar3);

        btnImageView1 = findViewById(R.id.btnImageView1);
        btnImageView1.setOnClickListener(this::showImageView1);

        btnImageView2 = findViewById(R.id.btnImageView2);
        btnImageView2.setOnClickListener(this::showImageView2);

        btnImageView3 = findViewById(R.id.btnImageView3);
        btnImageView3.setOnClickListener(this::showImageView3);

        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(v -> finish());
    } // onCreate


    // простейший Snackbar
    private void showSnackbar1(View view) {
        Snackbar.make(view, "Просто выводимый текст", Snackbar.LENGTH_LONG).show();
    } // showSnackbar1

    // Snackbar c кнопкой
    private void showSnackbar2(View view) {
        Snackbar snackbar = Snackbar.make(view, "Сообщение2 и подтверждение",
                Snackbar.LENGTH_INDEFINITE);

        // задать кнопку "Закрыть" и обработчик ее клика
        // !! оработчик м.б. пустым !!
        snackbar.setAction("Закрыть", v -> {
            Toast.makeText(this, "Сообщение прочитано,\nснекбар закрыт", Toast.LENGTH_SHORT).show();
        });
        snackbar.show();
    } // showSnackbar2

    // Snackbar c кнопкой: дополнительные настройки
    private void showSnackbar3(View view) {
        Snackbar snackbar = Snackbar.make(view,
                "Сообщение3 и подтверждение\nСообщение3 и подтверждение",
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Закрыть", v -> {});

        // цвет текста действия / кнопки
        snackbar.setActionTextColor(Color.CYAN);

        // фон отображения сообщений
        snackbar.setBackgroundTint(Color.DKGRAY);

        // цвет сообщения - получаем ид элемента внутренней разметки
        TextView tv = snackbar
            .getView()
            .findViewById(com.google.android.material.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();
    } // showSnackbar3


    private void showImageView1(View view) {
        // создание интента для запуска активности
        Intent intent = new Intent(this, ImageView1Activity.class);

        // передача параметра в активность - простые типы: String, int, double
        // передача - через экстрасы, пары ключ - значение
        intent.putExtra(NAME_PARAM1, "image1.jpg");
        intent.putExtra(NAME_PARAM2, 12);
        intent.putExtra(NAME_PARAM3, 28.3);

        // запуск активности
        startActivity(intent);
    } // showImageView1

    private void showImageView2(View view) {
        // создание интента для запуска активности
        Intent intent = new Intent(this, ImageView2Activity.class);

        Animal animal = new Animal("image2.jpg", 8, 12.1);
        // передача объекта
        intent.putExtra(Animal.class.getCanonicalName(), animal);

        // запуск активности
        startActivity(intent);
    } // showImageView2


    // передача объекта в активность при помощи интерфейса Serializable
    private void showImageView3(View view) {
        // создание интента для запуска активности
        Intent intent = new Intent(this, ImageView3Activity.class);

        User user = new User("image3.jpg", 18, 52.1);
        // передача объекта
        intent.putExtra(User.class.getSimpleName(), user);

        // запуск активности
        startActivity(intent);
    } // showImageView3

} // class MainActivity