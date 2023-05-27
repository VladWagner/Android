package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.step.wagner.R;
import com.step.wagner.models.Comment;

import java.util.List;

public class CommentAdapter extends BaseAdapter<Comment> {

    public CommentAdapter(@NonNull Context context, @NonNull List<Comment> collection) {

        super(context,collection);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        View itemView = inflater.inflate(R.layout.comment_item, recyclerView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Comment commentItem = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;

        //Задать обработчик клика по элементу списка
        holder.mainLl.setOnClickListener(v -> details(position));

        holder.commentIdTxv.setText(String.format("Id комментария: %d",commentItem.getId()) );
        holder.postIdTxv.setText(String.format("Id поста: %d",commentItem.getPostId()));

        holder.commentNameTxv.setText(String.format("Заголовок: %s",commentItem.getName()) );
        holder.emailTxv.setText(String.format("e-mail: %s",commentItem.getEmail()) );

        holder.bodyTxv.setText(String.format("Содержание: %s",commentItem.getBody()) );



    }

    //ViewHolder
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Id поста
        public final TextView postIdTxv;

        //id комментария
        public final TextView commentIdTxv;

        //Заголовок комментария
        public final TextView commentNameTxv;

        //E-mail пользователя
        public final TextView emailTxv;

        //Содержание комментария
        public final TextView bodyTxv;

        //Вся разметка элемента списка
        public final LinearLayout mainLl;

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.postIdTxv =    itemView.findViewById(R.id.postId);
            this.commentIdTxv =   itemView.findViewById(R.id.commentId);
            this.commentNameTxv = itemView.findViewById(R.id.commentNameTxv);
            this.emailTxv = itemView.findViewById(R.id.commentEmailTxv);
            this.bodyTxv = itemView.findViewById(R.id.commentBodyTxv);
            this.mainLl = itemView.findViewById(R.id.mainLl);

        }
    }

    //Обработчик клика по элементу
    private void details(int index){

        //Вызов диалога получения информации по комментарию

    }

}
