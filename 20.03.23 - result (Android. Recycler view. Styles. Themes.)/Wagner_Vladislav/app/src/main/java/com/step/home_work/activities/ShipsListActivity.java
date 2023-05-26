package com.step.home_work.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.step.home_work.R;
import com.step.home_work.adapters.ShipAdapter;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.ship.Ship;

import java.util.ArrayList;
import java.util.List;

public class ShipsListActivity extends AppCompatActivity {

    //Список кораблей
    private List<Ship> shipsList;

    //Recyclerview
    private RecyclerView shipsRcv;

    private ShipAdapter shipsAdapter;

    private View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ships_list);

        //Получить коллекцию из главной активности
        shipsList = getIntent().getParcelableArrayListExtra(Ship.class.getCanonicalName());

        //Получить ссылку на список
        shipsRcv = findViewById(R.id.rcvShipsAdapter);

        //Создать адаптер
        shipsAdapter = new ShipAdapter(this, R.layout.ship_item_full,shipsList);

        //Добавить адаптер в ListView
        shipsRcv.setAdapter(shipsAdapter);
        currentView = this.findViewById(android.R.id.content);

    }//onCreate

    //Получение отредактированного судна
    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData){
        super.onActivityResult(requestCode,resultCode,intentData);

        if(resultCode != Parameters.RESULT_OK){
            return;
        }


        try {

            Ship ship = intentData != null ? intentData.getParcelableExtra(Ship.class.getCanonicalName()) : null;

            if (ship == null){
                Utils.showSnackBar(currentView,String.format("Получить судно из активности с кодом %d не удалось!",requestCode));
                return;
            }

            //Найти старую запись по id
            Ship oldShip = shipsList.stream()
                    .filter(sh -> sh.getId() == ship.getId())
                    .findFirst().get();

            int index = shipsList.indexOf(oldShip);
            //Заменить найденную запись
            shipsList.set(index/*shipsList.indexOf(oldShip)*/,ship);

            shipsAdapter.notifyDataSetChanged();
            //shipsAdapter.notifyItemChanged(index);


            Utils.showSnackBar(currentView,String.format("Судно с id: %d изменено",ship.getId()));

        } catch (Exception e) {
            Utils.showSnackBar(currentView,e.getMessage());
        }//catch

    }//onActivityResult

    //Возврать из активности
    private void returnFromActivity() {

        Intent intent = new Intent();

        //Задать изменённую модель
        intent.putParcelableArrayListExtra(Ship.class.getCanonicalName(), (ArrayList<? extends Parcelable>) shipsList);

        setResult(Parameters.RESULT_OK, intent);

        //Выход из активности
        finish();

    }

    @Override
    public void onBackPressed(){
        returnFromActivity();
    }


    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @SuppressLint("DefaultLocale")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnAdd:
                Ship ship = Ship.factory();
                shipsList.add(ship);
                //shipsAdapter.notifyDataSetChanged();
                shipsAdapter.notifyItemInserted(shipsList.indexOf(ship));

                Utils.showSnackBar(currentView,String.format("Судно с id: %d добавлено",ship.getId()));
                break;
            case R.id.btnExitFromActivity:
                returnFromActivity();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}