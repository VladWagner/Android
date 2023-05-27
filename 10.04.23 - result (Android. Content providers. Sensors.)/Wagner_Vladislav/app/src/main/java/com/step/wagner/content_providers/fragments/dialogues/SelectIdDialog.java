package com.step.wagner.content_providers.fragments.dialogues;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.step.wagner.content_providers.R;
import com.step.wagner.content_providers.infrastructure.Utils;
import com.step.wagner.content_providers.interfaces.Listener;

public class SelectIdDialog extends DialogFragment {

    private Listener<Integer> listener;



    public SelectIdDialog(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getResources().getString(R.string.set_id_title))
                .setView(R.layout.select_by_id_dialog)
                .setPositiveButton("Задать",okClickListener)
                .setNegativeButton("Отмена",(DialogInterface dialogInterface, int i) ->listener.onOkClickListener(0))
                .setCancelable(false);

        return builder.create();
    }

    private DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Dialog dialog = getDialog();

            EditText idField = dialog.findViewById(R.id.selected_id_param);
            Integer id = Utils.tryParseInt(idField.getText().toString());

            //Вызвать обработчик, заданный извне
            listener.onOkClickListener(id != null ? id : 0);

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
