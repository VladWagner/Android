package org.itstep.pd011.viewpagerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.itstep.pd011.viewpagerdemo.adapters.PageAdapter;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    ViewPager2           pager;         // страничный интерфейс
    FragmentStateAdapter pageAdapter;   // адаптер страницы
    TabLayout            tabLayout;     // заголовки для переходов по страницам

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // связь с разметкой
        pager = findViewById(R.id.vpgMain);

        // адаптер для страниц
        pageAdapter = new PageAdapter(this);
        pager.setAdapter(pageAdapter);

        // заголовки страниц - не обязательный элемент
        tabLayout = findViewById(R.id.tbLMain);
        // tab      - ссылка на конкретный заголовок
        // position - индекс страницы
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, pager, (tab, position) -> {

            // разметка заголовка страницы
            tab.setCustomView(R.layout.tab_header);
            TextView txvHeaderPageNumber = tab.getCustomView().findViewById(R.id.txvHeaderPageNumber);

            // имитация индивидуальной настройки заголовков
            int page = position + 1;
            txvHeaderPageNumber.setText(String.format(Locale.UK, "%d", page));
        });

        // подключить настройщика к области вывода заголовоков
        tabLayoutMediator.attach();
    } // onCreate

    //region Работа с меню приложения
    @Override  // создание меню приложения
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    } // onCreateOptionsMenu


    @Override // обработка выбора в меню приложения
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mniExit) {
            finish();
            return true;
        } // if
        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected
    //endregion
}