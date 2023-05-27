package com.step.wagner.intefaces;


import com.step.wagner.models.Album;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Url;

import java.util.List;

public interface Api {

    String BASE_URL = "https://jsonplaceholder.typicode.com/";

    //Получение альбомов
    @GET
    Call<List<Album>> getAlbums(@Url String url);

    //Получение альбома
    @GET
    Call<Album> getAlbum(@Url String url);
}
