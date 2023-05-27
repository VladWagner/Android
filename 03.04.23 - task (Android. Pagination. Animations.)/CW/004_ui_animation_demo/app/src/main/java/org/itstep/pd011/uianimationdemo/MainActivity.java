package org.itstep.pd011.uianimationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView ship;

    private CheckBox cbAlpha;
    private CheckBox cbScale;
    private CheckBox cbRotate;
    private CheckBox cbTranslate;
    private CheckBox cbAuto;
    private AnimatorSet autoAnimatorSet;
    private AnimationSet animSet;

    private Button btnStart;

    private boolean isPlaying;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // анимируемое изображение
        ship = findViewById(R.id.ship);

        // управление анимацией
        cbAlpha = findViewById(R.id.cbAlpha);
        cbScale = findViewById(R.id.cbScale);
        cbRotate = findViewById(R.id.cbRotate);
        cbTranslate = findViewById(R.id.cbTranslate);
        cbAuto = findViewById(R.id.cbAuto);
        btnStart = findViewById(R.id.btnStart);

        // запуск согласно элементам управления
        startAnimation();
    }

    public void onChecked(View v) {
        stopAnimations();

        if (v.getId() == R.id.cbAuto) {
            // для автопилота выключаем одиночные анимации
            if(cbAuto.isChecked())
                clearSingleAnimCheckboxes();
        } else {
            cbAuto.setChecked(false);
        }

        // запуск анимации согласно чекбоксам
        startAnimation();
    }

    // включает/выключает чекбоксы для одиночных анимация
    private void clearSingleAnimCheckboxes() {
        cbAlpha.setChecked(false);
        cbScale.setChecked(false);
        cbRotate.setChecked(false);
        cbTranslate.setChecked(false);
    }

    // запуск анимации согласно выбранных чекбоксам
    private void startAnimation() {
        if(cbAuto.isChecked()) {
            // полет вверх
            ObjectAnimator slideUp = ObjectAnimator.ofFloat(ship, "translationY", 0f, -500f).setDuration(2000);

            // поворот еще до входа в верх. точку
            ObjectAnimator rotate = ObjectAnimator.ofFloat(ship, "rotation", 0f, 180f).setDuration(400);
            rotate.setStartDelay(1200);

            // исчезание - быстро летит:)
            ObjectAnimator alphaOut = ObjectAnimator.ofFloat(ship, "alpha", 1f, 0.5f).setDuration(300);
            ObjectAnimator alphaIn = ObjectAnimator.ofFloat(ship, "alpha", 0.5f, 1f).setDuration(300);
            alphaOut.setStartDelay(600);
            alphaIn.setStartDelay(900);

            // летим вниз
            ObjectAnimator slideDown = ObjectAnimator.ofFloat(ship, "translationY", -500f, 0f).setDuration(2000);
            slideDown.setStartDelay(2000);

            // исчезание - быстро летит:)
            ObjectAnimator alpha1Out = ObjectAnimator.ofFloat(ship, "alpha", 1f, 0.5f).setDuration(300);
            ObjectAnimator alpha1In = ObjectAnimator.ofFloat(ship, "alpha", 0.5f, 1f).setDuration(300);
            alpha1Out.setStartDelay(2600);
            alpha1In.setStartDelay(2900);

            // поворот еще до входа в ниж. точку
            ObjectAnimator rotateBack = ObjectAnimator.ofFloat(ship, "rotation", 180f, 360f).setDuration(400);
            rotateBack.setStartDelay(3200);

            autoAnimatorSet = new AnimatorSet();
            autoAnimatorSet.playTogether(slideUp, alphaOut, alphaIn, rotate, slideDown, alpha1Out, alpha1In, rotateBack);
            autoAnimatorSet.start();

            // слышаем окончание и повторяем
            autoAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animation.start();
                }
            });

            isPlaying = true;
        } else {
            animSet = new AnimationSet(true);

            if(cbAlpha.isChecked()) {
                animSet.addAnimation(AnimationUtils.loadAnimation(this, R.anim.ship_alpha));
            }
            if(cbScale.isChecked()) {
                animSet.addAnimation(AnimationUtils.loadAnimation(this, R.anim.ship_scale));
            }
            if(cbRotate.isChecked()) {
                animSet.addAnimation(AnimationUtils.loadAnimation(this, R.anim.ship_rotate));
            }
            if(cbTranslate.isChecked()) {
                animSet.addAnimation(AnimationUtils.loadAnimation(this, R.anim.ship_translate));
            }

            if(animSet.getAnimations().size() > 0) {
                ship.startAnimation(animSet);
                isPlaying = true;
            }
        }

        updateStartButtonLabel();
    }

    public void onButton(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                if(isPlaying) {
                    stopAnimations();
                    isPlaying = false;
                } else {
                    startAnimation();
                }
                break;
            case R.id.btnUIAnim:
                stopAnimations();

                startActivity(new Intent(this, AnimUIActivity.class));
                break;
            case R.id.btnExit:
                stopAnimations();
                finish();
                break;
        }
    }

    private void stopAnimations() {
        isPlaying = false;

        // чистка старой версии AnimationSet
        ship.clearAnimation();

        // чистка новой версии Animato(r)Set
        autoAnimatorSet.removeAllListeners();
        autoAnimatorSet.cancel();

        updateStartButtonLabel();
    }

    // подпись на кнопке Старт/Стоп в завис. была ли запущена анимация
    private void updateStartButtonLabel() {
        btnStart.setText(isPlaying ? R.string.btnStop : R.string.btnStart);
    }
}