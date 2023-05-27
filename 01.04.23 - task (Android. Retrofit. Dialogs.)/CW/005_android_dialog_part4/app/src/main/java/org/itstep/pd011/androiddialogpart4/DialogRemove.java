package org.itstep.pd011.androiddialogpart4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


// Выполняет удаление выбранного элемента списка из адаптера
// при помощи интерфейса, предоставляемого активностью (MainActivity)
// из которой вызывается диалоговое окно
public class DialogRemove extends DialogFragment {

    // переменная для связи с активностью
    private Datable datable;

    /*
    // без него можно обойтись :)
    @Override // Метод onAttach() вызывается в начале жизненного цикла фрагмента, и именно здесь
    // мы можем получить контекст фрагмента, в качестве которого выступает класс MainActivity
    // Так как MainActivity реализует интерфейс Datable, то мы можем преобразовать контекст
    // к данному интерфейсу.
    public void onAttach(Context context){
        super.onAttach(context);

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

		// передача информации в диалог
        Bundle bundle = getArguments();
        final String phone = bundle.getString("phone");

        // активность по-прежнему доступна в методе onCreateDialog :)
        datable = (Datable) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
            .setTitle("Подтверждение")
            .setIcon(R.drawable.user_delete)
            .setMessage(String.format("Вы хотите удалить\n%s?", phone))
            .setPositiveButton("OK", (dialog, which) -> {
                // удалить выбранный элемент списка при помощи метода интерфейса
                // phone - это данные, передаваемые в активность
                datable.remove(phone);})
            .setNegativeButton("Отмена", null);
        return builder.create();
    } // onCreateDialog
} // class DialogRemove