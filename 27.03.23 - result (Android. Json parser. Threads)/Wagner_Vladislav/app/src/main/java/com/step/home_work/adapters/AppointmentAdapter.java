package com.step.home_work.adapters;

import static com.step.home_work.infrastructure.Utils.appointmentsRepository;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.step.home_work.R;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.entities.Appointment;

import java.util.Date;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    //Загрузчик разметки
    private LayoutInflater inflater;


    //Ссылка на коллекцию
    private List<Appointment> appointments;

    private Context context;

    //Активность, выводящая список
    private View listParent;

    //Режим вывода списка
    private boolean changeable;

    public AppointmentAdapter(@NonNull Context context, @NonNull List<Appointment> collection,boolean isChangeable) {

        this.inflater= LayoutInflater.from(context);
        this.appointments = collection;
        this.context = context;

        this.listParent = ((Activity) context).findViewById(android.R.id.content);
        this.changeable = isChangeable;
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

        //Если выводится таблица,а не результат запроса
        if (changeable){
            holder.appointmentIdTxv.setText(String.format("Id приема: %d",appointmentItem.getId()));

            holder.editBtn.setOnClickListener(v -> update(position));
            holder.deleteBtn.setOnClickListener(v -> delete(position));

        }else {
            holder.appointmentIdTxv.setVisibility(View.GONE);

            holder.editBtn.setVisibility(View.GONE);
            holder.deleteBtn.setVisibility(View.GONE);
        }

        holder.appointmentDate.setText(String.format("Дата приема: %s",Utils.dateFormat.format(appointmentItem.getAppointmentDate())));
        holder.patientSnpTxv.setText(
                String.format("ФИО пациента: %s.%s.%s",
                        appointmentItem.getPatient().getSurname(),
                        appointmentItem.getPatient().getName().charAt(0),
                        appointmentItem.getPatient().getPatronymic().charAt(0))
        );

        holder.doctorSnpTxv.setText(
                String.format("ФИО доктора: %s.%s.%s",
                        appointmentItem.getDoctor().getSurname(),
                        appointmentItem.getDoctor().getName().charAt(0),
                        appointmentItem.getDoctor().getPatronymic().charAt(0))
        );

        holder.patientDobTxv.setText(String.format("Дата рождения пациента: %s",Utils.dateFormat.format(appointmentItem.getPatient().getBirthDate())));
        holder.patientAddressTxv.setText(String.format("Адрес пациента: %s",appointmentItem.getPatient().getAddress()));
        holder.patientPassportTxv.setText(String.format("Паспорт пациента: %s",appointmentItem.getPatient().getPassport()));

        holder.specialityTxv.setText(String.format("Специальность: %s",appointmentItem.getDoctor().getSpeciality()));
        holder.percentTxv.setText(String.format("%s отчислениий: %.2f","%",appointmentItem.getDoctor().getPercent()) );

    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Дата приёма
        public final TextView appointmentDate;

        //Id приёма
        public final TextView appointmentIdTxv;

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


        //Кнопки
        public final Button editBtn;
        //Кнопки
        public final Button deleteBtn;

        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.appointmentIdTxv =   itemView.findViewById(R.id.appointmentId);
            this.appointmentDate =    itemView.findViewById(R.id.appointmentDate);
            this.patientSnpTxv =      itemView.findViewById(R.id.patientSnp);
            this.patientDobTxv =      itemView.findViewById(R.id.birthDate);
            this.patientAddressTxv =  itemView.findViewById(R.id.address);
            this.patientPassportTxv = itemView.findViewById(R.id.passport);

            this.doctorSnpTxv =       itemView.findViewById(R.id.doctorSnp);
            this.specialityTxv =      itemView.findViewById(R.id.speciality);
            this.percentTxv =         itemView.findViewById(R.id.percent);

            this.editBtn = itemView.findViewById(R.id.btnEdit);
            this.deleteBtn = itemView.findViewById(R.id.btnDelete);


        }
    }

    //Изменение записи
    @SuppressLint("DefaultLocale")
    private void update(int index){
        Appointment appointment = appointments.get(index);

        Date newDate = Utils.getRandomDate();
        appointment.setAppointmentDate(newDate);

        //Открыть соединение с БД
        appointmentsRepository.open();

        appointmentsRepository.update(appointment);

        //Переписать коллекцию
        appointments.clear();
        appointments.addAll(appointmentsRepository.getAll());

        appointmentsRepository.close();
        notifyDataSetChanged();

        //Вывод сообщения
        Utils.showSnackBar(listParent,String.format("Запись с id: %d успешно изменена",appointment.getId()));
    }//update

    @SuppressLint("DefaultLocale")
    private void delete(int index){
        Appointment appointment = appointments.get(index);

        //Открыть соед
        appointmentsRepository.open();

        appointmentsRepository.delete(appointment.getId());

        //Переписать коллекцию
        appointments.clear();
        appointments.addAll(appointmentsRepository.getAll());

        appointmentsRepository.close();

        notifyDataSetChanged();

        //Вывод сообщения
        Utils.showSnackBar(listParent,String.format("Запись с id: %d успешно удалена",appointment.getId()));

    }

}
