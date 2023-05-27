package com.step.wagner.intefaces;

import java.util.List;

//Интерфейс для фрагмента, выводящего результаты запросов и таблицы
public interface Receiver {

    //Выполнение запроса по номеру
    void queries(int queryNumber);

    //Чтение и выводи таблиц по имени
    void tables(String tableName);

    //Добавление сущности в таблицу
    <T> void addInTable(T entity);

    //Задание собственной коллекции сущностей - нужно при чтении JSON-файла
    <T> void setCollection(List<T> entities, Class<T> entityType);

}
