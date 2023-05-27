package com.step.wagner.content_providers.interfaces;

//Интерфейс для главной активности, которая служит передатчиком номера запроса в результирующий фрагмент

public interface Sender {

    //Задать номер запроса для выполнения фрагментом
    void sendQueryNumber(int queryNumber);


}
