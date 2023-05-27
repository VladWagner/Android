package org.itstep.pd011.viewpagerdemo.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.itstep.pd011.viewpagerdemo.fragments.PageFragment;


// адаптер отображения страницы
public class PageAdapter extends FragmentStateAdapter {
    public PageAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // создание фрагмента для очередной страницы - при помощи фабричного метода
    // фрагменты могут и отличаться, лишь бы были в иерархии Fragment
    // !!! место для получения различных по разметке фрагментов !!!
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return PageFragment.newInstance(position);
    }

    // количество страниц - обязательно т.к. FragmentStateAdapter - абстрактный...
    @Override
    public int getItemCount() {
        return 5;
    }
}
