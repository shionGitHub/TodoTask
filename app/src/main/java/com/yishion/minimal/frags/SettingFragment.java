package com.yishion.minimal.frags;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.yishion.minimal.R;
import com.yishion.minimal.utils.SharedPreferencesUtils;

public class SettingFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perfer_share);
        String key = getResources().getString(R.string.night_mode_pref_key);
        CheckBoxPreference preference = (CheckBoxPreference) findPreference(key);
        preference.setChecked(!SharedPreferencesUtils.isThemeLight(getActivity()));
    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.e("tag", "---------------:" + key);
        if (key.equalsIgnoreCase(getResources().getString(R.string.night_mode_pref_key))) {
            CheckBoxPreference preference = (CheckBoxPreference) findPreference(key);

            if (!preference.isChecked()) {
                SharedPreferencesUtils.setCurrentTheme(getActivity(), SharedPreferencesUtils.THEME_LIGHT);
            }
            else {
                SharedPreferencesUtils.setCurrentTheme(getActivity(), SharedPreferencesUtils.THEME_DARK);
            }
            SharedPreferencesUtils.reCreate(getActivity(), true);
            getActivity().recreate();
        }

    }
}
