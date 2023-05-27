package org.itstep.pd011.androiddialogpart2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;

import androidx.annotation.NonNull;

// простейший диалог - только заголовок и текст
public class CustomDialogFragment1 extends DialogFragment {

    @NonNull // обработчик события создания диалога, вызывется
    // неявно из конструктора диалога
    // т.е. в клиентском коде (в месте использования диалога вызываем конструктор)
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // getActivity() - связь с вызывающей активностью
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
            .setTitle("Диалоговое окно 1")  // крупный шрфит
            .setMessage("Это основной текст, выводимый в диалог. " +
                    "Для закрытия окна нажмите кнопку \"Назад\"");
        return builder.create();
    } // class CustomDialogFragment1
} // class CustomDialogFragment1
