package com.step.wagner.sensors.async_tasks;

//Общая задача: обработка -> вывод результата

import android.os.AsyncTask;

import java.util.function.Supplier;

//Входной параметр: лямбда обработки, которая возвращает лямбду для вывода результата
//Промежуточный параметр: не задаём
//Конечный параметр: лямбда для вывода
public class CommonTask extends AsyncTask<Supplier<Supplier<Void>>, Void, Supplier<Void>> {

    @Override
    protected Supplier<Void> doInBackground(Supplier<Supplier<Void>>... suppliers) {
        return suppliers[0].get();
    }

    @Override
    protected void onPostExecute(Supplier<Void> voidSupplier) {
        super.onPostExecute(voidSupplier);

        //Выполнить индицкацию после обработки
        voidSupplier.get();
    }
}
