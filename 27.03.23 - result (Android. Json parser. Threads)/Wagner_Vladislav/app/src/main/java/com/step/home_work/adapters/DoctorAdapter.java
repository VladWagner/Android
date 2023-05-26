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
import com.step.home_work.models.entities.Doctor;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    //Загрузчик разметки
    private LayoutInflater inflater;

    //Ссылка на коллекцию
    private List<Doctor> doctors;

    private Context context;


    public DoctorAdapter(@NonNull Context context, @NonNull List<Doctor> collection) {

        this.inflater= LayoutInflater.from(context);
        this.doctors = collection;
        this.context = context;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(R.layout.doctor_item, listView, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }


    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Doctor doctorItem = doctors.get(position);

        holder.snpTxv.setText(
                String.format("ФИО доктора: %s.%s.%s",
                        doctorItem.getSurname(),
                        doctorItem.getName().charAt(0),
                        doctorItem.getPatronymic().charAt(0))
        );
        holder.specialityTxv.setText(String.format("Специальность: %s",doctorItem.getSpeciality()));
        holder.percentTxv.setText(String.format("%s отчислениий: %.2f","%",doctorItem.getPercent()));
        holder.priceTxv.setText(String.format("Стоимость приема: %d",doctorItem.getPayment()));

    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //ФИО
        public final TextView snpTxv;

        //Специальность
        public final TextView specialityTxv;

        //Процент отчислений
        public final TextView percentTxv;

        //Стоимость приёма
        public final TextView priceTxv;

        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.snpTxv =         itemView.findViewById(R.id.doctorSnp);
            this.specialityTxv =  itemView.findViewById(R.id.speciality);
            this.percentTxv =     itemView.findViewById(R.id.percent);
            this.priceTxv =       itemView.findViewById(R.id.price);


        }
    }


}
