package org.itstep.pd011.androiddialogpart2;

import android.app.Activity;
import android.content.DialogInterface;
import android.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


// Пример диалога с загружаемой разметкой и кнопками
public class CustomDialogFragment3 extends DialogFragment {

    // ссылка на активность, в которой будет запускаться диалог
    private Activity activity;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        activity = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder
            .setTitle("Диалоговое окно 3")
            .setIcon(R.drawable.dialog_icon)  // иконка из ресурсов нашего приложения
            .setView(R.layout.dialog3)       // добавить разметку - работает только с API21 и старше
            .setPositiveButton("ОК", onClickListener)   // для кнопок можно установить текст и обработчик
                // нажатия.
                // для кнопки "Отмена" обработчик установлен
            .setNegativeButton("Отмена", onClickListener);

        // возможна работа с элементами разметки диалога
        // при помощи getDialog().findBiewById()
        return builder.create();
    } // onCreateDialog


    // onClick
    // Слушатель события клик по кнопке диалога
    DialogInterface.OnClickListener onClickListener = (dialogInterface, id) -> {
        // определим название нажатой кнопки
        String nameButton = "нет такой кнопки";
        switch(id) {
            case Dialog.BUTTON_POSITIVE:
                nameButton = "Ок";
                break;
            case Dialog.BUTTON_NEGATIVE:
                nameButton = "Отмена";
                break;
        } // switch

        // определить состояние чек-бокса
        Dialog thisDialog = getDialog();   // ссылка на разметку
        CheckBox checkBox1 = thisDialog.findViewById(R.id.checkBox1);
        CheckBox checkBox2 = thisDialog.findViewById(R.id.checkBox2);
        CheckBox checkBox3 = thisDialog.findViewById(R.id.checkBox3);
        String chbState = (checkBox1.isChecked()?"1: установлен":"1: не установлен") + "\n" +
                (checkBox2.isChecked()?"2: установлен":"2: не установлен") + "\n"  +
                (checkBox3.isChecked()?"3: установлен":"3: не установлен");

        // определить набранный в строке ввода текст
        EditText editText = thisDialog.findViewById(R.id.editText);

        // подготовить и показть Toast
        // getBaseContext() - ссылка на активность, в которой создан диалог
        Toast toast = Toast.makeText(activity.getBaseContext(), "", Toast.LENGTH_LONG);
        toast.setText("Выбрана кнопка \"" + nameButton + "\"\n" +
            "Состояние чек-боксов:\n" + chbState + "\n" +
            "Введена строка: \"" + editText.getText() + "\""
        );
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    };
} // class CustomDialogFragment3
