package com.step.wagner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.step.wagner.R;
import com.step.wagner.adapters.view_pager.RequestsAdapter;
import com.step.wagner.adapters.view_pager.TablesAdapter;

public class WebRequestsActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    FragmentStateAdapter tablesAdapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_requests);

        viewPager = findViewById(R.id.vpgRequests);

        //Создать адаптер для ViewPager
        tablesAdapter = new RequestsAdapter(this);
        viewPager.setAdapter(tablesAdapter);

        tabLayout = findViewById(R.id.tabLayout);

        //Задать разметку и содержание заголовкам
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (header, index) -> {

            header.setCustomView(R.layout.tab_header);
            TextView txvHeader = header.getCustomView().findViewById(R.id.txvHeader);

            switch (index) {
                case 0:
                    txvHeader.setText("Альбомы");
                    break;
                case 1:
                    txvHeader.setText("Альбом по id");
                    break;
                case 2:
                    txvHeader.setText("Комментарии");
                    break;
                case 3:
                    txvHeader.setText("Посты");
                    break;
            }

        });//tabLayoutMediator

        tabLayoutMediator.attach();
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

        if (item.getItemId() == R.id.btnExitFromActivity)
            finish();

        return super.onOptionsItemSelected(item);
    }
    //endregion

}