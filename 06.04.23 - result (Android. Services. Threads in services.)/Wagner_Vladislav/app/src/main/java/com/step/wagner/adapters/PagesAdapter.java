package com.step.wagner.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.step.wagner.fragments.pages.RequestPageFragment;

//Адаптер для постраничного вывода
public class PagesAdapter extends FragmentStateAdapter {
    public PagesAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return RequestPageFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
