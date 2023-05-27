package org.itstep.pd011.androiddialogpart2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;


// диалог с добавленными кнопками
public class CustomDialogFragment2 extends DialogFragment {

    // ссылка на текущую активность
    private Activity activity;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // запомнить ссылкук на текущую активность
        activity = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        return builder
            .setTitle("Диалоговое окно 2")
            // .setIcon(android.R.drawable.ic_dialog_alert)  // иконка из стандартных ресурсов Android
            .setIcon(R.drawable.application_add)  // иконка из ресурсов нашего приложения
            // текст диалогового окна
            .setMessage("Это текст основного сообщения диалога\nЛюбая из трех кнопок закроет диалог")
            // диалог закрывается по кнопке без обработчика
            .setPositiveButton("ОК", onClickListener) // можно задать текст и обработчик нажатия
            .setNegativeButton("Нет", (di, id) -> {}) // можно задать текст и обработчик нажатия
            .setNeutralButton("Не знаю", null) // можно задать текст и обработчик нажатия
            .create();
    } // onCreateDialog


    // Слушатель события клика по кнопке диалога
    DialogInterface.OnClickListener onClickListener =
        (DialogInterface dialogInterface, int idButton) -> {
        // формирование Toast в контексте текущей активности
        Toast toast = Toast.makeText(activity.getBaseContext(), "Выбрана кнопка \"Ок\"", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    };
} // class CustomDialogFragment2
