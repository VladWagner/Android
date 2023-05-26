package com.step.home_work.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.step.home_work.R;
import com.step.home_work.adapters.TelevisionGrvAdapter;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.Television.Television;
import com.step.home_work.models.Television.TelevisionsContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class TelevisionsListActivity extends AppCompatActivity {

    //Список телевизоров
    private TelevisionsContainer tvContainer;

    //RecyclerView
    private GridView televisionGv;

    private TelevisionGrvAdapter tvAdapter;

    //Кнопки сортировок
    private Button sortByPriceBtn;
    private Button sortByDiagonalBtn;

    private View currentView;

    public static String FILE_NAME = "televisions.bin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_list);

        currentView = this.findViewById(android.R.id.content);

        //Получить коллекцию из файла
        try {
            getCollectionFromFile(FILE_NAME);
        } catch (Exception e) {
           throw new RuntimeException(e);
        }

        //Получить ссылку на список
        televisionGv = findViewById(R.id.grvTelevisionsAdapter);

        sortByPriceBtn = findViewById(R.id.btnSortByPrice);
        sortByDiagonalBtn =  findViewById(R.id.btnSortByDiagonal);

        //Создать адаптер
        tvAdapter = new TelevisionGrvAdapter(this, R.layout.television_grv_item, tvContainer.televisions);

        //Добавить адаптер в GridView
        televisionGv.setAdapter(tvAdapter);

        sortByPriceBtn.setOnClickListener(this::startSortingActivity);
        sortByDiagonalBtn.setOnClickListener(this::startSortingActivity);

    }//onCreate


    //Запуск активности сортировки
    private void startSortingActivity(View btn){
        Intent intent = new Intent(this, TelevisionsSortingActivity.class);

        //Передать коллекцию для сортировки
        intent.putParcelableArrayListExtra(Television.class.getCanonicalName(), (ArrayList<? extends Parcelable>) tvContainer.televisions);


        //Тип сортировки
        switch (btn.getId()){
            case R.id.btnSortByPrice:
                intent.putExtra("orderBy","price");
                break;
            case R.id.btnSortByDiagonal:
                intent.putExtra("orderBy","diagonal");
                break;
        }

        //Запустить активность
        startActivity(intent);
    }

    //Получение коллекции из банарного файла
    private void getCollectionFromFile(String fileName) throws Exception {

        File file = Utils.getExternalPath(fileName,this.getApplicationContext());

        //Если файла нет или он пуст
        if (!file.exists() || file.length() == 0){

            //tvList = Utils.generateTvList(10);
            tvContainer = TelevisionsContainer.containerFactory();

            //Записать коллекцию в файл
            try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file,true))) {
                /*for (Television television : tvList) {
                    os.writeObject(television);
                }*/
                os.writeObject(tvContainer);
            }//try

            return;

        }//if

        try(ObjectInputStream is = new ObjectInputStream(new FileInputStream(file))) {
            //tvList = (List<Television>) is.readObject();
            tvContainer = (TelevisionsContainer) is.readObject();
        }//try

    }

    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.btnExitFromActivity)
            finish();

        return super.onOptionsItemSelected(item);
    }
    //endregion
}