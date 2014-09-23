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
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import de.bno.snakingnumbers.game.gui.Game;

/**
 * Created by marvin on 23.09.14.
 */
public class Highscores {

    public static void displayHighscores(int requestCode, String leaderBoardID, Activity activity, GoogleApiClient apiClient) {

        activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(apiClient, leaderBoardID), requestCode);
    }

    public static String getDifficultyLeaderBoardId(int difficulty) {

        //TODO: leader board Id

        switch (difficulty) {
            case Game.DIFFICULTY_EASY:
                break;
            case Game.DIFFICULTY_MEDIUM:
                break;
            case Game.DIFFICULTY_HARD:
                break;
        }

        return null;
    }

    public static void handleGameResult(int difficulty, long points, GoogleApiClient apiClient) {

        newEntry(difficulty, points, apiClient);
    }

    private static void newEntry(int difficulty, long points, GoogleApiClient apiClient) {
        Log.d(Highscores.class.getName(), "newEntry " + points);

        //TODO: new Entry
        //Games.Leaderboards.submitScore(apiClient, getDifficultyLeaderBoardId(difficulty), points);
    }
}
