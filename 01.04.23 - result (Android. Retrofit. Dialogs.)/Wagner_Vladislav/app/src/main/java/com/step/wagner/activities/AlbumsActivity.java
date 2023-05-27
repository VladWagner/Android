package com.step.wagner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.step.wagner.R;
import com.step.wagner.adapters.AlbumAdapter;
import com.step.wagner.adapters.DoctorAdapter;
import com.step.wagner.async_tasks.CommonTask;
import com.step.wagner.async_tasks.DataBaseTask;
import com.step.wagner.converters.AppointmentComplexConverter;
import com.step.wagner.fragments.dialogs.AlbumIdDialog;
import com.step.wagner.fragments.dialogs.Query2Dialog;
import com.step.wagner.infrastructure.JsonHelper;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.SimpleTuple;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.intefaces.Api;
import com.step.wagner.models.Album;
import com.step.wagner.models.entities.Appointment;
import com.step.wagner.models.entities.Doctor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlbumsActivity extends AppCompatActivity {

    //RecyclerView c выводом альбомов
    private RecyclerView albumsRcv;

    //Получение всей коллекции
    private Button albumsCollectionBtn;

    //Получение информации о альбоме по id
    private Button albumsInfoBtn;

    //Заголовок активности
    private TextView activityTitleTxv;

    //Объект для обращения к серверу
    private Retrofit retrofit;

    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        albumsRcv = findViewById(R.id.rcvAlbums);
        albumsCollectionBtn = findViewById(R.id.btnAlbumsCollection);
        albumsInfoBtn = findViewById(R.id.btnAlbumInfo);
        activityTitleTxv = findViewById(R.id.albumsActivityTitle);

        currentView = this.findViewById(android.R.id.content);

        //Построить объект rentrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Задать обработчикик
        albumsCollectionBtn.setOnClickListener(v -> getAlbumsCollection());
        albumsInfoBtn.setOnClickListener(v -> getAlbumById());

        getAlbumsCollection();
    }

    //Получить коллекцию альбомов
    private void getAlbumsCollection() {

        Api api = retrofit.create(Api.class);

        Call<List<Album>> getCollectionCallBack = api.getAlbums("albums/");

        getCollectionCallBack.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                List<Album> albums = response.body();

                if (albums == null)
                    Utils.showSnackBar(currentView, "Прочитать альбомы не получилось!");

                activityTitleTxv.setText("Коллекции альбомов");
                albumsRcv.setAdapter(new AlbumAdapter(getApplicationContext(), albums));
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Utils.showSnackBar(currentView, String.format("Ошибка при чтении альбомов: %s", t.getMessage()));
            }
        });

    }//getAlbumsCollection

    //Получить информацию об альбоме по id
    @SuppressLint("DefaultLocale")
    private void getAlbumById() {
        AlbumIdDialog albumIdDialog = new AlbumIdDialog();

        //Задать обработчик задания параметра в диалоге
        albumIdDialog.setListener((Integer... params) -> {

            //Если заданы некорректные параметры
            if (params[0] == null || params[0] == 0) {

                Utils.showSnackBar(currentView, "Параметры заданы некорректно! Выведен весь список альбомов.");

                //Вывести весь список
                getAlbumsCollection();

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
                        Utils.showSnackBar(currentView, "Прочитать альбом не получилось, выбраны все альбомы!");

                        //Вывести весь список
                        getAlbumsCollection();

                        return;
                    }

                    activityTitleTxv.setText(String.format("Альбом с id: %d", album.getId()));

                    //Задать запись в recyclerView
                    albumsRcv.setAdapter(new AlbumAdapter(getApplicationContext(), new ArrayList<>(List.of(album))));
                }

                @Override
                public void onFailure(Call<Album> call, Throwable t) {
                    Utils.showSnackBar(currentView, String.format("Ошибка при чтении альбома: %s", t.getMessage()));
                }
            });

        });//setListener

        albumIdDialog.show(getSupportFragmentManager(), "album_id_dialog");
    }

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_albums_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btnAlbumsCollection:

                getAlbumsCollection();

                break;
            case R.id.btnAlbumInfo:

                getAlbumById();

                break;

            case R.id.menuBtnExit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion

}