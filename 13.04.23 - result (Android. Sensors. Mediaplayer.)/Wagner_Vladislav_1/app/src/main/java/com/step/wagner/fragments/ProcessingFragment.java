package com.step.wagner.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.step.wagner.R;
import com.step.wagner.async_tasks.TaskGeneric;
import com.step.wagner.infrastructure.SessionState;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.entities.Statistics;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ProcessingFragment extends Fragment {

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

    public ProcessingFragment() {

    }

    public static ProcessingFragment newInstance(String param1, String param2) {
        ProcessingFragment fragment = new ProcessingFragment();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View loadingView = inflater.inflate(R.layout.fragment_processing, container, false);

        txvDataAmount = loadingView.findViewById(R.id.amountTxv);

        txvAccelerometerX = loadingView.findViewById(R.id.accelerometerXTxv);
        txvAccelerometerY = loadingView.findViewById(R.id.accelerometerYTxv);
        txvAccelerometerZ = loadingView.findViewById(R.id.accelerometerZTxv);

        txvApproximation = loadingView.findViewById(R.id.approximationTxv);
        txvLight = loadingView.findViewById(R.id.lightTxv);

        txvCollectingStartTime = loadingView.findViewById(R.id.collecting_start_time_txv);
        txvHandlingTime = loadingView.findViewById(R.id.handling_time_txv);
        txvSensorsTypes = loadingView.findViewById(R.id.sensorsTypesTxv);

        return loadingView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TaskGeneric<Void, Void, Statistics> taskGeneric = new TaskGeneric<>(

                //doInBackGround
                (voidVal) -> {

                    Statistics statistics;

                    long count = Utils.statisticsRepository.getCount();
                    Utils.statisticsRepository.open();

                    //Если был произведён сбор параметров, тогда произвести выборку, в противном случае прочитать последний элемент в таблице
                    if (SessionState.isNewCollecting || count <= 0) {

                        int itemId = (int) Utils.statisticsRepository.getStatisticsFromCollecting();

                        statistics = Utils.statisticsRepository.getById(itemId);

                    }else
                        statistics = Utils.statisticsRepository.getLastAddedItem();


                    SessionState.isSessionHandled = true;
                    SessionState.isNewCollecting = false;

                    Utils.statisticsRepository.close();
                    return statistics;
                },

                //OnPostExecute
                this::fillTextViews,
                null
        );

        taskGeneric.execute();

    }

    //Заполнить поля
    @SuppressLint("DefaultLocale")
    public void fillTextViews(Statistics statistics) {
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