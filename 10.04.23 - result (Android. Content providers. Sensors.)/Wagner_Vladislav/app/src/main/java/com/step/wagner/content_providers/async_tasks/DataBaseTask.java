package com.step.wagner.content_providers.async_tasks;

import android.os.AsyncTask;

import com.step.wagner.content_providers.adapters.BaseAdapter;
import com.step.wagner.content_providers.fragments.FragmentReceiver;
import com.step.wagner.content_providers.infrastructure.SimpleTuple;

import java.util.function.Supplier;

//Входной параметр: лямбда обработки, которая возвращает адаптер + строку для заголовка
//Промежуточный параметр: ничего не передаём
//Конечный параметр: адаптер для recycler view + строка для заголовка
//                                                          Входной                         Промежуточный           Конечный
public class DataBaseTask<T> extends AsyncTask< Supplier< SimpleTuple<BaseAdapter<T>,String> >, Void , SimpleTuple<BaseAdapter<T>,String> > {

    FragmentReceiver fragment;


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

    public void linkFragment(FragmentReceiver fragmentReceiver){
        this.fragment = fragmentReceiver;
    }
    public void unLinkFragment(){
        this.fragment = null;
    }

}
