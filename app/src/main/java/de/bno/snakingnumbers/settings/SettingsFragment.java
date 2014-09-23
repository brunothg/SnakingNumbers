/*
 * Snaking Numbers an Android game.
 * Copyright (c) 2014 Marvin Bruns
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package de.bno.snakingnumbers.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.google.android.gms.games.Games;

import de.bno.snakingnumbers.R;
import de.bno.snakingnumbers.data.Settings;
import de.bno.snakingnumbers.helper.GooglePlay.GooglePlayActivity;

/**
 * Created by marvin on 17.09.14.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    GooglePlayActivity playActivity;

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        if (activity instanceof GooglePlayActivity) {
            playActivity = (GooglePlayActivity) activity;
        }
    }

    @Override
    public void onDetach() {

        playActivity = null;

        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        addPreferencesFromResource(R.xml.preferences);
        updateSummery(getPreferenceScreen().getSharedPreferences(), Settings.EXPLICIT_OFFLINE_KEY);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(SettingsFragment.class.getName(), "onResume");

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(SettingsFragment.class.getName(), "onPause");

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Log.d(SettingsFragment.class.getName(), "onSharedPreferenceChanged " + key);

        if (key.equals(Settings.EXPLICIT_OFFLINE_KEY)) {

            boolean success = setLogin(sharedPreferences.getBoolean(key, true));
            if (!success) {
                sharedPreferences.edit().putBoolean(key, !sharedPreferences.getBoolean(key, true)).apply();
            }
        }

        updateSummery(sharedPreferences, key);
    }

    private void updateSummery(SharedPreferences sharedPreferences, String key) {

        if (key.equals(Settings.EXPLICIT_OFFLINE_KEY)) {

            Preference connectionPref = findPreference(key);

            if (sharedPreferences.getBoolean(key, true)) {

                connectionPref.setSummary(getString(R.string.settings_explicit_offline_summary_checked));
            } else {

                connectionPref.setSummary(getString(R.string.settings_explicit_offline_summary));
            }
        }
    }

    private boolean setLogin(boolean logOut) {

        if (playActivity == null) {
            return false;
        }

        if (logOut) {
            Games.signOut(playActivity.getApiClient());
            playActivity.disconnect();
        } else {
            playActivity.retryConnecting();
        }

        return true;
    }
}
