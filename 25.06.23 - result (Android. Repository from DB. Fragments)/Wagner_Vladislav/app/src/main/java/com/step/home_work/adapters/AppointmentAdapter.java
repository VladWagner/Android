package com.step.home_work.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.step.home_work.R;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.entities.Appointment;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    //Загрузчик разметки
    private LayoutInflater inflater;


    //Ссылка на коллекцию
    private List<Appointment> appointments;

    //Представление, где находится listView
    private Context context;

    //Активность, выводящая список
    private View listActivity;

    public AppointmentAdapter(@NonNull Context context, @NonNull List<Appointment> collection) {

        this.inflater= LayoutInflater.from(context);
        this.appointments = collection;
        this.context = context;

        this.listActivity = ((Activity) context).findViewById(android.R.id.content);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(R.layout.appointment_item, listView, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointmentItem = appointments.get(position);


        holder.appointmentDate.setText(String.format("Дата приема: %s",Utils.dateFormat.format(appointmentItem.getDate())));
        holder.patientSnpTxv.setText(
                String.format("ФИО пациента: %s.%s.%s",
                        appointmentItem.getPatientSurname(),
                        appointmentItem.getPatientName().charAt(0),
                        appointmentItem.getPatientPatronymic().charAt(0))
        );

        holder.doctorSnpTxv.setText(
                String.format("ФИО доктора: %s.%s.%s",
                        appointmentItem.getDoctorSurname(),
                        appointmentItem.getDoctorName().charAt(0),
                        appointmentItem.getDoctorPatronymic().charAt(0))
        );

        holder.patientDobTxv.setText(String.format("Дата рождения пациента: %s",Utils.dateFormat.format(appointmentItem.getDob())));
        holder.patientAddressTxv.setText(String.format("Адрес пациента: %s",appointmentItem.getAddress()));
        holder.patientPassportTxv.setText(String.format("Паспорт пациента: %s",appointmentItem.getPassport()));

        holder.specialityTxv.setText(String.format("Специальность: %s",appointmentItem.getSpeciality()));
        holder.percentTxv.setText(String.format("%s отчислениий: %.2f","%",appointmentItem.getPercent()) );

    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Дата приёма
        public final TextView appointmentDate;

        //ФИО пациента
        public final TextView patientSnpTxv;

        //Дата рождения
        public final TextView patientDobTxv;

        //Адрес проживания
        public final TextView patientAddressTxv;

        //Паспорт
        public final TextView patientPassportTxv;

        //ФИО доктора
        public final TextView doctorSnpTxv;

        //Специальность
        public final TextView specialityTxv;

        //Процент отчислений
        public final TextView percentTxv;

        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.appointmentDate =    itemView.findViewById(R.id.appointmentDate);
            this.patientSnpTxv =      itemView.findViewById(R.id.patientSnp);
            this.patientDobTxv =      itemView.findViewById(R.id.birthDate);
            this.patientAddressTxv =  itemView.findViewById(R.id.address);
            this.patientPassportTxv = itemView.findViewById(R.id.passport);

            this.doctorSnpTxv =       itemView.findViewById(R.id.doctorSnp);
            this.specialityTxv =      itemView.findViewById(R.id.speciality);
            this.percentTxv =         itemView.findViewById(R.id.percent);


        }
    }


}
