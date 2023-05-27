package com.step.wagner.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.intefaces.Api;
import com.step.wagner.models.Album;
import com.step.wagner.models.user.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetByIdService extends Service {

    //Объект для фомрирования запросов
    Retrofit retrofit;

    int actionNameIndex;

    String LOG_TAG = "get_by_id_service";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Создание сервиса
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    } // onCreate

    //Запуск сервиса
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Получение параметров
        int id = intent.getIntExtra("id",-1);
        String requestType = intent.getStringExtra(Parameters.REQUEST_TYPE_PARAM);

        Api api = retrofit.create(Api.class);

        //Запуск запроса по типу
        switch (requestType){
            case Parameters.ALBUM_REQUEST_NAME:

                //Получить индекс в массиве строк для intent filters
                actionNameIndex = Parameters.ALBUM_DETAILS_FILTER.value2;

                sendRequest(api.getAlbumById(id),Album.class);
                break;
            case Parameters.USER_REQUEST_NAME:

                actionNameIndex = Parameters.USER_DETAILS_FILTER.value2;
                sendRequest(api.getUserById(id), User.class);
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    } // onStartCommand

    //Попытка сделать шаблонный метод
    private <T extends Parcelable> void sendRequest(Call<T> callBack, Class<T> type){

        callBack.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                T item = response.body();

                if (item == null)
                {
                    Log.d("Получить запись не удалось",LOG_TAG);
                    return;
                }

                //Отправить данные обратно в активность
                Intent intent = new Intent(Parameters.ACTIONS[actionNameIndex]);

                intent.putExtra(type.getCanonicalName(),item);

                sendBroadcast(intent);


            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.d(String.format("Ошибка при получении ответа %s", t.getMessage()),LOG_TAG);
            }
        });//enqueue

        stopSelf();
    }//sendRequest

}
