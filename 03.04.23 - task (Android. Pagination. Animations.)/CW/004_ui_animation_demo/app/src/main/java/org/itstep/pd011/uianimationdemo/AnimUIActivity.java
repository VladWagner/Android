package org.itstep.pd011.uianimationdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class AnimUIActivity extends AppCompatActivity {

    private TextView animText;
    private Button animButton;
    private CheckBox animCheck;
    private Switch animSwitch;

    private CheckBox cbAlpha;
    private CheckBox cbScale;
    private CheckBox cbRotate;
    private CheckBox cbTranslate;
    private CheckBox cbAuto;
    private AnimatorSet autoAnimatorSetText;
    private AnimatorSet autoAnimatorSetButton;
    private AnimatorSet autoAnimatorSetCheck;
    private AnimatorSet autoAnimatorSetSwitch;
    private AnimationSet animSet;

    private Button btnStart;

    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_ui);

        // анимируемые UI элементы
        animText = findViewById(R.id.animText);
        animButton = findViewById(R.id.animButton);
        animCheck = findViewById(R.id.animCheck);
        animSwitch = findViewById(R.id.animSwitch);

        // управление
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
            // текст
            autoAnimatorSetText = new AnimatorSet();
            autoAnimatorSetText.playTogether(setAutoAnimators(animText));
            autoAnimatorSetText.start();
            autoAnimatorSetText.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animation.start();
                }
            });

            // кнопка
            autoAnimatorSetButton = new AnimatorSet();
            autoAnimatorSetButton.setStartDelay(200);
            autoAnimatorSetButton.playTogether(setAutoAnimators(animButton));
            autoAnimatorSetButton.start();
            autoAnimatorSetButton.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animation.start();
                }
            });

            // чекбокс
            autoAnimatorSetCheck = new AnimatorSet();
            autoAnimatorSetCheck.setStartDelay(400);
            autoAnimatorSetCheck.playTogether(setAutoAnimators(animCheck));
            autoAnimatorSetCheck.start();
            autoAnimatorSetCheck.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animation.start();
                }
            });

            // свитч
            autoAnimatorSetSwitch = new AnimatorSet();
            autoAnimatorSetSwitch.setStartDelay(600);
            autoAnimatorSetSwitch.playTogether(setAutoAnimators(animSwitch));
            autoAnimatorSetSwitch.start();
            autoAnimatorSetSwitch.addListener(new AnimatorListenerAdapter() {
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
                animText.startAnimation(animSet);
                animButton.startAnimation(animSet);
                animCheck.startAnimation(animSet);
                animSwitch.startAnimation(animSet);
                isPlaying = true;
            }
        }

        updateStartButtonLabel();
    }

    private List<Animator> setAutoAnimators(View v) {
        // полет вверх
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(v, "translationY", 0f, -500f).setDuration(2000);

        // поворот еще до входа в верх. точку
        ObjectAnimator rotate = ObjectAnimator.ofFloat(v, "rotation", 0f, 180f).setDuration(400);
        rotate.setStartDelay(1200);

        // исчезание - быстро летит:)
        ObjectAnimator alphaOut = ObjectAnimator.ofFloat(v, "alpha", 1f, 0.5f).setDuration(300);
        ObjectAnimator alphaIn = ObjectAnimator.ofFloat(v, "alpha", 0.5f, 1f).setDuration(300);
        alphaOut.setStartDelay(600);
        alphaIn.setStartDelay(900);

        // летим вниз
        ObjectAnimator slideDown = ObjectAnimator.ofFloat(v, "translationY", -500f, 0f).setDuration(2000);
        slideDown.setStartDelay(2000);

        // исчезание - быстро летит:)
        ObjectAnimator alpha1Out = ObjectAnimator.ofFloat(v, "alpha", 1f, 0.5f).setDuration(300);
        ObjectAnimator alpha1In = ObjectAnimator.ofFloat(v, "alpha", 0.5f, 1f).setDuration(300);
        alpha1Out.setStartDelay(2600);
        alpha1In.setStartDelay(2900);

        // поворот еще до входа в ниж. точку
        ObjectAnimator rotateBack = ObjectAnimator.ofFloat(v, "rotation", 180f, 360f).setDuration(400);
        rotateBack.setStartDelay(3200);

        return Arrays.asList(slideUp, alphaOut, alphaIn, rotate, slideDown, alpha1Out, alpha1In, rotateBack);
    }

    public void onButton(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                if(isPlaying) {
                    stopAnimations();
                    isPlaying = false;
                    updateStartButtonLabel();
                } else {
                    startAnimation();
                }
                break;
            case R.id.btnExit:
                stopAnimations();
                finish();
                break;
        }
    }

    private void stopAnimations() {
        // чистка старой версии AnimationSet
        animText.clearAnimation();
        animButton.clearAnimation();
        animCheck.clearAnimation();
        animSwitch.clearAnimation();

        // чистка новой версии Animato(r)Set
        autoAnimatorSetText.removeAllListeners();
        autoAnimatorSetText.cancel();

        autoAnimatorSetButton.removeAllListeners();
        autoAnimatorSetButton.cancel();

        autoAnimatorSetCheck.removeAllListeners();
        autoAnimatorSetCheck.cancel();

        autoAnimatorSetSwitch.removeAllListeners();
        autoAnimatorSetSwitch.cancel();
    }

    // подпись на кнопке Старт/Стоп в завис. была ли запущена анимация
    private void updateStartButtonLabel() {
        btnStart.setText(isPlaying ? R.string.btnStop : R.string.btnStart);
    }
}
