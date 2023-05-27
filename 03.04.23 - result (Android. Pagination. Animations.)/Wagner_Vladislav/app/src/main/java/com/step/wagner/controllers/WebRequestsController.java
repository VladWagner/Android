package com.step.wagner.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.step.wagner.adapters.AlbumAdapter;
import com.step.wagner.fragments.dialogues.AlbumIdDialog;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.Api;
import com.step.wagner.models.Album;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebRequestsController {


    //Объект для обращения к серверу
    private Retrofit retrofit;

    public WebRequestsController() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    //GET-запрос 1:  https://jsonplaceholder.typicode.com/albums/
    // выводить информацию обо всех альбомах
    //Асинхронный варинат
    public void getAllAlbums(RecyclerView recyclerView, TextView fragmentTitleTxv, View view, Context fragmentContext) {

        Api api = retrofit.create(Api.class);

        Call<List<Album>> getCollectionCallBack = api.getAlbums("albums/");

        getCollectionCallBack.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                List<Album> albums = response.body();

                if (albums == null)
                    Utils.showSnackBar(view, "Прочитать альбомы не получилось!");

                fragmentTitleTxv.setText("Коллекции альбомов");
                recyclerView.setAdapter(new AlbumAdapter(fragmentContext, albums));
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Utils.showSnackBar(view, String.format("Ошибка при чтении альбомов: %s", t.getMessage()));
            }
        });

    }//getAllAlbums

    //Синхронный варинат
    public List<Album> getAllAlbums() throws Exception {

        Api api = retrofit.create(Api.class);

        Response<List<Album>> response = api.getAlbums("albums/").execute();

        return response.body();

    }//getAllAlbums

    //Получить информацию об альбоме по id

    //Асинхронный вариант
    @SuppressLint("DefaultLocale")
    public void getAlbumById(RecyclerView recyclerView, TextView fragmentTitleTxv, View view, FragmentManager fragmentManager, Context fragmentContext) {

        AlbumIdDialog albumIdDialog = new AlbumIdDialog();

        //Задать обработчик внесения параметра в диалоге
        albumIdDialog.setListener((Integer... params) -> {

            //Если заданы некорректные параметры
            if (params[0] == null || params[0] <= 0) {

                Utils.showSnackBar(view, String.format("Параметры %s! Выведен весь список альбомов.",
                        params[0] != null && params[0] == -1 ?
                                "не были заданы" : "заданы некорректно"));

                //Вывести весь список
                getAllAlbums(recyclerView, fragmentTitleTxv, view,fragmentContext);

                return;
            }

            //Отправка запроса
            Api api = retrofit.create(Api.class);

            //Задание url c параметром
            Call<Album> getAlbum = api.getAlbum(String.format("albums/%d", params[0]));

            //Запуск запроса и задание обработчиков
            getAlbum.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Album> call, Response<Album> response) {
                    Album album = response.body();

                    if (album == null) {
                        Utils.showSnackBar(view, "Прочитать альбом не получилось, выбраны все альбомы!");

                        //Вывести весь список
                        getAllAlbums(recyclerView, fragmentTitleTxv, view,fragmentContext);

                        return;
                    }

                    fragmentTitleTxv.setText(String.format("Альбом с id: %d", album.getId()));

                    //Задать запись в recyclerView
                    recyclerView.setAdapter(new AlbumAdapter(fragmentContext, new ArrayList<>(List.of(album))));
                }

                @Override
                public void onFailure(Call<Album> call, Throwable t) {
                    Utils.showSnackBar(view, String.format("Ошибка при чтении альбома: %s", t.getMessage()));
                }
            });

        });//setListener

        albumIdDialog.show(fragmentManager, "album_id_dialog");
    }//getAlbumById

    //Синхронный вариант
    @SuppressLint("DefaultLocale")
    public Album getAlbumById(int id) throws IOException {


        Api api = retrofit.create(Api.class);

        //Задание url c параметром
        Response<Album> response = api.getAlbum(String.format("albums/%d", id)).execute();

        return response.body();
    }//getAlbumById
}
