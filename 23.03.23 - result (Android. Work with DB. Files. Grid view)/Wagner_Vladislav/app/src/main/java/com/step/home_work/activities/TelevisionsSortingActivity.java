package com.step.home_work.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.step.home_work.R;
import com.step.home_work.adapters.TelevisionRsvAdapter;
import com.step.home_work.models.Television.Television;

import java.util.Comparator;
import java.util.List;

public class TelevisionsSortingActivity extends AppCompatActivity {
    private List<Television> tvList;

    //RecyclerView
    private RecyclerView televisionRcv;

    private TelevisionRsvAdapter tvAdapter;

    private View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_televisions_sorting);

        //Получить коллекцию из активности

        Intent intent = getIntent();
        tvList = intent.getParcelableArrayListExtra(Television.class.getCanonicalName());

        String sortingType = intent.getStringExtra("orderBy");

        switch (sortingType.toLowerCase()){
            case "price":
                tvList.sort((tv1,tv2) -> tv2.getPrice() - tv1.getPrice());
                ((TextView)findViewById(R.id.tvTitle)).setText("Сортировка по убыванию цены");
                break;
            case "diagonal":
                tvList.sort(Comparator.comparingDouble(Television::getDiagonal));
                ((TextView)findViewById(R.id.tvTitle)).setText("Сортировка по диагонали");
                break;
        }

        //Получить ссылку на recycler view
        televisionRcv = findViewById(R.id.rcvTelevisionsAdapter);

        //Создать адаптер
        tvAdapter = new TelevisionRsvAdapter(this, R.layout.television_rcv_item, tvList);

        //Добавить адаптер в GridView
        televisionRcv.setAdapter(tvAdapter);
        currentView = this.findViewById(android.R.id.content);

    }//onCreate


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