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

package de.bno.snakingnumbers.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.ref.WeakReference;

/**
 * Created by marvin on 16.09.14.
 */
public class Settings {


    public static final String FIRST_START_KEY = "isFirstStart";
    public static final String FIRST_GAME_KEY = "isFirstGame";

    private static WeakReference<SharedPreferences> prefs;


    private static SharedPreferences getPrefs(Context context){

        if(prefs == null || prefs.get() == null){


            prefs = new WeakReference<SharedPreferences>(PreferenceManager.getDefaultSharedPreferences(context));
        }

        return prefs.get();
    }


    private Context context;

    public Settings(Context context){

        this.context = context;
    }

    private SharedPreferences getPrefs(){

        return getPrefs(context);
    }

    public boolean isFirstStart(){

        return getPrefs().getBoolean(FIRST_START_KEY, true);
    }

    public void setFirstStart(boolean firstStart){

        getPrefs().edit().putBoolean(FIRST_START_KEY, firstStart).apply();
    }

    public boolean isFirstGame(){

        return getPrefs().getBoolean(FIRST_GAME_KEY, true);
    }

    public void setFirstGame(boolean firstGame){

        getPrefs().edit().putBoolean(FIRST_GAME_KEY, firstGame).apply();
    }
}
