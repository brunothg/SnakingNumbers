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


    public static final String FIRST_GAME_KEY = "isFirstGame";
    public static final String EXPLICIT_OFFLINE_KEY = "isExplicitOffline";
    public static final String FIRST_SERVICE_TRY_KEY = "isFirstServiceTry";

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

    public boolean isFirstGame(){

        return getPrefs().getBoolean(FIRST_GAME_KEY, true);
    }

    public void setFirstGame(boolean firstGame){

        getPrefs().edit().putBoolean(FIRST_GAME_KEY, firstGame).apply();
    }

    public boolean isExplicitOffline(){

        return getPrefs().getBoolean(EXPLICIT_OFFLINE_KEY, true);
    }

    public void setExplicitOffline(boolean explicitOffline){

        if(explicitOffline == false){

            setFirstServiceTry(false);
        }

        getPrefs().edit().putBoolean(EXPLICIT_OFFLINE_KEY, explicitOffline).apply();
    }

    public boolean isFirstServiceTry(){

        return getPrefs().getBoolean(FIRST_SERVICE_TRY_KEY, true);
    }

    public void setFirstServiceTry(boolean first){

        getPrefs().edit().putBoolean(FIRST_SERVICE_TRY_KEY, first).apply();
    }
}
