package org.itstep.pd011.retrofitwebdemo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

// Описание API для REST-операций веб-сервера
public interface Api {

    String BASE_URL = "https://jsonplaceholder.typicode.com/";

    // GET-запрос для получения коллекции записей от веб-сервера
    @GET("posts")
    Call<List<Post>> getPosts();

}
