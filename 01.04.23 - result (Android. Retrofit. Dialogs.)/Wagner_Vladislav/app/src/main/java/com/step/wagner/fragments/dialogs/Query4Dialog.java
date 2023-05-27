package com.step.wagner.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.step.wagner.R;
import com.step.wagner.async_tasks.CommonTask;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.Listener;
import com.step.wagner.models.entities.Appointment;

import java.util.List;

public class Query4Dialog extends DialogFragment {

    private Listener<String> listener;
    private Spinner specialitySpinner;


    public Query4Dialog(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.query_4_dialog,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.query_2_param_tile))
                .setView(linearLayout/*R.layout.query_4_dialog*/)
                .setPositiveButton("Задать",okClickListener)
                .setNegativeButton("Отмена",(DialogInterface dialogInterface, int i) ->listener.onOkClickListener(" "))
                .setCancelable(false) ;


        CommonTask task = new CommonTask();

        specialitySpinner = linearLayout.findViewById(R.id.query_4_params);

        //Заполнить список специальностей в отдельной  задаче
        task.execute(
                ()->{
                    Utils.doctorsRepository.open();

                    List<String> specialities = Utils.doctorsRepository.getSpecialities();

                    Utils.doctorsRepository.close();

                    //Действия после обращения к БД
                    return () -> {

                        //Задать значения в выпадающий список
                        Utils.setSpinner(linearLayout.getContext(), specialitySpinner,specialities,
                                android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item);
                        return null;
                    };
                }
        );//execute

        return builder.create();
    }

    private DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Dialog dialog = getDialog();

            String selectedSpeciality = specialitySpinner.getSelectedItem().toString();

            //Вызвать обработчик, заданный извне
            listener.onOkClickListener(selectedSpeciality);

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
