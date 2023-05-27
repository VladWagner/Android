package org.itstep.pd011.uitweenanimation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // объект, на котором демонстрируем анимацию
    TextView txvExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // управляющие кнопки
        findViewById(R.id.btnAlpha).setOnClickListener(v -> startAnimation(R.anim.ui_alpha));
        findViewById(R.id.btnRotate).setOnClickListener(v -> startAnimation(R.anim.ui_rotate));
        findViewById(R.id.btnCombo).setOnClickListener(v -> startAnimation(R.anim.ui_combo));

        // объект для анимации
        txvExample = findViewById(R.id.txvExample);
    } // onCreate


    // запуск анимации прозрачности для текстового поля из ресурса, заданного
    // идентификатором
    private void startAnimation(int id) {
        // создаем объект анимации из файла anim/@id
        Animation anim = AnimationUtils.loadAnimation(this, id);

        txvExample.startAnimation(anim);
    } // startAnimation

} // class MainActivity