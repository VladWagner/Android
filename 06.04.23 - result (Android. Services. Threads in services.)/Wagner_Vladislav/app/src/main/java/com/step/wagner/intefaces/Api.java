package com.step.wagner.intefaces;


import com.step.wagner.models.Album;
import com.step.wagner.models.Comment;
import com.step.wagner.models.Post;
import com.step.wagner.models.user.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface Api {

    String BASE_URL = "https://jsonplaceholder.typicode.com/";

    //Получение альбомов
    @GET("albums")
    Call<List<Album>> getAlbums();

    //Получение альбома
    @GET("albums/{id}")
    Call<Album> getAlbumById(@Path(value = "id") int id);

    //Получение комментариев
    @GET("comments")
    Call<List<Comment>> getComments();

    //Получение постов
    @GET("posts")
    Call<List<Post>> getPosts();

    //Получение пользователей
    @GET("users")
    Call<List<User>> getUsers();

    //Получение пользователя по id
    @GET("users/{id}")
    Call<User> getUserById(@Path(value = "id") int id);
}
