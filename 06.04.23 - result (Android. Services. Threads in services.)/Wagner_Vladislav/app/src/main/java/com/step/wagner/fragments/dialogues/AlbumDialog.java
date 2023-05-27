package com.step.wagner.fragments.dialogues;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.step.wagner.R;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.intefaces.Listener;
import com.step.wagner.models.Album;
import com.step.wagner.services.GetByIdService;

public class AlbumDialog extends DialogFragment {

    private Listener<Integer> listener;

    private TextView userIdTxv;
    private TextView titleTxv;

    //Активность
    private Activity activity;

    private BroadcastReceiver broadcastReceiver;

    public AlbumDialog(){
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        activity = getActivity();
        LayoutInflater inflater = LayoutInflater.from(activity);

        View view =  inflater.inflate(R.layout.album_details_dialog,null);

        //Получить ссылки на элементы разметки
        userIdTxv = view.findViewById(R.id.userIdTxv);
        titleTxv = view.findViewById(R.id.albumTitleTxv);


        int albumId = getArguments().getInt(Parameters.ALBUM_ID_PARAM_NAME,1);

        builder.setTitle(String.format("Альбом с id: %d",albumId))
                .setView(view)
                .setNegativeButton("Закрыть",null)
                .setCancelable(false);


        //Создать объект получателя значений из сервиса
        broadcastReceiver = createReceiver();

        //Зарегистрировать получателя
        activity.registerReceiver(broadcastReceiver,new IntentFilter(Parameters.ALBUM_DETAILS_FILTER.value1));

        //Запуск сервиса для запроса
        Intent intent = new Intent(activity.getApplicationContext(), GetByIdService.class);

        //Задать тип запроса и id
        intent.putExtra(Parameters.REQUEST_TYPE_PARAM, Parameters.ALBUM_REQUEST_NAME);
        intent.putExtra("id", albumId);

        activity.startService(intent);

        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        //Снять регистрацию получателя при закрытии диалога
        activity.unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver createReceiver(){
        return new BroadcastReceiver() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onReceive(Context context, Intent intent) {
                Album album = intent.getParcelableExtra(Album.class.getCanonicalName());

                if (album == null){
                    userIdTxv.setText("Id пользователя: ---");
                    titleTxv.setText("Название: ---");

                    return;
                }//if

                //Задать значения в поля
                userIdTxv.setText(String.format("Id пользователя: %d",album.getUserId()));
                titleTxv.setText(String.format("Название: %s",album.getTitle()));
            }
        };
    }

    //Задать обработчика извне
    public void setListener(Listener<Integer> listener){
        if (listener == null) return;

        this.listener = listener;
    }

}
