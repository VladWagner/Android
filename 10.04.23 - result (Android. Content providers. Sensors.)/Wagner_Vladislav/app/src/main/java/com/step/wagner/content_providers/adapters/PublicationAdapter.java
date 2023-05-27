package com.step.wagner.content_providers.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.step.wagner.content_providers.R;
import com.step.wagner.content_providers.models.entities.Publication;

import java.util.List;

public class PublicationAdapter extends BaseAdapter<Publication> {


    public PublicationAdapter(@NonNull Context context, @NonNull List<Publication> collection) {
        super(context, collection);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(R.layout.publication_item, listView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Publication publicationItem = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;


        holder.txvPubId.setText(String.format("Id издания: %s",            publicationItem.getId()));
        holder.txvPubType.setText(String.format("Вид издания: %s",         publicationItem.getPublicationType()));
        holder.txvPubName.setText(String.format("Наименование издания: %s",publicationItem.getPublicationName()));
        holder.txvPubIndex.setText(String.format("Индекс издания: %s",     publicationItem.getPublicationIndex()));
        holder.txvUnitPrice.setText(String.format("Стоимость единицы: %s", publicationItem.getUnitPrice()));

    }

    //ViewHolder наследуется от ViewHolder базового класса для возможности применения полиморфизма
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Id Издания
        public final TextView txvPubId;

        //Вид издания
        public final TextView txvPubType;

        //Наименование издания
        public final TextView txvPubName;

        //Индекс издания
        public final TextView txvPubIndex;

        //Стоимость единицы
        public final TextView txvUnitPrice;


        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.txvPubId = itemView.findViewById(R.id.publicationId);
            this.txvPubType = itemView.findViewById(R.id.pub_type);
            this.txvPubName = itemView.findViewById(R.id.pub_name);
            this.txvPubIndex = itemView.findViewById(R.id.pub_index);

            this.txvUnitPrice = itemView.findViewById(R.id.unit_price);

        }
    }


}
