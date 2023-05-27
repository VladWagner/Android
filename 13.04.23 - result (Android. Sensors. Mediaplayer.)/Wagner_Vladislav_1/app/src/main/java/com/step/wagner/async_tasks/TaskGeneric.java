package com.step.wagner.async_tasks;

//Общая задача: обработка -> вывод результата

import android.os.AsyncTask;

import java.util.function.Consumer;
import java.util.function.Function;

//Входной параметр: лямбда обработки, которая возвращает лямбду для вывода результата
//Промежуточный параметр: не задаём
//Конечный параметр: лямбда для вывода
public class TaskGeneric<Entry,Medium,Final> extends AsyncTask<Entry, Medium, Final> {

    private Function<Entry[], Final> doInBackgroundCallBack;

    private Consumer<Final> onPostExecuteCallBack;
    private Consumer<Medium[]> onProgressUpdateCallBack;


    public TaskGeneric(Function<Entry[], Final> doInBackgroundCallBack,
                       Consumer<Final> onPostExecuteCallBack,
                       Consumer<Medium[]> onProgressUpdateCallBack) {
        this.doInBackgroundCallBack = doInBackgroundCallBack;
        this.onPostExecuteCallBack = onPostExecuteCallBack;
        this.onProgressUpdateCallBack = onProgressUpdateCallBack;
    }

    //Код обработки
    @Override
    protected Final doInBackground(Entry... entries) {
        return doInBackgroundCallBack.apply(entries);
    }

    //После завершениия задачи
    @Override
    protected void onPostExecute(Final aFinal) {
        super.onPostExecute(aFinal);

        onPostExecuteCallBack.accept(aFinal);
    }

    //Во время выполнения задачи
    @Override
    protected void onProgressUpdate(Medium... values) {
        super.onProgressUpdate(values);

        onProgressUpdateCallBack.accept(values);

    }
}

