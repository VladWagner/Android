package com.step.wagner.adapters.view_pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.step.wagner.fragments.pages.AppointmentsPageFragment;
import com.step.wagner.fragments.pages.TablePageFragment;

//Адаптер для постраничного вывода таблиц БД
public class TablesAdapter extends FragmentStateAdapter {
    public TablesAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment;

        //Создание разных фрагментов в зависимости от индекса страницы
        if (position == 0)
            fragment = AppointmentsPageFragment.newInstance();
        else
            fragment = TablePageFragment.newInstance(position);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
