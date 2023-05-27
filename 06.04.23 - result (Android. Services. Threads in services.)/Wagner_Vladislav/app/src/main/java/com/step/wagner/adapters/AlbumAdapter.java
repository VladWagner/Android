package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.step.wagner.R;
import com.step.wagner.fragments.dialogues.AlbumDialog;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.Album;

import java.util.List;

public class AlbumAdapter extends BaseAdapter<Album> {

    //Для запуска диалога
    private FragmentManager fragmentManager;

    public AlbumAdapter(@NonNull Context context, @NonNull List<Album> collection,FragmentManager fragmentManager) {

        super(context,collection);

        this.fragmentManager = fragmentManager;
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

        holder.mainLl.setOnClickListener(v -> details(position));
    }

    //ViewHolder
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Id пользователя
        public final TextView userIdTxv;

        //id альбома
        public final TextView albumIdTxv;

        //Название альбома
        public final TextView albumTitleId;

        //Весь элемент списка
        public final LinearLayout mainLl;

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.userIdTxv =    itemView.findViewById(R.id.userId);
            this.albumIdTxv =   itemView.findViewById(R.id.albumIdTxv);
            this.albumTitleId = itemView.findViewById(R.id.albumTitleTxv);
            this.mainLl = itemView.findViewById(R.id.mainLl);

        }
    }


    //Обработчик клика по элементу
    private void details(int index){
        Album albumItem = collection.get(index);

        Bundle bundle = new Bundle();

        bundle.putInt(Parameters.ALBUM_ID_PARAM_NAME,albumItem.getId());

        //Вызов диалога получения информации по комментарию
        AlbumDialog albumDialog = new AlbumDialog();

        albumDialog.setArguments(bundle);

        albumDialog.show(fragmentManager,"album_details_dialog");

    }
}
