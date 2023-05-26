package com.step.home_work.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.step.home_work.R;
import com.step.home_work.adapters.ShipsAdapter;
import com.step.home_work.infrastructure.Parameters;
import com.step.home_work.infrastructure.Utils;
import com.step.home_work.models.Animal;
import com.step.home_work.models.ship.Ship;

import java.util.List;

public class ShipsListActivity extends AppCompatActivity {

    //Список кораблей
    private List<Ship> shipsList;

    //Listview
    private ListView shipsLsv;

    private ShipsAdapter shipsAdapter;

    private View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ships_list);

        Parameters.lastShipId = 0;
        shipsList = Utils.generateShipsList();

        //Получить ссылку на список
        shipsLsv = findViewById(R.id.lsvShipsAdapter);

        //Создать адаптер
        shipsAdapter = new ShipsAdapter(this, R.layout.ship_item,shipsList);

        //Добавить адаптер в ListView
        shipsLsv.setAdapter(shipsAdapter);
        currentView = this.findViewById(android.R.id.content);

    }//onCreate

    //Получение отредактированного судна
    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData){
        super.onActivityResult(requestCode,resultCode,intentData);

        if(resultCode != Parameters.RESULT_OK){
            Utils.showSnackBar(currentView,String.format("Активность с кодом %d вернула код ошибки: %d",requestCode,resultCode));
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

            //Заменить найденную запись
            shipsList.set(shipsList.indexOf(oldShip),ship);

            shipsAdapter.notifyDataSetChanged();


            Utils.showSnackBar(currentView,String.format("Судно с id: %d изменено",ship.getId()));

        } catch (Exception e) {
            Utils.showSnackBar(currentView,e.getMessage());
        }//catch

    }//onActivityResult

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