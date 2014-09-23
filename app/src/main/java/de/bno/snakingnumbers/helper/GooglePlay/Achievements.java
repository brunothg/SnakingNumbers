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

package de.bno.snakingnumbers.helper.GooglePlay;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.util.LinkedList;
import java.util.List;

import de.bno.snakingnumbers.R;
import de.bno.snakingnumbers.game.gui.Game;

/**
 * Created by marvin on 23.09.14.
 */
public class Achievements {

    public static void displayAchievements(int requestCode, Activity activity, GoogleApiClient apiClient) {

        activity.startActivityForResult(Games.Achievements.getAchievementsIntent(apiClient), requestCode);
    }

    public static void handleGameResult(int difficulty, long time, int clicks, float prozent, GoogleApiClient apiClient, Context context) {

        String[] ids = getAchievementID(difficulty, time, clicks, prozent, context);

        if (ids == null) {
            return;
        }

        for (String id : ids) {
            Log.d(Achievements.class.getName(), "unlock ID:" + id.substring(id.length() / 2));

            Games.Achievements.unlock(apiClient, id);
        }
    }

    private static String[] getAchievementID(int difficulty, long time, int clicks, float prozent, Context context) {

        List<String> ids = new LinkedList<String>();

        switch (difficulty) {

            case Game.DIFFICULTY_EASY:
                ids.add(context.getString(R.string.achievement_complete_first_easy));

                if (prozent == 0) {
                    ids.add(context.getString(R.string.achievement_perfect_easy));
                }
                break;
            case Game.DIFFICULTY_MEDIUM:
                ids.add(context.getString(R.string.achievement_complete_first_medium));

                if (prozent == 0) {
                    ids.add(context.getString(R.string.achievement_perfect_medium));
                }
                break;
            case Game.DIFFICULTY_HARD:
                ids.add(context.getString(R.string.achievement_complete_first_hard));

                if (prozent == 0) {
                    ids.add(context.getString(R.string.achievement_perfect_hard));
                }
                break;
        }


        if (ids.size() <= 0) {
            return null;
        }
        return ids.toArray(new String[ids.size()]);
    }
}
