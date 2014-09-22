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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.bno.snakingnumbers.R;
import de.bno.snakingnumbers.game.gui.Game;
import de.bno.snakingnumbers.helper.GooglePlay.GooglePlayActivity;

public class GameResult extends GooglePlayActivity implements View.OnClickListener {

    public static final String DIFFICULTY_EXTRA = "difficulty_extra";
    public static final String TIME_EXTRA = "time_extra";
    public static final String CLICK_EXTRA = "click_extra";
    public static final String MAX_NUMBER_EXTRA = "max_number_extra";


    private int difficulty;
    private long time;
    private int clicks;
    private int max_number;
    private float prozent;

    private ImageView ivDifficulty;
    private TextView tvTime;
    private TextView tvClicks;
    private Button btnOk;

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
    }

    @Override
    public void onClick(View v) {

        if(v == btnOk){

            finish();
        }
    }

    @Override
    protected void onUserAbortedConnection(int resultCode) {

    }

    @Override
    protected boolean autoStartConnection() {

        return false;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
    }

    @Override
    public void onConnectionSuspended(int cause) {
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

}
