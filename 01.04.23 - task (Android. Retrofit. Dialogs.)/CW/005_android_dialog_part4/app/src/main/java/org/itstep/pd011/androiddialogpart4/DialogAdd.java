package org.itstep.pd011.androiddialogpart4;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

// Добавление данных о заказчике в активность из диалога
public class DialogAdd extends DialogFragment {
    private Datable datable; // переменная для связи с главной активностью


    /*
    В этом примере активность получаем в событии onCreateDialog
    @Override // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity
    // Так как MainActivity реализует интерфейс Datable, то мы можем преобразовать контекст
    // к данному интерфейсу.
    public void onAttach(Context context){
        super.onAttach(context);

        // связь с активностью вызова
        // это важное действие - запомнить ссылку на активность
        // !!! внедрение зависимости (DI - Dependency Injection) - присваивание ссылки на объект,
        // но не создание объекта
        datable = (Datable) context;
    } // onAttach
    */

    // построить диалог с получением данных из активности и обработчиком кнопки
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // получить ссылку на вызывающую активность
        datable = (Datable) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
            .setTitle("Новый телефон")
            .setIcon(R.drawable.user_add)
            .setView(R.layout.dialog_add)   // API 21+ !!!!
            .setPositiveButton("Добавить", addClickListner)
            .setNegativeButton("Закрыть", null);
        return builder.create();
    } // onCreateDialog


    // слушатель клика по кнопке "Добавить"
    private final DialogInterface.OnClickListener addClickListner = (dialogInterface, which) -> {
        // данные для отправки в активность
        Dialog dialog = getDialog();
        EditText edtPhone = dialog.findViewById(R.id.edtPhone);
        EditText edtSurname = dialog.findViewById(R.id.edtSurname);
        EditText edtName    = dialog.findViewById(R.id.edtName);
        EditText edtAmount = dialog.findViewById(R.id.edtAmount);

        // создание заказчика из данных в полях диалога
        Customer customer = new Customer(
                edtSurname.getText().toString(),
                edtName.getText().toString(),
                Integer.parseInt(edtAmount.getText().toString())
        );

        // получить информацию о телефоне
        String phone = edtPhone.getText().toString();

        // отправка данных в активность
        datable.add(phone, customer);
    };
} // class DialogAdd
