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

package de.bno.snakingnumbers.result;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.games.GamesActivityResultCodes;

import de.bno.snakingnumbers.R;
import de.bno.snakingnumbers.data.Settings;
import de.bno.snakingnumbers.game.gui.Game;
import de.bno.snakingnumbers.helper.GooglePlay.Achievements;
import de.bno.snakingnumbers.helper.GooglePlay.GooglePlayActivity;
import de.bno.snakingnumbers.helper.GooglePlay.GooglePlayGame;
import de.bno.snakingnumbers.helper.GooglePlay.Highscores;
import de.bno.snakingnumbers.helper.Network;

public class GameResult extends GooglePlayActivity implements View.OnClickListener {

    public static final String DIFFICULTY_EXTRA = "difficulty_extra";
    public static final String TIME_EXTRA = "time_extra";
    public static final String CLICK_EXTRA = "click_extra";
    public static final String MAX_NUMBER_EXTRA = "max_number_extra";

    private static final String NETWORK_ERROR_DIALOG_TAG = "network_error_dialog_tag";
    private static final String FIRST_SIGN_ATTEMPT_DIALOG_TAG = "first_sign_attempt_dialog_tag";

    private int difficulty;
    private long time;
    private int clicks;
    private int max_number;
    private float prozent;

    private ImageView ivDifficulty;
    private TextView tvTime;
    private TextView tvClicks;
    private Button btnOk;

    private Settings settings;
    private boolean playServiceWorkDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getData();
        if (!verifyData()) {
            finish();
        }

        setContentView(R.layout.activity_game_result);

        fetchGUIElements();
        updateGUI();

        settings = new Settings(this);

        checkFirstSinginAttempt();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.gameresult_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {

            settings.setExplicitOffline(false);
            playServiceWorkDone = false;
            retryConnecting();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v == btnOk) {

            finish();
        }
    }

    @Override
    protected void onUserAbortedConnection(int resultCode) {
        Log.d(GameResult.class.getName(), "onUserAbortedConnection " + resultCode);

        if (resultCode == RESULT_CANCELED || resultCode == GamesActivityResultCodes.RESULT_LICENSE_FAILED) {

            settings.setExplicitOffline(true);
        } else if (resultCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED) {

            reconnect();
        } else if (resultCode == GamesActivityResultCodes.RESULT_NETWORK_FAILURE) {

            checkNetwork();
        } else {

            Log.e(GameResult.class.getName(), "onUserAbortedConnection unhandled " + resultCode);
        }

        settings.setFirstServiceTry(false);
    }

    @Override
    protected boolean autoStartConnection() {

        return (!settings.isExplicitOffline()) && Network.isNetworkConnectionAvailable(this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        settings.setFirstServiceTry(false);
        settings.setExplicitOffline(false);

        doPlayServiceWork();
    }

    @Override
    public void onConnectionSuspended(int cause) {

        retryConnecting();
    }

    @Override
    public void retryConnecting() {

        if (!playServiceWorkDone) {

            super.retryConnecting();
        }
    }

    protected View getViewForPopups() {

        return findViewById(R.id.main_layout);
    }

    private void getData() {

        Intent data = getIntent();

        difficulty = data.getIntExtra(DIFFICULTY_EXTRA, -1);
        time = data.getLongExtra(TIME_EXTRA, -1);
        clicks = data.getIntExtra(CLICK_EXTRA, -1);
        max_number = data.getIntExtra(MAX_NUMBER_EXTRA, -1);
        prozent = ((100f / (max_number - 1)) * (clicks)) - 100;
    }


    private boolean verifyData() {

        if (difficulty == -1 || time == -1 || clicks == -1 || max_number == -1) {
            Log.e(GameResult.class.getName(), "verified data wrong [dif, t, cl, mx_num] " + difficulty + " " + time + " " + clicks + " " + max_number);

            return false;
        }

        return true;
    }

    private void fetchGUIElements() {

        ivDifficulty = (ImageView) findViewById(R.id.ivDifficulty);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvClicks = (TextView) findViewById(R.id.tvClicks);

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
    }


    private void updateGUI() {

        //DIFFICULTY
        switch (difficulty) {
            case Game.DIFFICULTY_EASY:
                ivDifficulty.setImageDrawable(getResources().getDrawable(R.drawable.easy));
                break;
            case Game.DIFFICULTY_MEDIUM:
                ivDifficulty.setImageDrawable(getResources().getDrawable(R.drawable.medium));
                break;
            case Game.DIFFICULTY_HARD:
                ivDifficulty.setImageDrawable(getResources().getDrawable(R.drawable.hard));
                break;
        }

        //TIME
        long time = this.time;
        int min, sec, milli;

        min = (int) (time / 60000);
        time %= 60000;

        sec = (int) (time / 1000);
        time %= 1000;

        milli = (int) (time / 10);

        tvTime.setText(String.format(getString(R.string.timer_format), min, sec, milli));

        //CLICKS
        tvClicks.setText(String.format(getString(R.string.click_count_format_result), clicks, prozent));
    }

    private void checkNetwork() {
        Log.d(GameResult.class.getName(), "checkNetwork " + settings.isExplicitOffline() + " " + Network.isNetworkConnectionAvailable(this));

        if (!Network.isNetworkConnectionAvailable(this) && (!settings.isExplicitOffline())) {
            Log.d(GameResult.class.getName(), "NetworkErrorDialog");
            new GooglePlayGame.NetworkErrorDialogFragment().show(getSupportFragmentManager(), NETWORK_ERROR_DIALOG_TAG);
        }
    }



    private void checkFirstSinginAttempt() {

        if (!settings.isFirstServiceTry() && !settings.isExplicitOffline()) {
            return;
        }

        Log.d(GameResult.class.getName(), "FirstSignAttemptDialog");
        settings.setFirstServiceTry(false);
        GooglePlayGame.FirstSignAttemptDialogFragment.create(settings, this).show(getSupportFragmentManager(), FIRST_SIGN_ATTEMPT_DIALOG_TAG);
    }



    private void doPlayServiceWork() {


        Achievements.handleGameResult(difficulty, time, clicks, prozent, getApiClient(), this);
        Highscores.handleGameResult(difficulty, time, clicks, max_number - 1, getApiClient(), this);

        playServiceWorkDone = true;
    }
}
