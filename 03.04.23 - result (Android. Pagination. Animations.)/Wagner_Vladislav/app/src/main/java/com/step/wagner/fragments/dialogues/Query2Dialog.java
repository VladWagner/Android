package com.step.wagner.fragments.dialogues;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.step.wagner.R;
import com.step.wagner.intefaces.Listener;

public class Query2Dialog extends DialogFragment {

    private Listener<String> listener;
    private EditText percentField;


    public Query2Dialog(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getResources().getString(R.string.query_2_param_tile))
                .setView(R.layout.query_2_dialog)
                .setPositiveButton("Задать",okClickListener)
                .setNegativeButton("Отмена",(DialogInterface dialogInterface, int i) ->listener.onOkClickListener(" "))
                .setCancelable(false);

        return builder.create();
    }

    private DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Dialog dialog = getDialog();

            percentField = dialog.findViewById(R.id.query_2_param);

            //Вызвать обработчик, заданный извне
            listener.onOkClickListener(percentField.getText().toString());

        }
    };

    //Обработчик выхода из диалога
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    //Задать обработчика извне
    public void setListener(Listener<String> listener){
        if (listener == null) return;

        this.listener = listener;
    }

}
