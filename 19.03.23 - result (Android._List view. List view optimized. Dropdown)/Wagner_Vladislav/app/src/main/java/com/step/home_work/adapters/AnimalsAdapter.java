package com.step.home_work.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.step.home_work.R;
import com.step.home_work.activities.AnimalActivity;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.Animal;

import java.util.List;
import java.util.Locale;

public class AnimalsAdapter extends ArrayAdapter<Animal> {

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

    public AnimalsAdapter(@NonNull Context context, int layout, @NonNull List<Animal> collection) {
        super(context, layout, collection);

        this.inflater= LayoutInflater.from(context);
        this.layoutId = layout;
        this.animals = collection;
        this.context = context;

        this.listActivity = ((Activity) context).findViewById(android.R.id.content);

    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, @Nullable View itemView, @NonNull ViewGroup listView) {

        final ViewHolder viewHolder;

        if (itemView == null){
            itemView = inflater.inflate(this.layoutId,listView,false);
            viewHolder = new ViewHolder(itemView,position);

            itemView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) itemView.getTag();
            viewHolder.index = position;

            //Назначить обработчики по индексу
            //viewHolder.setListeners();
        }

        Animal animalItem = animals.get(position);

        //Задать изображение
        try {

            Utils.setImageView(animalItem.getFileName(),viewHolder.animalImageView,context);

        } catch (Exception e) {
            Utils.showSnackBar(listActivity/*((Activity) context).findViewById(android.R.id.content)*/,
                    String.format("Ошибка imageView для судна с id: %d",animalItem.getId()));
        }

        viewHolder.animalIdTxv.setText(String.format("Id животного: %d",animalItem.getId()));

        viewHolder.breedTxv.setText(String.format("Порода: %s",animalItem.getBreed()));
        viewHolder.nameTxv.setText(String.format("Кличка: %s",animalItem.getName()));

        viewHolder.ageTxv.setText(String.format("Возраст: %d лет",animalItem.getAge()));
        viewHolder.animalWeightTxv.setText(String.format(Locale.UK,"Вес: %.2f кг.",animalItem.getWeight()));

        viewHolder.ownerSnpTxv.setText((String.format("Владелец:%s",animalItem.getOwnerSnp()) ) );

        //Вывести признаки
        viewHolder.animalSignsTxv.setText(String.format("Специальная диета: %s\nСпециальный уход: %s",
                animalItem.isDiet() ? "да" : "нет",
                animalItem.isSpecialCare() ? "да" : "нет"
        ));
        return itemView;

    }

    private class ViewHolder{

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

        //Кнопки
        public final Button btnReduceWeight;
        public final Button btnIncreaseWeight;

        public final Button btnReduceAge;
        public final Button btnIncreaseAge;

        //Индекс выводимого элемента
        public int index;
        //endregion

        //Получить ссылки на текущие элементы
        public ViewHolder(View itemView, int index) {

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

            //Кнопки
            this.btnReduceWeight =   itemView.findViewById(R.id.btnReduceWeight);
            this.btnIncreaseWeight = itemView.findViewById(R.id.btnIncreaseWeight);
            this.btnReduceAge =    itemView.findViewById(R.id.btnReduceAge);
            this.btnIncreaseAge =  itemView.findViewById(R.id.btnIncreaseAge);

            this.index = index;

            //Назначить обработчики
            setListeners();

        }

        public void setListeners(){

            //Обработчик клика по элементу
            mainLayout.setOnClickListener(v -> startAnimalFormActivity(index));
            animalWeightTxv.setOnClickListener(v -> startAnimalFormActivity(index));
            ageTxv.setOnClickListener(v -> startAnimalFormActivity(index));

            //Изменение стомисти и груза
            btnIncreaseWeight.setOnClickListener(v -> changeAnimalWeight(R.string.btn_increase, animalWeightTxv,index,v));
            btnReduceWeight.setOnClickListener(v -> changeAnimalWeight(R.string.btn_reduce, animalWeightTxv,index,v));

            btnIncreaseAge.setOnClickListener(v -> changeAnimalAge(R.string.btn_increase, ageTxv,index,v));
            btnReduceAge.setOnClickListener(v -> changeAnimalAge(R.string.btn_reduce, ageTxv,index,v));
        }

    }//ViewHolder

    //Переход на активность редактирования
    private void startAnimalFormActivity(int index){
        Intent intent = new Intent(context, AnimalActivity.class);

        Animal animal = animals.get(index);

        //Передать объект животного в активность
        intent.putExtra(Animal.class.getCanonicalName(),animal);

        //Запустить активность
        ((Activity) context).startActivityForResult(intent, Parameters.ANIMAL_ACTIVITY_ID);
    }


    //Изменение значений веса
    @SuppressLint({"DefaultLocale", "NonConstantResourceId"})
    private void changeAnimalWeight(int btnType, TextView field, int index, View view) {
        try {

            Animal animal = animals.get(index);
            //Текущее значение в поле
            double currValue = animal.getWeight();

            switch (btnType) {

                case R.string.btn_increase:
                    animal.setWeight(currValue + Animal.DELTA);
                    field.setText(String.format(Locale.UK,"Вес: %.2f кг.",animal.getWeight()));
                    break;

                case R.string.btn_reduce:
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
    private void changeAnimalAge(int btnType, TextView field, int index, View view) {
        try {

            Animal animal = animals.get(index);
            //Текущее значение в поле
            int currValue = animal.getAge();

            switch (btnType) {

                case R.string.btn_increase:
                    animal.setAge(currValue + Animal.DELTA);
                    field.setText(String.format("Возраст: %d лет",animal.getAge()));
                    break;

                case R.string.btn_reduce:
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
