package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.step.wagner.R;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.entities.Patient;

import java.util.List;

public class PatientAdapter extends BaseAdapter<Patient> {

    //Активность, выводящая список
    private View listActivity;

    public PatientAdapter(@NonNull Context context, @NonNull List<Patient> collection) {

        super(context,collection);
        this.listActivity = ((Activity) context).findViewById(android.R.id.content);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(R.layout.patient_item, listView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Patient patientItem = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;

        holder.snpTxv.setText(
                String.format("ФИО пациента: %s.%s.%s",
                        patientItem.getSurname(),
                        patientItem.getName().charAt(0),
                        patientItem.getPatronymic().charAt(0))
        );
        holder.dobTxv.setText(String.format("Дата рождения: %s",Utils.dateFormat.format(patientItem.getBirthDate())));
        holder.addressTxv.setText(String.format("Адрес: %s",patientItem.getAddress()));
        holder.passportTxv.setText(String.format("Паспорт: %s",patientItem.getPassport()));

    }

    //ViewHolder
    public static class ViewHolder extends BaseAdapter.ViewHolder {

        //ФИО
        public final TextView snpTxv;

        //Дата рождения
        public final TextView dobTxv;

        //Адрес проживания
        public final TextView addressTxv;

        //Паспорт
        public final TextView passportTxv;

        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.snpTxv =         itemView.findViewById(R.id.patientSnp);
            this.dobTxv =          itemView.findViewById(R.id.birthDate);
            this.addressTxv =          itemView.findViewById(R.id.address);
            this.passportTxv =  itemView.findViewById(R.id.passport);


        }
    }


}
