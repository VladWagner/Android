package org.itstep.pd011.preferencesfragmentcompatdemo;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

// Фрагмент SettingsFragment наследуется от класса PreferenceFragmentCompat.
// В его методе onCreatePreferences вызывается метод addPreferencesFromResource(),
// в который передается id ресурса xml с настройками (в данном случае ранее
// определенный ресурс R.xml.settings).
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }
}
