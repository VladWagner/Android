package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.step.wagner.R;
import com.step.wagner.infrastructure.Utils;
import com.step.wagner.models.queries.Query5;

import java.util.List;

public class Query5Adapter extends BaseAdapter<Query5> {

    public Query5Adapter(@NonNull Context context, @NonNull List<Query5> collection) {
        super(context,collection);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(R.layout.query5_item, listView, false);

        return new ViewHolder(itemView);
    }

    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Query5 query5Item = collection.get(position);

        //Привести базовый тип ViewHolder, к наследнику
        ViewHolder holder = (ViewHolder) baseHolder;

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
    public static class ViewHolder extends BaseAdapter.ViewHolder {

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
