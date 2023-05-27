package com.step.wagner.content_providers.interfaces;

import java.util.List;

//Интерфейс для фрагмента, выводящего результаты запросов и таблицы
public interface Receiver {

    //Выполнение запроса по номеру
    void queries(int queryNumber);

    //Чтение и выводи таблиц по имени
    void tables(String tableName);
    void tables(int index);

    //Добавление сущности в таблицу
    <T> void addInTable(T entity);

    //Задание собственной коллекции сущностей - нужно при чтении JSON-файла
    <T> void setCollection(List<T> entities, Class<T> entityType);

}
