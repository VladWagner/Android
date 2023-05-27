package org.itstep.pd011.celltweenanimation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    // области для отображения анимации
    private ImageView img1, img2;

    // покадровая анимация на серии картинок
    private AnimationDrawable frameAnimation;

    // анимация одной картинки (одного объекта UI)
    private Animation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // получить первую картинку и установить анимацию
        img1 = findViewById(R.id.animationView1);
        img1.setBackgroundResource(R.drawable.rabbit_animation);

        // вторая картинка для анимации
        img2 = findViewById(R.id.animationView2);

        // определим для ImageView изображение
        img2.setImageResource(R.drawable.image);
    } // onCreate


    //region Работа с меню активности
    @Override // создание меню
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu

    @Override // выбор в методе меню
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.mniQuit:
                finish();
                break;
            case R.id.mniRabbitAnimStart:
                // получаем объект анимации
                frameAnimation = (AnimationDrawable) img1.getBackground();
                frameAnimation.start(); // запуск анимации
                break;
            case R.id.mniRabbitAnimStop:
                frameAnimation = (AnimationDrawable) img1.getBackground();
                // первый вариант для завершения анимации
                if (frameAnimation.isRunning())
                    frameAnimation.stop();
                break;
            case R.id.mniCutAnimStart:
                // создаем и запускаем анимацию
                animation = AnimationUtils.loadAnimation(this, R.anim.common_animation);
                img2.startAnimation(animation);   // запуск анимации
                break;
            case R.id.mnicutAnimStop:
                // второй вариант для завершения анимации
                img2.clearAnimation();
                break;
        } // switch

        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
} // class MainActivity