package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ActionBarContainer;
import androidx.fragment.app.FragmentManager;

import com.step.wagner.R;
import com.step.wagner.fragments.dialogues.MessageDialog;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.Message;
import com.step.wagner.services.MainService;

import java.io.BufferedReader;
import java.util.List;
import java.util.function.Consumer;

public class MessageAdapter extends BaseAdapter<Message> {


    MainService service;

    FragmentManager fragmentManager;

    //Активность, выводящая список
    private View parentView;

    public MessageAdapter(@NonNull Context context, @NonNull List<Message> collection,MainService service, FragmentManager fragmentManager) {

        super(context,collection);
        this.service = service;
        this.fragmentManager = fragmentManager;

        //Получить представление, содержащее список
        parentView = ((Activity)context).findViewById(android.R.id.content);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        View itemView = inflater.inflate(R.layout.message_item, recyclerView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Message messageItem = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;

        //Задать обработчик клика по элементу списка
        holder.btnEdit.setOnClickListener(v -> update(position));
        holder.btnDelete.setOnClickListener(v -> delete(position));

        holder.messageIdTxv.setText(String.format("Id сообщения: %d",messageItem.getId()) );
        holder.attachmentTxv.setText(messageItem.getAttachment() ? "Есть вложения" : "Нет вложений");

        holder.senderTxv.setText(String.format("Отправитель: %s",messageItem.getSender()) );
        holder.receiverTxv.setText(String.format("Получатель: %s",messageItem.getReceiver()) );

        holder.subjectTxv.setText(String.format("Тема: %s",messageItem.getSubject()) );
        holder.messageTextTxv.setText(String.format("Текст: %s",messageItem.getText()) );

    }

    //ViewHolder
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //Id сообщения
        public final TextView messageIdTxv;

        //Отправитель
        public final TextView senderTxv;

        //Получатель
        public final TextView receiverTxv;

        //Вложения
        public final TextView attachmentTxv;

        //Тема
        public final TextView subjectTxv;

        //Текст сообщения
        public final TextView messageTextTxv;

        //Вся разметка элемента списка
        public final LinearLayout mainLl;

        //Кнопа редактирования
        public final Button btnEdit;

        //Кнопка удаления
        public final Button btnDelete;

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.attachmentTxv =  itemView.findViewById(R.id.attachmentsTxv);
            this.senderTxv =      itemView.findViewById(R.id.senderTxv);
            this.receiverTxv =    itemView.findViewById(R.id.receiverTxv);
            this.messageIdTxv =   itemView.findViewById(R.id.messageIdTxv);
            this.subjectTxv =     itemView.findViewById(R.id.subjectTxv);
            this.messageTextTxv = itemView.findViewById(R.id.messageText);
            this.mainLl =         itemView.findViewById(R.id.mainLl);
            this.btnEdit =         itemView.findViewById(R.id.btnEdit);
            this.btnDelete =         itemView.findViewById(R.id.btnDelete);

        }
    }

    //Изменение записи
    @SuppressLint("DefaultLocale")
    private void update(int position){
        Message message = collection.get(position);

        Bundle bundle = new Bundle();

        bundle.putParcelable("message",message);
        bundle.putBoolean("isEditing",true);

        MessageDialog messageDialog = new MessageDialog();

        messageDialog.setArguments(bundle);

        messageDialog.setListener(params -> {
            if (params[0] == null)  {
                Utils.showSnackBar(parentView,String.format("Не удалось изменить сообщение с id: %d", message.getId()));
                return;
            }

            //Вызвать метод на сервисе
            service.updateItem(params[0],position);
            //notifyDataSetChanged();
            notifyItemChanged(position);

            Utils.showSnackBar(parentView,String.format("Сообщение с id: %d успешно изменено", message.getId()));

        });

        //Вывести диалог
        messageDialog.show(fragmentManager,"message_dialog");
    }

    //Удаление записи
    @SuppressLint("DefaultLocale")
    private void delete(int position){
        Message message = collection.get(position);

        service.deleteItem(message);
        notifyDataSetChanged();

        Utils.showSnackBar(parentView,String.format("Сообщение с id: %d успешно удалено", message.getId()));
    }

}
