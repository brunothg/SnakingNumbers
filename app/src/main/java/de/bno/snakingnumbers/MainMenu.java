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

package de.bno.snakingnumbers;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import de.bno.snakingnumbers.about.About;
import de.bno.snakingnumbers.data.Settings;
import de.bno.snakingnumbers.game.gui.Game;
import de.bno.snakingnumbers.helper.FullScreenActivity;
import de.bno.snakingnumbers.result.GameResult;
import de.bno.snakingnumbers.settings.SettingsActivity;
import de.bno.snakingnumbers.tutorial.Tutorial;

/**
 * Created by marvin on 09.09.14.
 */
public class MainMenu extends FullScreenActivity implements View.OnClickListener {

    private static final int REQUEST_GAME_RESULT = 0x01;
    private static final int REQUEST_TUT_RESULT = 0x02;

    private Button btn_easy_game;
    private Button btn_medium_game;
    private Button btn_hard_game;

    private Button btn_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        fetchGUIElements();
        createDefaultSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            gotoSettingsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v == btn_about) {

            gotoAboutActivity();
        } else if (v == btn_easy_game || v == btn_medium_game || v == btn_hard_game) {

            gotoGameActivity(v);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_GAME_RESULT) {

            onGameResult(resultCode, data);
        } else if (requestCode == REQUEST_TUT_RESULT) {

            onTutResult(resultCode, data);
        }
    }

    //Helper methods

    private void onGameResult(int resultCode, Intent data) {

        Log.d(MainMenu.class.getName(), "GameResult " + resultCode);

        if (resultCode == RESULT_OK) {
            long elapsedTime = data.getLongExtra(Game.EXTRA_ELAPSED_TIME, -1);
            int difficulty = data.getIntExtra(Game.EXTRA_DIFFICULTY, -1);
            int clickCount = data.getIntExtra(Game.EXTRA_CLICK_COUNT, -1);
            int maxNumber = data.getIntExtra(Game.EXTRA_MAX_NUMBER, -1);
            Log.d(MainMenu.class.getName(), "GameResult [elapsedTime, difficulty, clickCount, maxNumber] " + elapsedTime + " " + difficulty + " " + clickCount + " " + maxNumber);

            if (elapsedTime > -1 && difficulty > -1 && clickCount > -1 && maxNumber > -1) {

                //TODO: Game result
            }
        }
    }

    private void onTutResult(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            int difficulty = data.getIntExtra(Tutorial.EXTRA_DIFFICULTY, -1);

            if (difficulty > -1) {

                gotoGameActivity(difficulty);
            }
        }
    }

    private void fetchGUIElements() {

        btn_easy_game = (Button) findViewById(R.id.btn_easy_game);
        btn_easy_game.setOnClickListener(this);

        btn_medium_game = (Button) findViewById(R.id.btn_medium_game);
        btn_medium_game.setOnClickListener(this);

        btn_hard_game = (Button) findViewById(R.id.btn_hard_game);
        btn_hard_game.setOnClickListener(this);

        btn_about = (Button) findViewById(R.id.btn_about);
        btn_about.setOnClickListener(this);
    }

    private void gotoGameActivity(View v) {

        if (v == btn_easy_game) {

            gotoGameActivity(Game.DIFFICULTY_EASY);
        } else if (v == btn_medium_game) {

            gotoGameActivity(Game.DIFFICULTY_MEDIUM);
        } else if (v == btn_hard_game) {

            gotoGameActivity(Game.DIFFICULTY_HARD);
        }


    }

    private void gotoSettingsActivity(){

        Intent settings_intent = new Intent(this, SettingsActivity.class);
        startActivity(settings_intent);
    }

    private void gotoGameActivity(int difficulty) {

        Settings settings = new Settings(this);

        if(settings.isFirstGame()){

            gotoTutorialActivity(difficulty);
            return;
        }

        Intent game_intent = new Intent(this, Game.class);
        game_intent.putExtra(Game.EXTRA_DIFFICULTY, difficulty);

        startActivityForResult(game_intent, REQUEST_GAME_RESULT);
    }

    private void gotoAboutActivity() {

        Intent about_intent = new Intent(this, About.class);
        startActivity(about_intent);
    }

    private void gotoTutorialActivity(int difficulty) {

        Settings settings = new Settings(this);
        settings.setFirstGame(false);

        Intent tut_intent = new Intent(this, Tutorial.class);
        tut_intent.putExtra(Tutorial.EXTRA_DIFFICULTY, difficulty);

        startActivityForResult(tut_intent, REQUEST_TUT_RESULT);
    }

    private void createDefaultSettings(){

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

}
