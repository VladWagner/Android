package com.step.wagner.adapters.view_pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.step.wagner.fragments.pages.AlbumByIdPageFragment;
import com.step.wagner.fragments.pages.RequestPageFragment;

//Адаптер для постраничного вывода таблиц БД
public class RequestsAdapter extends FragmentStateAdapter {
    public RequestsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = AlbumByIdPageFragment.newInstance();

        //Создание разных фрагментов в зависимости от индекса страницы
        switch (position){
            case 0: case 2: case 3:
                fragment = RequestPageFragment.newInstance(position);
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
