package com.step.wagner.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.intefaces.Api;
import com.step.wagner.models.Album;
import com.step.wagner.models.Comment;
import com.step.wagner.models.Post;
import com.step.wagner.models.user.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestsService extends Service {

    //Объект для фомрирования запросов
    Retrofit retrofit;

    String LOG_TAG = "requests_service";

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

        int requestNumber = intent.getIntExtra(Parameters.REQUEST_NUMBER_PARAM,1);

        Api api = retrofit.create(Api.class);

        //Запуск запроса по номеру
        switch (requestNumber){
            case 1:
                sendRequest(api.getAlbums(),requestNumber,Album.class);
                break;
            case 2:
                sendRequest(api.getComments(),requestNumber,Comment.class);
                break;
            case 3:
                sendRequest(api.getPosts(),requestNumber, Post.class);
                break;
            case 4:
                sendRequest(api.getUsers(),requestNumber, User.class);
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    } // onStartCommand

    //Попытка сделать шаблонный метод
    private <T extends Parcelable> void sendRequest(Call<List<T>> callBack, int requestNumber, Class<T> type){

        callBack.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<T>> call, Response<List<T>> response) {
                List<T> collection = response.body();

                if (collection == null)
                {
                    Log.d("Получить коллекцию не вышло",LOG_TAG);
                    return;
                }

                //Отправить данные обратно в активность
                Intent intent = new Intent(Parameters.ACTIONS[requestNumber-1]);

                //Номер запроса для определения страницы
                intent.putExtra(Parameters.REQUEST_NUMBER_PARAM, requestNumber);

                intent.putParcelableArrayListExtra(type.getCanonicalName(), (ArrayList<? extends Parcelable>) collection);

                //sendBroadcast(intent);
                LocalBroadcastManager.getInstance(RequestsService.this).sendBroadcast(intent);

            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                Log.d(String.format("Ошибка при получении ответа %s", t.getMessage()),LOG_TAG);
            }
        });//enqueue

        stopSelf();
    }//sendRequest

}
