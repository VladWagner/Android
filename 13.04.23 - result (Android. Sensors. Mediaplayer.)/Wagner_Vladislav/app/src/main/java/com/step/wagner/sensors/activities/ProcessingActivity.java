package com.step.wagner.sensors.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.step.wagner.sensors.R;
import com.step.wagner.sensors.adapters.PagesAdapter;
import com.step.wagner.sensors.fragments.ProcessingFragment;
import com.step.wagner.sensors.fragments.ProcessingHistoryFragment;
import com.step.wagner.sensors.infrastructure.SessionState;
import com.step.wagner.sensors.infrastructure.Utils;

public class ProcessingActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;

    //Текущее представление
    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        currentView = this.findViewById(android.R.id.content);

        viewPager = findViewById(R.id.vpgProcessing);

        //Массив фрагментов
        Fragment[] fragments = new Fragment[]{
                new ProcessingFragment(),
                new ProcessingHistoryFragment()
        };

        //Создать адаптер для ViewPager
        viewPager.setAdapter(new PagesAdapter(this, fragments));

        tabLayout = findViewById(R.id.tabLayout);

        //Задать разметку и содержание заголовкам
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (header, index) -> {

            header.setCustomView(R.layout.tab_header);
            TextView txvHeader = header.getCustomView().findViewById(R.id.txvHeader);

            txvHeader.setText(Utils.pagesHeaders[index]);

        });//tabLayoutMediator

        tabLayoutMediator.attach();
    }


    //Сохранить состояние сессии
    @Override
    protected void onStop() {
        super.onStop();

        SessionState.writeValues(this);

    }


    //region Установка меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activities_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Обработчики
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.btnSettings:
                startActivity(new Intent(this,SettingsActivity.class));
            case R.id.btnExitFromActivity:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion
}