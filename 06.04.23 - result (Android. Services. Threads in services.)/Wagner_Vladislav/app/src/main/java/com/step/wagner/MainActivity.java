package com.step.wagner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.step.wagner.adapters.PagesAdapter;
import com.step.wagner.infrastructure.Utils;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    FragmentStateAdapter pagesAdapter;
    TabLayout tabLayout;

    //Текущее представление
    View currentView;

    public BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentView = this.findViewById(android.R.id.content);

        viewPager = findViewById(R.id.vpgRequests);

        //Создать адаптер для ViewPager
        pagesAdapter = new PagesAdapter(this);
        viewPager.setAdapter(pagesAdapter);

        tabLayout = findViewById(R.id.tabLayout);

        //Задать разметку и содержание заголовкам
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, (header, index) -> {

            header.setCustomView(R.layout.tab_header);
            TextView txvHeader = header.getCustomView().findViewById(R.id.txvHeader);

            txvHeader.setText(Utils.pagesHeaders[index]);

        });//tabLayoutMediator

        tabLayoutMediator.attach();

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

        if (item.getItemId() == R.id.btnExitFromActivity)
            finish();


        return super.onOptionsItemSelected(item);
    }
    //endregion



}