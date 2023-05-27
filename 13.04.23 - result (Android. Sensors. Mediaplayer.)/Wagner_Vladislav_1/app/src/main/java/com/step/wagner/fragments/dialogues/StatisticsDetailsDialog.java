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
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.entities.Statistics;

public class StatisticsDetailsDialog extends DialogFragment {

    //Id записи
    private TextView txvId;

    //Количество записей
    private TextView txvDataAmount;

    //Ускорение по оси X
    private TextView txvAccelerometerX;

    //Ускорение по оси Y
    private TextView txvAccelerometerY;

    //Ускорение по оси Z
    private TextView txvAccelerometerZ;

    //Датчик приближения
    private TextView txvApproximation;

    //Датчик освещённости
    private TextView txvLight;

    //Время начала сбора
    private TextView txvCollectingStartTime;

    //Время обработки
    private TextView txvHandlingTime;

    //Использованные датчики
    private TextView txvSensorsTypes;

    private Statistics statistics;

    //Активность
    private Activity activity;

    private BroadcastReceiver broadcastReceiver;

    public StatisticsDetailsDialog(){
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        activity = getActivity();
        LayoutInflater inflater = LayoutInflater.from(activity);

        View view =  inflater.inflate(R.layout.processing_history_dialog,null);

        txvId =                  view.findViewById(R.id.idStatisticsTxv);
        txvDataAmount =          view.findViewById(R.id.amountTxv);
        txvAccelerometerX =      view.findViewById(R.id.accelerometerXTxv);
        txvAccelerometerY =      view.findViewById(R.id.accelerometerYTxv);
        txvAccelerometerZ =      view.findViewById(R.id.accelerometerZTxv);
        txvApproximation =       view.findViewById(R.id.approximationTxv);
        txvLight =               view.findViewById(R.id.lightTxv);
        txvCollectingStartTime = view.findViewById(R.id.collecting_start_time_txv);
        txvHandlingTime =        view.findViewById(R.id.handling_time_txv);
        txvSensorsTypes =        view.findViewById(R.id.sensorsTypesTxv);

        Bundle arguments = getArguments();

        statistics = arguments.getParcelable("statistics");

        fillFields();

        builder/*.setTitle(String.format("Запись с id: %d",statistics.getId()))*/
                .setView(view)
                .setPositiveButton("Выйти",null)
                .setCancelable(false);

        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    //Заполнить элементы энтерфейса значениями
    @SuppressLint("DefaultLocale")
    private void fillFields(){

        if (statistics == null)
            return;

        txvId.setText(String.valueOf(statistics.getId()));
        txvDataAmount.setText(String.valueOf(statistics.getAmount()));

        //Ось X
        txvAccelerometerX.setText(String.format(" min: %.3f\n avg: %.3f\n max: %.3f"
                , statistics.getAccelerometerStatistics().getAxisX_min()
                , statistics.getAccelerometerStatistics().getAxisX_avg()
                , statistics.getAccelerometerStatistics().getAxisX_max()));

        //Ось Y
        txvAccelerometerY.setText(String.format(" min: %.3f\n avg: %.3f\n max: %.3f"
                , statistics.getAccelerometerStatistics().getAxisY_min()
                , statistics.getAccelerometerStatistics().getAxisY_avg()
                , statistics.getAccelerometerStatistics().getAxisY_max()));

        //Ось Z
        txvAccelerometerZ.setText(String.format(" min: %.3f\n avg: %.3f\n max: %.3f"
                , statistics.getAccelerometerStatistics().getAxisZ_min()
                , statistics.getAccelerometerStatistics().getAxisZ_avg()
                , statistics.getAccelerometerStatistics().getAxisZ_max()));

        //Приближение
        txvApproximation.setText(String.format(" min: %.3f\n avg: %.3f\n max: %.3f"
                , statistics.getApproximationMin()
                , statistics.getApproximationAvg()
                , statistics.getApproximationMax()));

        //Освещённость
        txvLight.setText(String.format(" min: %.3f\n avg: %.3f\n max: %.3f"
                , statistics.getLightMin()
                , statistics.getLightAvg()
                , statistics.getLightMax()));

        txvCollectingStartTime.setText(Utils.dbDateTimeFormat.format(statistics.getCollectingStartTime()));
        txvHandlingTime.setText(Utils.dbDateTimeFormat.format(statistics.getHandlingTime()));
        txvSensorsTypes.setText(statistics.getSensorsTypes());

    }

}
