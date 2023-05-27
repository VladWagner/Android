package com.step.wagner.async_tasks;

import android.os.AsyncTask;

import com.step.wagner.adapters.BaseAdapter;
import com.step.wagner.fragments.ReceiverFragment;
import com.step.wagner.infrastructure.SimpleTuple;

import java.util.function.Supplier;

//Входной параметр: лямбда обработки, которая возвращает адаптер + строку для заголовка
//Промежуточный параметр: ничего не передаём
//Конечный параметр: адаптер для recycler view + строка для заголовка
//                                                          Входной                         Промежуточный           Конечный
public class DataBaseTask<T> extends AsyncTask< Supplier< SimpleTuple<BaseAdapter<T>,String> >, Void , SimpleTuple<BaseAdapter<T>,String> > {

    ReceiverFragment fragment;


    @SafeVarargs
    @Override
    protected final SimpleTuple<BaseAdapter<T>, String> doInBackground(Supplier<SimpleTuple<BaseAdapter<T>, String>>... suppliers) {
        return suppliers[0].get();
    }

    @Override
    protected void onPostExecute(SimpleTuple<BaseAdapter<T>,String> result) {
        super.onPostExecute(result);

        if (fragment == null)
            return;

        fragment.setRecyclerViewAdapter(result.value1);
        fragment.setTxvValue(result.value2);
    }

    public void linkFragment(ReceiverFragment receiverFragment){
        this.fragment = receiverFragment;
    }
    public void unLinkFragment(){
        this.fragment = null;
    }

}
