package com.step.home_work.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.step.home_work.R;
import com.step.home_work.activities.AnimalActivity;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.Animal;

import java.util.List;
import java.util.Locale;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {

    //Загрузчик разметки
    private LayoutInflater inflater;

    //Id ресурса
    private int layoutId;

    //Ссылка на коллекцию
    private List<Animal> animals;

    //Представление, где находится listView
    private Context context;

    //Активность, выводящая список
    private View listActivity;

    public AnimalAdapter(@NonNull Context context, int layout, @NonNull List<Animal> collection) {

        this.inflater= LayoutInflater.from(context);
        this.layoutId = layout;
        this.animals = collection;
        this.context = context;

        this.listActivity = ((Activity) context).findViewById(android.R.id.content);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup listView, int viewType) {
        View itemView = inflater.inflate(layoutId, listView, false);

        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }


    //Задать значения в поля и привязать обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Animal animalItem = animals.get(position);

        //Задать изображение
        try {

            Utils.setImageView(animalItem.getFileName(),holder.animalImageView,context);

        } catch (Exception e) {
            Utils.showSnackBar(listActivity,String.format("Ошибка imageView для животного с id: %d",animalItem.getId()));
        }

        holder.animalIdTxv.setText(String.format("Id животного: %d",animalItem.getId()));

        holder.breedTxv.setText(String.format("Порода: %s",animalItem.getBreed()));
        holder.nameTxv.setText(String.format("Кличка: %s",animalItem.getName()));

        holder.ageTxv.setText(String.format("Возраст: %d лет",animalItem.getAge()));
        holder.animalWeightTxv.setText(String.format("Вес: %.2f кг.",animalItem.getWeight()));

        holder.ownerSnpTxv.setText((String.format("Владелец:%s",animalItem.getOwnerSnp()) ) );

        //Вывести признаки
        holder.animalSignsTxv.setText(String.format("Специальная диета: %s\nСпециальный уход: %s",
                animalItem.isDiet() ? "да" : "нет",
                animalItem.isSpecialCare() ? "да" : "нет"
        ));

        //Задать обработчики
        holder.mainLayout.setOnClickListener(v -> startAnimalFormActivity(position));

        holder.btnEdit.setOnClickListener(v -> startAnimalFormActivity(position));
        holder.btnDelete.setOnClickListener(v -> deleteShip(position));

        //Изменение стомисти и груза
        holder.btnIncreaseWeight.setOnClickListener(v -> changeAnimalWeight(v, holder.animalWeightTxv,position));
        holder.btnReduceWeight.setOnClickListener(v -> changeAnimalWeight(v, holder.animalWeightTxv,position));

        holder.btnIncreaseAge.setOnClickListener(v -> changeAnimalAge(v, holder.ageTxv,position));
        holder.btnReduceAge.setOnClickListener(v -> changeAnimalAge(v, holder.ageTxv,position));
    }

    //ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //region Поля
        public final ImageView animalImageView;

        public final LinearLayout mainLayout;

        //id
        public final TextView animalIdTxv;

        //Порода
        public final TextView breedTxv;

        //Кличка
        public final TextView nameTxv;

        //Возраст
        public final TextView ageTxv;

        //ФИО владельца
        public final TextView ownerSnpTxv;

        //Вес
        public final TextView animalWeightTxv;

        //Признаки животного
        public final TextView animalSignsTxv;


        //Кнопки удаления и редактирования
        public final Button btnEdit;
        public final Button btnDelete;

        //Кнопки
        public final Button btnReduceWeight;
        public final Button btnIncreaseWeight;

        public final Button btnReduceAge;
        public final Button btnIncreaseAge;

        //Индекс выводимого элемента
        public int index;
        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Поля вывода
            this.animalImageView =    itemView.findViewById(R.id.animalImage);
            this.mainLayout =       itemView.findViewById(R.id.mainLl);
            this.animalIdTxv =      itemView.findViewById(R.id.animalId);
            this.breedTxv =         itemView.findViewById(R.id.breed);
            this.nameTxv =          itemView.findViewById(R.id.name);
            this.ageTxv =          itemView.findViewById(R.id.age);
            this.ownerSnpTxv =      itemView.findViewById(R.id.ownerSnp);
            this.animalWeightTxv =  itemView.findViewById(R.id.weight);
            this.animalSignsTxv =   itemView.findViewById(R.id.animalSigns);


            //Кнопки удаления и редактирования
            this.btnEdit = itemView.findViewById(R.id.btnEdit);
            this.btnDelete = itemView.findViewById(R.id.btnDelete);

            //Кнопки
            this.btnReduceWeight =   itemView.findViewById(R.id.btnReduceWeight);
            this.btnIncreaseWeight = itemView.findViewById(R.id.btnIncreaseWeight);
            this.btnReduceAge =    itemView.findViewById(R.id.btnReduceAge);
            this.btnIncreaseAge =  itemView.findViewById(R.id.btnIncreaseAge);


        }
    }

    //Переход на активность редактирования
    private void startAnimalFormActivity(int index){
        Intent intent = new Intent(context, AnimalActivity.class);

        Animal animal = animals.get(index);

        //Передать объект животного в активность
        intent.putExtra(Animal.class.getCanonicalName(),animal);

        //Запустить активность
        ((Activity) context).startActivityForResult(intent, Parameters.ANIMAL_ACTIVITY_ID);
    }

    //Удаление
    @SuppressLint("DefaultLocale")
    private void deleteShip(int index){
        int id = animals.get(index).getId();

        animals.remove(index);

        Utils.showSnackBar(listActivity,String.format("Животное с id: %d удалено",id));

        notifyItemRemoved(index);
    }

    //Изменение значений веса
    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
    private void changeAnimalWeight(View btn, TextView field, int index) {
        try {

            Animal animal = animals.get(index);
            //Текущее значение в поле
            double currValue = animal.getWeight();

            switch (btn.getId()) {

                case R.id.btnIncreaseWeight:
                    animal.setWeight(currValue + Animal.DELTA);
                    field.setText(String.format(Locale.UK,"Вес: %.2f кг.",animal.getWeight()));
                    break;

                case R.id.btnReduceWeight:
                    if (currValue >= Animal.DELTA) {
                        animal.setWeight(currValue - Animal.DELTA);
                        field.setText(String.format(Locale.UK,"Вес: %.2f кг.",animal.getWeight()) );
                    }
                    break;

            }//switch

        } catch (Exception e) {
            Utils.showSnackBar(listActivity,e.getMessage());
        }//try-catch
    }

    //Изменение значений возраста
    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
    private void changeAnimalAge(View btn, TextView field, int index) {
        try {

            Animal animal = animals.get(index);
            //Текущее значение в поле
            int currValue = animal.getAge();

            switch (btn.getId()) {

                case R.id.btnIncreaseAge:
                    animal.setAge(currValue + Animal.DELTA);
                    field.setText(String.format("Возраст: %d лет",animal.getAge()));
                    break;

                case R.id.btnReduceAge:
                    if (currValue >= Animal.DELTA) {
                        animal.setAge(currValue - Animal.DELTA);
                        field.setText(String.format("Возраст: %d лет",animal.getAge()));
                    }
                    break;

            }//switch

        } catch (Exception e) {
            Utils.showSnackBar(listActivity,e.getMessage());
        }//try-catch
    }

}
