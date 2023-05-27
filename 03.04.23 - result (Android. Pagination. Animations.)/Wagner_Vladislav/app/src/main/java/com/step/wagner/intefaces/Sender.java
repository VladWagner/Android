package com.step.wagner.intefaces;

//Интерфейс для главной активности, которая служит передатчиком номера запроса в результирующий фрагмент

public interface Sender {

    //Задать номер запроса для выполнения фрагментом
    void sendQueryNumber(int queryNumber);

    //Задать номер таблицы для вывода в фрагменте
    void sendTableNumber(int tableNumber);

}
