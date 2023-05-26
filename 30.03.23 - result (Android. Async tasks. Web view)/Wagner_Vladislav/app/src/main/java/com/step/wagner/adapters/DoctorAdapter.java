package com.step.wagner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.step.wagner.R;
import com.step.wagner.models.entities.Doctor;

import java.util.List;

public class DoctorAdapter extends BaseAdapter<Doctor> {


    public DoctorAdapter(@NonNull Context context, @NonNull List<Doctor> collection) {

        super(context,collection);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {
        View itemView = inflater.inflate(R.layout.doctor_item, recyclerView, false);

        return new ViewHolder(itemView);
    }


    //Привязка обработчиков и задание значений в textView
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter.ViewHolder baseHolder, int position) {
        Doctor doctorItem = collection.get(position);

        ViewHolder holder = (ViewHolder) baseHolder;

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

    public static class ViewHolder extends BaseAdapter.ViewHolder {

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
