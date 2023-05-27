package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.step.wagner.R;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.Album;

import java.util.List;

public class AlbumAdapter extends BaseAdapter<Album> {

    public AlbumAdapter(@NonNull Context context, @NonNull List<Album> collection) {

        super(context,collection);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        View itemView = inflater.inflate(R.layout.album_item, recyclerView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Album albumItem = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;

        holder.userIdTxv.setText(String.format("Id пользователя: %d",albumItem.getUserId()));

        holder.albumIdTxv.setText(String.format("Id альбома: %d",albumItem.getId()) );
        holder.albumTitleId.setText(String.format("Название альбома: %s",albumItem.getTitle()) );

    }

    //ViewHolder
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Id пользователя
        public final TextView userIdTxv;

        //id альбома
        public final TextView albumIdTxv;

        //Название альбома
        public final TextView albumTitleId;

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.userIdTxv =    itemView.findViewById(R.id.userId);
            this.albumIdTxv =   itemView.findViewById(R.id.albumIdTxv);
            this.albumTitleId = itemView.findViewById(R.id.albumTitleTxv);

        }
    }


}
