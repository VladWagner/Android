package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.step.wagner.R;
import com.step.wagner.models.Post;

import java.util.List;

public class PostAdapter extends BaseAdapter<Post> {

    public PostAdapter(@NonNull Context context, @NonNull List<Post> collection) {

        super(context,collection);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        View itemView = inflater.inflate(R.layout.post_item, recyclerView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Post postItem = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;


        holder.titleTxv.setText(String.format("Id поста: %d",postItem.getId()) );
        holder.postIdTxv.setText(String.format("Заголовок: %s",postItem.getTitle()));

        holder.bodyTxv.setText(String.format("Текст: %s",postItem.getBody()) );

    }

    //ViewHolder
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Id поста
        public final TextView postIdTxv;

        //id комментария
        public final TextView titleTxv;

        //Заголовок комментария
        public final TextView bodyTxv;

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.postIdTxv =    itemView.findViewById(R.id.postId);
            this.titleTxv =   itemView.findViewById(R.id.titleTxv);
            this.bodyTxv = itemView.findViewById(R.id.bodyTxv);

        }
    }


}
