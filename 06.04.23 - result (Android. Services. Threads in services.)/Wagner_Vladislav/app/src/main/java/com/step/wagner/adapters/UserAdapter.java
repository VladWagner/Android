package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.step.wagner.R;
import com.step.wagner.activities.UserDetailsActivity;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.models.user.User;

import java.util.List;

public class UserAdapter extends BaseAdapter<User> {

    public UserAdapter(@NonNull Context context, @NonNull List<User> collection) {

        super(context,collection);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        View itemView = inflater.inflate(R.layout.user_item, recyclerView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        User userItem = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;

        //Задать обработчик клика по элементу списка
        holder.mainLl.setOnClickListener(v -> details(position));

        holder.userNameTxv.setText(String.format("Id пользователя: %d",userItem.getId()) );
        holder.userIdTxv.setText(String.format("Имя пользователя: %s",userItem.getName()));

        holder.usernameTxv.setText(String.format("Никнейм пользователя: %s",userItem.getUsername()) );
        holder.emailTxv.setText(String.format("e-mail: %s",userItem.getEmail()) );

        holder.webSite.setText(String.format("Сайт: %s",userItem.getWebsite()) );



    }

    //ViewHolder
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Id пользователя
        public final TextView userIdTxv;

        //Имя пользователя
        public final TextView userNameTxv;

        //Фамилия
        public final TextView usernameTxv;

        //E-mail пользователя
        public final TextView emailTxv;

        //Сайт
        public final TextView webSite;

        //Вся разметка элемента списка
        public final LinearLayout mainLl;

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.userIdTxv =    itemView.findViewById(R.id.userId);
            this.userNameTxv =   itemView.findViewById(R.id.userNameTxv);
            this.usernameTxv = itemView.findViewById(R.id.usernameTxv);
            this.emailTxv = itemView.findViewById(R.id.userEmailTxv);
            this.webSite = itemView.findViewById(R.id.userWebsite);
            this.mainLl = itemView.findViewById(R.id.mainLl);

        }
    }

    //Обработчик клика по элементу
    private void details(int index){

        User userItem = collection.get(index);

        //Вызов активности получения информации по пользватлю
        Intent intent = new Intent(context, UserDetailsActivity.class);


        //Передать объект корабля в активность
        intent.putExtra(Parameters.USER_ID_PARAM_NAME, userItem.getId());

        //Запустить активность
        context.startActivity(intent);
    }

}
