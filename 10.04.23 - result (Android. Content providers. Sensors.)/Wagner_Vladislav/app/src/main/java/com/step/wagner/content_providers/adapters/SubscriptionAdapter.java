package com.step.wagner.content_providers.adapters;

import static com.step.wagner.content_providers.infrastructure.Utils.subscriptionsRepository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.step.wagner.content_providers.R;
import com.step.wagner.content_providers.async_tasks.CommonTask;
import com.step.wagner.content_providers.fragments.dialogues.SubscriptionEditDialog;
import com.step.wagner.content_providers.infrastructure.Utils;
import com.step.wagner.content_providers.interfaces.SubscriptionListener;
import com.step.wagner.content_providers.models.entities.Publication;
import com.step.wagner.content_providers.models.entities.Subscription;

import java.util.Date;
import java.util.List;

public class SubscriptionAdapter extends BaseAdapter<Subscription> {

    //Активность, выводящая список
    private View listParent;

    //Режим вывода списка
    private boolean changeable;

    //Менеджер для вывода диалога
    private FragmentManager fragmentManager;

    public SubscriptionAdapter(@NonNull Context context, @NonNull List<Subscription> collection, boolean isChangeable, FragmentManager fragmentManager) {
        super(context, collection);

        this.listParent = ((Activity) context).findViewById(android.R.id.content);
        this.changeable = isChangeable;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(R.layout.subsciption_item, listView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Subscription subscriptionItem = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;

        //Если выводится таблица,а не результат запроса
        if (changeable) {
            holder.subscriptionId.setText(String.format("Id подписки: %d", subscriptionItem.getId()));

            holder.editBtn.setOnClickListener(v -> update(position));
            holder.deleteBtn.setOnClickListener(v -> delete(position));

        } else {
            holder.subscriptionId.setVisibility(View.GONE);

            holder.editBtn.setVisibility(View.GONE);
            holder.deleteBtn.setVisibility(View.GONE);
        }


        holder.txvStartDate.setText(String.format("Дата начала подписки: %s", Utils.dateFormat.format(subscriptionItem.getDateStart())));
        holder.txvPubType.setText(String.format("Вид издания: %s",subscriptionItem.getPublication().getPublicationType()));
        holder.txvPubName.setText(String.format("Наименование издания: %s",subscriptionItem.getPublication().getPublicationName()));
        holder.txvPubIndex.setText(String.format("Индекс издания: %s",subscriptionItem.getPublication().getPublicationIndex()));
        holder.txvUnitPrice.setText(String.format("Стоимость единицы: %s",subscriptionItem.getPublication().getUnitPrice()));
        holder.txvDuration.setText(String.format("Длительность подписки: %s мес.",subscriptionItem.getDuration()));

    }

    //ViewHolder наследуется от ViewHolder базового класса для возможности применения полиморфизма
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Дата начала подписки
        public final TextView txvStartDate;

        //Id подписки
        public final TextView subscriptionId;

        //Вид издания
        public final TextView txvPubType;

        //Наименование издания
        public final TextView txvPubName;

        //Индекс издания
        public final TextView txvPubIndex;

        //Стоимость единицы
        public final TextView txvUnitPrice;

        //Длительностть
        public final TextView txvDuration;

        //Кнопки
        public final Button editBtn;
        //Кнопки
        public final Button deleteBtn;

        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.subscriptionId = itemView.findViewById(R.id.subscriptionId);
            this.txvStartDate = itemView.findViewById(R.id.subscriptionStartDate);
            this.txvPubType = itemView.findViewById(R.id.pub_type);
            this.txvPubName = itemView.findViewById(R.id.pub_name);
            this.txvPubIndex = itemView.findViewById(R.id.pub_index);

            this.txvUnitPrice = itemView.findViewById(R.id.unit_price);
            this.txvDuration = itemView.findViewById(R.id.duration);

            this.editBtn = itemView.findViewById(R.id.btnEdit);
            this.deleteBtn = itemView.findViewById(R.id.btnDelete);


        }
    }

    //Изменение записи
    @SuppressLint("DefaultLocale")
    private void update(int index) {
        Subscription subscription = collection.get(index);

        SubscriptionEditDialog subscriptionEditDialog = new SubscriptionEditDialog();

        Bundle bundle = new Bundle();

        bundle.putParcelable("subscription", subscription);

        subscriptionEditDialog.setArguments(bundle);

        //Обработчик клика в диалоге
        subscriptionEditDialog.setListener(new SubscriptionListener() {
            @Override
            public void onOkClickListener(int id, Date dateStart, int publicationId, int duration) {

                CommonTask task = new CommonTask();
                //Изменить запись приёма в одельной задаче
                task.execute(
                        ()->{

                            //Если полученные значения - некорректны
                            if (dateStart == null || publicationId == 0 || duration == 0)
                                return () -> {
                                    //Вывод сообщения
                                    Utils.showSnackBar(listParent, String.format("Не удалось изменить запись с id: %d", subscription.getId()));

                                    return null;
                                };


                            //Выбрать занчения из справочной таблицы
                            Publication publication = Utils.publicationsRepository.getById(publicationId);

                            Subscription editedSubscription = new Subscription(subscription.getId(),publication,dateStart,duration);

                            //Действия после обращения к БД
                            return () -> {

                                //Открыть соединение с БД
                                subscriptionsRepository.update(editedSubscription);

                                //Переписать коллекцию
                                collection.clear();
                                collection.addAll(subscriptionsRepository.getAll());


                                notifyDataSetChanged();

                                //Вывод сообщения
                                Utils.showSnackBar(listParent, String.format("Запись с id: %d успешно изменена", subscription.getId()));

                                return null;
                            };
                        }
                );//execute

            }//onOkClickListener
        });

        subscriptionEditDialog.show(fragmentManager, "appointment_edit_dialog");



    }//update

    @SuppressLint("DefaultLocale")
    private void delete(int index) {
        Subscription subscription = collection.get(index);

        //Открыть соед

        subscriptionsRepository.delete(subscription.getId());

        //Переписать коллекцию
        collection.clear();
        collection.addAll(subscriptionsRepository.getAll());


        notifyDataSetChanged();

        //Вывод сообщения
        Utils.showSnackBar(listParent, String.format("Запись с id: %d успешно удалена", subscription.getId()));

    }

}
