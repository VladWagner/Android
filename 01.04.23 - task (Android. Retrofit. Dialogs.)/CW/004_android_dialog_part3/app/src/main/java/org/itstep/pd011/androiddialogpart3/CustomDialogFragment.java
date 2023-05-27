package org.itstep.pd011.androiddialogpart3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


// диалог для приема данных из активности
public class CustomDialogFragment extends DialogFragment {

    /* прием данных в диалоговом окне из активности */
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // прочитать данные, переданные из активности (из точки вызова)
        // getArguments() - получить параметры из активности
        Bundle bundle = getArguments();

        // читаем строковый параметр
        String phone = bundle.getString("phone");

        // читаем сложный параметр - Parcelable-объект
        Customer customer = bundle.getParcelable("customer");

        // есть возможность получить ссылку активности на которой
        // вызыввается диалог
        Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder
            .setTitle("Подтверждение")
            .setIcon(R.drawable.user_astronaut)
            .setMessage("Вы выбрали телефон:\n" + phone + "\n\nПользователь: " + customer)
            //.setNegativeButton("Отмена", null)
            .setPositiveButton("OK", null);

        AlertDialog alertDialog = builder.create();

        View view = alertDialog.getCurrentFocus();

        return builder.create();
    } // onCreateDialog
} // class CustomDialogFragment