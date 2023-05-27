package com.step.wagner.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.step.wagner.R;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.Listener;

public class AlbumIdDialog extends DialogFragment {

    private Listener<Integer> listener;
    private EditText albumIdField;


    public AlbumIdDialog(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Задание параметра для запроса к серверу")
                .setView(R.layout.album_id_dialog)
                .setPositiveButton("Задать",okClickListener)
                .setNegativeButton("Отмена",null)
                .setCancelable(false);

        return builder.create();
    }

    private DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Dialog dialog = getDialog();

            albumIdField = dialog.findViewById(R.id.albumId);

            Integer id = Utils.tryParseInt(albumIdField.getText().toString());

            //Вызвать обработчик, заданный извне
            listener.onOkClickListener(id);

        }
    };

    //Обработчик выхода из диалога
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    //Задать обработчика извне
    public void setListener(Listener<Integer> listener){
        if (listener == null) return;

        this.listener = listener;
    }

}
