package com.step.wagner.content_providers.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.step.wagner.content_providers.R;
import com.step.wagner.content_providers.adapters.PublicationAdapter;
import com.step.wagner.content_providers.adapters.SubscriptionAdapter;
import com.step.wagner.content_providers.adapters.BaseAdapter;
import com.step.wagner.content_providers.async_tasks.CommonTask;
import com.step.wagner.content_providers.async_tasks.DataBaseTask;
import com.step.wagner.content_providers.fragments.dialogues.SelectIdDialog;
import com.step.wagner.content_providers.fragments.dialogues.SubscriptionAddDialog;
import com.step.wagner.content_providers.infrastructure.Parameters;
import com.step.wagner.content_providers.infrastructure.SimpleTuple;
import com.step.wagner.content_providers.infrastructure.Utils;
import com.step.wagner.content_providers.interfaces.Listener;
import com.step.wagner.content_providers.interfaces.Receiver;
import com.step.wagner.content_providers.interfaces.SubscriptionListener;
import com.step.wagner.content_providers.models.entities.Publication;
import com.step.wagner.content_providers.models.entities.Subscription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class FragmentReceiver extends Fragment implements Receiver, Serializable {

    RecyclerView recyclerView;

    TextView titleTxv;

    Context fragmentContext;

    //Кнопка добавления записи
    private FloatingActionButton btnAdd;

    private Button btnSortById;
    private int sortType = Parameters.DESC;

    View parentView;

    //Временные коллекции
    List<Subscription> subscriptions;

    List<Publication> publications;

    public FragmentReceiver() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Загрузить разметку
        return inflater.inflate(R.layout.fragment_receiver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Получить ссылку на recyclerView
        recyclerView = view.findViewById(R.id.rcvResult);

        btnAdd = view.findViewById(R.id.btn_add);
        btnAdd.setVisibility(View.GONE);
        btnSortById = view.findViewById(R.id.btnSortById);
        btnSortById.setVisibility(View.GONE);

        //Получить ссылку на textView
        titleTxv = view.findViewById(R.id.receiverTitle);
        fragmentContext = getContext();

        parentView = getActivity().findViewById(android.R.id.content);

    } // onViewCreated


    //Запросы
    @SuppressLint("DefaultLocale")
    @Override
    public void queries(int queryNumber) {

        switch (queryNumber) {

            //Запрос 2 в пункте меню с номером 1
            case 1:
                SelectIdDialog selectIdDialog = new SelectIdDialog();

                //Заадть обработчик клика
                selectIdDialog.setListener(new Listener<Integer>() {
                    @Override
                    public void onOkClickListener(Integer... params) {
                        if (params[0] == null || params[0] == 0) {
                            titleTxv.setText("Запрос 2: идентификатор задан некорретно!");
                            Utils.showSnackBar(parentView, "Параметры заданы некорректно! Ничего не выбрано.");

                            return;
                        }//if

                        DataBaseTask<Publication> taskPublication = new DataBaseTask<>();

                        taskPublication.linkFragment(FragmentReceiver.this);

                        //Произвести запрос к БД и вывести найденный элемент
                        taskPublication.execute(() -> {

                            Publication publication = Utils.publicationsRepository.getById(params[0]);

                            String title = String.format("Запрос 2: издание с id %d %s", params[0], publication != null ? "" : "не найдено!");

                            List<Publication> foundPublication = publication != null ? new ArrayList<>(List.of(publication)) : new ArrayList<>();

                            return new SimpleTuple<>(
                                    new PublicationAdapter(fragmentContext, foundPublication),
                                    title
                            );
                        });//execute

                    }
                });//setListener

                selectIdDialog.show(getParentFragmentManager(), "select_id_dialog");
                break;

            //Запрос 2
            case 2:

                break;
        }//switch

    }

    //Вывод таблиц
    @Override
    public void tables(String entityType) {

        switch (entityType) {
            case Parameters.SUBSCRIPTIONS:

                //Кнопка выхода на активности
                Button exitBtn = parentView.findViewById(R.id.btnExit);

                if (exitBtn != null)
                    exitBtn.setVisibility(View.GONE);


                btnAdd.setVisibility(View.VISIBLE);

                btnAdd.setOnClickListener(v -> addClickHandler(Parameters.SUBSCRIPTIONS));

                //Влкючить и задать обработчик на кнопку
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    btnSortById.setVisibility(View.VISIBLE);
                    btnSortById.setOnClickListener(v -> sortByIdClickHandler(Parameters.SUBSCRIPTIONS));
                }

                DataBaseTask<Subscription> dbTask = new DataBaseTask<>();

                dbTask.linkFragment(this);

                dbTask.execute(() -> {

                    /*List<Subscription>*/
                    subscriptions = Utils.subscriptionsRepository.getAll();

                    return new SimpleTuple<>(
                            new SubscriptionAdapter(fragmentContext, subscriptions, true, getParentFragmentManager()),
                            "Таблица подписок"
                    );
                });

                break;
            case Parameters.PUBLICATIONS:

                //Отключить кнопку добавления
                if (btnAdd.getVisibility() == View.VISIBLE)
                    btnAdd.setVisibility(View.GONE);

                btnAdd.setVisibility(View.GONE);


                DataBaseTask<Publication> dbTaskPublications = new DataBaseTask<>();

                dbTaskPublications.linkFragment(this);

                dbTaskPublications.execute(() -> {

                    /*List<Publication>*/
                    publications = Utils.publicationsRepository.getAll();

                    return new SimpleTuple<>(
                            new PublicationAdapter(fragmentContext, publications),
                            "Таблица изданий"
                    );
                });

                break;
        }

    }

    @Override
    public void tables(int index) {

        switch (index) {
            case Parameters.PUBLICATIONS_TABLE_INDEX:
                tables(Parameters.PUBLICATIONS);
                break;
            case Parameters.SUBSCRIPTIONS_TABLE_INDEX:
                tables(Parameters.SUBSCRIPTIONS);
                break;
        }

    }

    //Добавление записей
    @Override
    public <T> void addInTable(T entity) {

        //Использовать switch для проверки типов не получилось

        if (entity.getClass().getName().equalsIgnoreCase(Subscription.class.getName())) {

            Utils.subscriptionsRepository.insert((Subscription) entity);

            //Получить записи после добавления
            /*List<Subscription>*/
            subscriptions = Utils.subscriptionsRepository.getAll();


            //Сортировка по убыванию
            subscriptions.sort((a1, a2) -> a2.getId() - a1.getId());


            recyclerView.setAdapter(new SubscriptionAdapter(fragmentContext, subscriptions, true, getParentFragmentManager()));
        }

    }

    //Обработчик кнопки добавления
    private void addClickHandler(String tableType) {

        //Проверка, в какую таблицу происходит добавление
        switch (tableType) {
            case Parameters.SUBSCRIPTIONS:

                SubscriptionAddDialog subscriptionAddDialog = new SubscriptionAddDialog();

                //Задать обработчик клика в диалоге
                subscriptionAddDialog.setListener(new SubscriptionListener() {

                    @Override
                    public void onOkClickListener(int id, Date dateStart, int publicationId, int duration) {

                        CommonTask task = new CommonTask();

                        //Запуск добавления в отдельной задаче
                        task.execute(
                                () -> {

                                    //Если полученные значения некорреткны
                                    if (dateStart == null || publicationId == 0 || duration == 0)
                                        return () -> {
                                            //Вывод сообщения
                                            Utils.showSnackBar(parentView, "Добавить подписку не удалось!");

                                            return null;
                                        };//if

                                    //Выбрать занчения из справочной таблицы
                                    Publication publication = Utils.publicationsRepository.getById(publicationId);

                                    Subscription subscription = new Subscription(id, publication, dateStart, duration);


                                    //Добавление и ввод в синхронной части
                                    return () -> {
                                        addInTable(subscription);

                                        Utils.showSnackBar(parentView, "Подписка добавлена успешно!");

                                        return null;
                                    };
                                }
                        );//execute

                    }
                });//setListener

                subscriptionAddDialog.show(getParentFragmentManager(), "subscription_add_dialog");

                break;
            case Parameters.PUBLICATIONS:

                break;
        }
    }

    //Обработчик клика по кнопке сортировки
    private void sortByIdClickHandler(String tableType) {

        //Проверка, в какую таблицу происходит добавление
        switch (tableType) {
            case Parameters.SUBSCRIPTIONS:

                if (sortType == Parameters.DESC) {
                    subscriptions.sort((s1, s2) -> s2.getId() - s1.getId());
                    sortType = Parameters.ASC;

                } else {
                    subscriptions.sort(Comparator.comparingInt(Subscription::getId));
                    sortType = Parameters.DESC;
                }

                recyclerView.getAdapter().notifyDataSetChanged();

                break;
            case Parameters.PUBLICATIONS:

                if (sortType == Parameters.DESC) {
                    publications.sort((p1, p2) -> p2.getId() - p1.getId());
                    sortType = Parameters.ASC;

                } else {
                    publications.sort(Comparator.comparingInt(Publication::getId));
                    sortType = Parameters.DESC;
                }

                recyclerView.getAdapter().notifyDataSetChanged();
                break;
        }
    }

    //Установка коллекции, полученной не из БД
    @Override
    public <T> void setCollection(List<T> entities, Class<T> entityType) {

        if (entityType.getName().equalsIgnoreCase(Subscription.class.getName())) {

            //Задать коллекцию в recyclerView
            recyclerView.post(() -> {
                recyclerView.setAdapter(new SubscriptionAdapter(fragmentContext, (List<Subscription>) entities, false, getParentFragmentManager()));
            });
        }
    }

    //Установить адаптер в recycler vie
    public <T> void setRecyclerViewAdapter(BaseAdapter<T> adapter) {
        if (adapter == null) return;

        recyclerView.setAdapter(adapter);
    }

    public void setTxvValue(String value) {
        if (value.isBlank())
            return;

        titleTxv.setText(value);
    }
}