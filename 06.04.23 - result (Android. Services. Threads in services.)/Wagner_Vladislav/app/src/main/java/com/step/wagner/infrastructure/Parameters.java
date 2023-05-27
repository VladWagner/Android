package com.step.wagner.infrastructure;

public class Parameters {

    //Имя параметра для передачи номера запроса
    public static final String REQUEST_NUMBER_PARAM = "request_number";
    public static final String REQUEST_TYPE_PARAM = "request_type";

    //Intent-filter для получения коллекции записей из сервиса

    //Строковое значение + индекс элемента
    public static final SimpleTuple<String, Integer> ALBUM_DETAILS_FILTER = new SimpleTuple<>("com.step.wagner.Album_details",4);
    public static final SimpleTuple<String, Integer> USER_DETAILS_FILTER = new SimpleTuple<>("com.step.wagner.User_details",5);
    public static final String ALBUMS_FILTER = "com.step.wagner.Albums";
    public static final String COMMENTS_FILTER = "com.step.wagner.Comments";
    public static final String POSTS_FILTER = "com.step.wagner.Posts";
    public static final String USERS_FILTER = "com.step.wagner.Users";

    public static final String[] ACTIONS = new String[]{
            ALBUMS_FILTER,COMMENTS_FILTER,POSTS_FILTER,USERS_FILTER,ALBUM_DETAILS_FILTER.value1,USER_DETAILS_FILTER.value1
    };


    //Имена параметров, передаваемых в intents

    //Имена для определения типа запроса в сервисе
    public static final String ALBUM_REQUEST_NAME = "album_by_id";
    public static final String USER_REQUEST_NAME = "user_by_id";

    public static final String ALBUM_ID_PARAM_NAME = "album_id";
    public static final String USER_ID_PARAM_NAME = "user_id";

}
