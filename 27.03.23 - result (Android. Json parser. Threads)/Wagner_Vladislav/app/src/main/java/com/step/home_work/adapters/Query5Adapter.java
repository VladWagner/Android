package com.step.home_work.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.step.home_work.R;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.queries.Query5;

import java.util.List;

public class Query5Adapter extends RecyclerView.Adapter<Query5Adapter.ViewHolder> {

    //Загрузчик разметки
    private LayoutInflater inflater;


    //Ссылка на коллекцию
    private List<Query5> query5Result;

    private Context context;


    public Query5Adapter(@NonNull Context context, @NonNull List<Query5> collection) {

        this.inflater= LayoutInflater.from(context);
        this.query5Result = collection;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(R.layout.query5_item, listView, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return query5Result.size();
    }


    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Query5 query5Item = query5Result.get(position);


        holder.appointmentDate.setText(String.format("Дата приема: %s",Utils.dateFormat.format(query5Item.getAppointmentDate())));

        holder.doctorSnpTxv.setText(
                String.format("ФИО доктора: %s.%s.%s",
                        query5Item.getDoctorSurname(),
                        query5Item.getDoctorName().charAt(0),
                        query5Item.getDoctorPatronymic().charAt(0))
        );

        holder.specialityTxv.setText(String.format("Специальность доктора: %s",query5Item.getSpeciality()));
        holder.priceTxv.setText(String.format("Стоимость приёма: %d",query5Item.getPrice()) );
        holder.percentTxv.setText(String.format("%s отчислениий: %.2f","%",query5Item.getPercent()) );
        holder.salaryTxv.setText(String.format("Зарплата доктора: %.2f",query5Item.getSalary()) );

    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //Дата приёма
        public final TextView appointmentDate;


        //ФИО доктора
        public final TextView doctorSnpTxv;

        //Специальность
        public final TextView specialityTxv;

        //Стоимость приёма
        public final TextView priceTxv;

        //Процент отчислений
        public final TextView percentTxv;

        //Зарплата
        public final TextView salaryTxv;

        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.appointmentDate =    itemView.findViewById(R.id.appointmentDate);
            this.doctorSnpTxv =       itemView.findViewById(R.id.doctorSnp);
            this.specialityTxv =      itemView.findViewById(R.id.speciality);
            this.priceTxv =         itemView.findViewById(R.id.price);
            this.percentTxv =         itemView.findViewById(R.id.percent);
            this.salaryTxv =         itemView.findViewById(R.id.salary);


        }
    }


}
