package com.step.wagner.fragments.dialogues;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.step.wagner.R;
import com.step.wagner.infrastructure.AppSettings;
import com.step.wagner.infrastructure.Parameters;
import com.step.wagner.interfaces.Listener;
import com.step.wagner.models.Message;

public class MessageDialog extends DialogFragment {

    private Listener<Message> listener;

    //Активность
    private Activity activity;

    private EditText senderField;
    private EditText receiverField;
    private EditText subjectField;
    private EditText textField;
    private Switch swtAttachments;

    //Объект, получаемый при редактировании
    private Message message;

    //Режим работы диалога
    private boolean isEdit;

    //Для чтения файла настроек
    private SharedPreferences sharedPreferences;

    public MessageDialog(){
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        activity = getActivity();
        LayoutInflater inflater = LayoutInflater.from(activity);

        View view =  inflater.inflate(R.layout.message_dialog,null);

        //Получить ссылки на элементы разметки
        senderField = view.findViewById(R.id.senderEtx);
        receiverField = view.findViewById(R.id.receiverEtx);
        subjectField = view.findViewById(R.id.subjectEtx);
        textField = view.findViewById(R.id.textEtx);
        swtAttachments = view.findViewById(R.id.swtAttachment);

        Bundle arguments = getArguments();
        isEdit = arguments.getBoolean("isEditing",false);

        //Если режим редактирования, тогда получить редактируемую запись и задать значения в поля
        if (isEdit){
            message = arguments.getParcelable("message");

            fillFields();
        }
        else {

            //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
            sharedPreferences = activity.getSharedPreferences(Parameters.DIALOG_SETTINGS_FILE, Context.MODE_PRIVATE);
            //Прочитать значения из файла настроек и заполнить поля
            if (AppSettings.saveFields)
                fillFieldsFromSettings();
        }

        builder.setTitle(isEdit ? String.format("Редактирование сообщения с id: %d",message.getId()) : "Добавление сообщения")
                .setView(view)
                .setNegativeButton("Отмена",null)
                .setPositiveButton("Сохранить",onClickListener)
                .setCancelable(false);

        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    //Обработчки кнопки сохранения
    private DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            //Получение значений из полей
            String sender = senderField.getText().toString();
            String receiver = receiverField.getText().toString();
            String subject = subjectField.getText().toString();
            String text = textField.getText().toString();
            Boolean hasAttachments = swtAttachments.isChecked();

            Message sendingMessage;

            //Если режим редактирования, тогда создаём объект с ID
            if (isEdit)
                sendingMessage = new Message(message.getId(),sender,receiver,hasAttachments,subject,text);
            else {
                sendingMessage = new Message(sender, receiver, hasAttachments, subject, text);

                //Записать значения из полей в файл настроек
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("message_sender",sender);
                editor.putString("message_receiver",receiver);
                editor.putString("message_subject",subject);
                editor.putString("message_text",text);
                editor.putBoolean("has_attachments",hasAttachments);

                editor.apply();
            }

            listener.onOkClickListener(sendingMessage);
        }
    };

    //Записать значения в поля
    private void fillFields(){
        senderField.setText(message.getSender());
        receiverField.setText(message.getReceiver());
        subjectField.setText(message.getSubject());
        textField.setText(message.getText());

        swtAttachments.setChecked(message.getAttachment());
    }

    //Заполнить поля из настроек
    private void fillFieldsFromSettings(){

        senderField.setText(sharedPreferences.getString("message_sender",""));
        receiverField.setText(sharedPreferences.getString("message_receiver",""));
        subjectField.setText(sharedPreferences.getString("message_subject",""));
        textField.setText(sharedPreferences.getString("message_text",""));

        swtAttachments.setChecked(sharedPreferences.getBoolean("has_attachments",false));
    }

    //Задать обработчика извне
    public void setListener(Listener<Message> listener){
        if (listener == null) return;

        this.listener = listener;
    }

}
