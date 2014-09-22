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

package de.bno.snakingnumbers.game.gui;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.bno.snakingnumbers.helper.FullScreenActivity;
import de.bno.snakingnumbers.R;
import de.bno.snakingnumbers.game.logic.Position;
import de.bno.snakingnumbers.game.logic.Result;
import de.bno.snakingnumbers.game.logic.Zahlenschlange;
import de.bno.snakingnumbers.game.logic.time.OnTickListener;
import de.bno.snakingnumbers.game.logic.time.Timer;
import de.bno.snakingnumbers.helper.AsString;

/**
 * Created by marvin on 09.09.14.
 */
public class Game extends FullScreenActivity implements View.OnClickListener, OnClickListener, OnTickListener, FieldCreationFragment.Callback {

    public static final String EXTRA_ELAPSED_TIME = "extra_elapsed_time";
    public static final String EXTRA_DIFFICULTY = "extra_difficulty";
    public static final String EXTRA_CLICK_COUNT = "extra_click_count";
    public static final String EXTRA_MAX_NUMBER = "extra_max_number";

    public static final int DIFFICULTY_EASY = 5;
    public static final int DIFFICULTY_MEDIUM = 6;
    public static final int DIFFICULTY_HARD = 7;

    private static final int GAME_FINISHED_SUCCESSFUL = 0x01;
    private static final int GAME_FINISHED_FAILED = 0x00;
    private static final int GAME_FINISHED_NOT_YET = -1;

    private static final String BUNDLE_ELAPSED_TIME = "bundle_game_elapsed_time";
    private static final String BUNDLE_GAME_PAUSED = "bundle_game_paused";
    private static final String BUNDLE_GAME_CREATING = "bundle_game_creating";
    private static final String BUNDLE_GAME_FINISHED = "bundle_game_finished";
    private static final String BUNDLE_CLICK_COUNT = "bundle_click_count";

    private static final String TAG_FIELD_CREATION_FRAGMENT = "field_creation_fragment";

    public static final long MAX_TIME = 99 * 60000;
    public static final int MAX_CLICKS = 9999;

    private FrameLayout centeredFrameLayout;
    private FrameLayout centeredFullScreenFrameLayout;
    private SquareGridLayout gameSquareGridLayout;
    private TextView timerTimeTextView;
    private ImageButton timerPauseButton;
    private Button finishButton;
    private TextView clickCountTextView;

    private OverlayDialog centeredFullScreenDialog;
    private LoadingDialog loadingDialog;

    private boolean paused;
    private boolean creating;
    private int finished;

    private int difficulty;
    private int clickCount;
    private Timer timer;
    private Toast toast;
    private FieldCreationFragment fieldCreationFragment;

    //Lifetime cycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        paused = false;
        creating = false;
        finished = GAME_FINISHED_NOT_YET;

        noTitle();
        setContentView(R.layout.activity_game);

        fetchGUIElements();
        fetchExtras();
        fetchFieldCreationFragment();

        setTimer(new Timer(101));

        setGridToSize(difficulty);
        setTimerTime(0);
        setClickCount(0);
    }

    @Override
    protected void onRestart() {

        super.onRestart();

        if (creating) {

            showLoadingDialog();
        } else if (finished == GAME_FINISHED_NOT_YET) {

            pauseGame();
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

        requestFullscreen(true, false, false, false);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        restoreElapsedTime(savedInstanceState);
        restoreGameState(savedInstanceState);
        restoreClickCount(savedInstanceState);
    }

    @Override
    protected void onResume() {

        super.onResume();

        Log.d(Game.class.getName(), "Resume [creating, paused, finished, timerTime] " + creating + " " + paused + " " + finished + " " + timer.getElapsedTime());

        if (!creating) {
            if (fieldCreationFragment.getZahlenschlange() == null) {

                createGame();
            } else if (!paused && finished == GAME_FINISHED_NOT_YET) {

                startGame();
            } else {

                updateGridView();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        hideLoadingDialog();
        hideCenteredFullScreenDialog();

        super.onSaveInstanceState(outState);

        saveElapsedTime(outState);
        saveGameState(outState);
        saveClickCount(outState);
    }

    @Override
    protected void onPause() {

        Log.d(Game.class.getName(), "Pause [creating, paused, finished, timerTime] " + creating + " " + paused + " " + finished + " " + timer.getElapsedTime());

        super.onPause();

        timer.stop();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void finish() {

        updateResult();
        super.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && finished == GAME_FINISHED_NOT_YET) {

            showDoYouWantToExitDialog();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {

        if (v == null) {
            return;
        }


        if (!paused && !creating && finished == GAME_FINISHED_NOT_YET) {
            if (v == timerPauseButton) {

                pauseGame();
            } else if (v instanceof Field) {

                Field f = (Field) v;
                onFieldClick(f);
            }
        } else if (finished != GAME_FINISHED_NOT_YET) {
            if (v == finishButton) {

                finish();
            }
        }
    }

    @Override
    public void onClick(Fragment f) {

        if (f == null) {
            return;
        }

        if (f == centeredFullScreenDialog) {

            String title = centeredFullScreenDialog.getTitle();

            if (title.equals(getString(R.string.paused_game_title))) {

                startGame();
            }

            hideCenteredFullScreenDialog();
        }
    }

    @Override
    public void onTick(long elapsedTime) {

        if (timer.getElapsedTime() > MAX_TIME) {
            timer.stop();
            timer.setElapsedTime(MAX_TIME);
            finishGame(false);
        }
        updateTimerTime();
    }


    //Game lifecycle

    @Override
    public void onStartLoading() {

        Log.d(Game.class.getName(), "StartLoading");
        showLoadingDialog();
    }

    @Override
    public void onFinishedLoading() {

        Log.d(Game.class.getName(), "FinishedLoading " + AsString.fetch(fieldCreationFragment.getZahlenschlange().getSolution()));
        hideLoadingDialog();
        updateGridView();
        creating = false;

        startGame();
    }

    /**
     * Create a new game depending on the current difficulty.
     */
    private void createGame() {

        Log.d(Game.class.getName(), "Create Game");

        creating = true;

        clickCount = 0;
        timer.stop();
        timer.resetTime();
        updateTimerTime();

        fieldCreationFragment.createNewZahlenschlange(difficulty);
    }

    /**
     * Start or restart actual game instance.
     */
    private void startGame() {

        Log.d(Game.class.getName(), "Start Game");

        updateGridView();
        timer.start();
        paused = false;
    }

    /**
     * Pause the actual game.
     */
    private void pauseGame() {

        Log.d(Game.class.getName(), "Pause Game");

        paused = true;

        timer.stop();
        updateTimerTime();
        showPauseDialog();
    }

    /**
     * Finish the actual game.
     *
     * @param successful True if the user successfully found a solution. False otherwise.
     */
    private void finishGame(boolean successful) {

        Log.d(Game.class.getName(), "Finish Game " + successful);

        finished = (successful) ? GAME_FINISHED_SUCCESSFUL : GAME_FINISHED_FAILED;

        timer.stop();
        updateTimerTime();

        timerPauseButton.setVisibility(ImageButton.INVISIBLE);
        finishButton.setVisibility(Button.VISIBLE);

        updateFinishButtonText();
    }

    /**
     * Save the actual game state.
     *
     * @param outState Bundle for saving purpose
     */
    private void saveGameState(Bundle outState) {

        Log.d(Game.class.getName(), "Save Game");

        outState.putBoolean(BUNDLE_GAME_PAUSED, paused);
        outState.putBoolean(BUNDLE_GAME_CREATING, creating);
        outState.putInt(BUNDLE_GAME_FINISHED, finished);
    }


    /**
     * Restore the actual game state.
     *
     * @param savedInstanceState Bundle for loading purpose
     */
    private void restoreGameState(Bundle savedInstanceState) {

        Log.d(Game.class.getName(), "Restore Game");

        if (savedInstanceState.getBoolean(BUNDLE_GAME_PAUSED, false)) {

            pauseGame();
        }

        if ((creating = savedInstanceState.getBoolean(BUNDLE_GAME_CREATING, false))) {

            showLoadingDialog();
        }

        finished = savedInstanceState.getInt(BUNDLE_GAME_FINISHED, GAME_FINISHED_NOT_YET);
        if (finished != GAME_FINISHED_NOT_YET) {

            timerPauseButton.setVisibility(ImageButton.INVISIBLE);
            finishButton.setVisibility(Button.VISIBLE);
            updateFinishButtonText();
        }


    }

    //Helper methods

    /**
     * Setting the result depending on finishing state and the elapsed time.
     * Use before finish().
     */
    private void updateResult() {

        Intent result = new Intent();
        result.putExtra(EXTRA_ELAPSED_TIME, (long) timer.getElapsedTime());
        result.putExtra(EXTRA_DIFFICULTY, (int) difficulty);
        result.putExtra(EXTRA_CLICK_COUNT, (int) clickCount);
        result.putExtra(EXTRA_MAX_NUMBER, (int) getMaxNumber());
        setResult((finished == GAME_FINISHED_SUCCESSFUL) ? RESULT_OK : RESULT_CANCELED, result);
    }

    /**
     * MaxNumber Value
     *
     * @return Zahlenschlange.getMaxNumber() or -1 if zahlenschlange null
     */
    private int getMaxNumber() {

        int ret = -1;

        if (fieldCreationFragment != null && fieldCreationFragment.getZahlenschlange() != null) {

            ret = fieldCreationFragment.getZahlenschlange().getMaxNumber();
        }

        return ret;
    }

    /**
     * Fetch FieldCreationFragment or create a new one if it is the first time.
     */
    private void fetchFieldCreationFragment() {

        FragmentManager fragmentManager = getFragmentManager();

        Fragment fragment = fragmentManager.findFragmentByTag(TAG_FIELD_CREATION_FRAGMENT);

        if (fragment != null && fragment instanceof FieldCreationFragment) {

            fieldCreationFragment = (FieldCreationFragment) fragment;
        } else {

            fieldCreationFragment = new FieldCreationFragment();
            fragmentManager.beginTransaction().add(fieldCreationFragment, TAG_FIELD_CREATION_FRAGMENT).commit();
        }
    }

    /**
     * Fetch all required extras from this Activity or use default values.
     */
    private void fetchExtras() {

        difficulty = getIntent().getIntExtra(EXTRA_DIFFICULTY, DIFFICULTY_EASY);
    }

    /**
     * Fetch all required View's from this Activity
     * Should be called in onCreate after the Layout is inflated
     */
    private void fetchGUIElements() {

        centeredFrameLayout = (FrameLayout) findViewById(R.id.centerDialogFrameLayout);
        centeredFullScreenFrameLayout = (FrameLayout) findViewById(R.id.fullDialogFrameLayout);
        gameSquareGridLayout = (SquareGridLayout) findViewById(R.id.sqaureGrid);
        timerTimeTextView = (TextView) findViewById(R.id.tvTimer);
        clickCountTextView = (TextView) findViewById(R.id.tvClickCount);

        timerPauseButton = (ImageButton) findViewById(R.id.btnPause);
        timerPauseButton.setOnClickListener(this);

        finishButton = (Button) findViewById(R.id.btnFinish);
        finishButton.setOnClickListener(this);
    }

    /**
     * Change the squareGrids's dimension
     *
     * @param dimension Dimension the squareGrid should have.
     */
    private void setGridToSize(int dimension) {

        gameSquareGridLayout.removeAllViews();
        gameSquareGridLayout.setColumnCount(dimension);
        gameSquareGridLayout.setRowCount(dimension);

        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {

                Field tmpField = new Field(this);
                tmpField.setIndex(x + y * dimension);
                tmpField.setOnClickListener(this);
                tmpField.setBackgroundColor(Color.WHITE);

                //Update border
                tmpField.setBorderWidth(getDIP(1));

                //Update border

                gameSquareGridLayout.addView(tmpField);
            }
        }
    }

    /**
     * Updates the gridLayout with the actual game data if existing.
     */
    private void updateGridView() {

        if (fieldCreationFragment == null || fieldCreationFragment.getZahlenschlange() == null) {
            return;
        }

        Zahlenschlange gameLogic = fieldCreationFragment.getZahlenschlange();

        int index = 0;
        for (int y = 0; y < difficulty; y++) {
            for (int x = 0; x < difficulty; x++) {

                View v = gameSquareGridLayout.getChildAt(index);
                if (v instanceof Field) {
                    Field f = (Field) v;

                    f.setNumber(gameLogic.getValue(x, y));
                    f.setDirection(Field.DIRECTION_NONE);
                    f.setDirection2(Field.DIRECTION_NONE);
                    f.setBodyStyle(Field.BODY_NORMAL);
                }
                index++;
            }
        }

        Field first = (Field) gameSquareGridLayout.getChildAt(0);
        Field last = (Field) gameSquareGridLayout.getChildAt(index - 1);

        first.setBodyStyle(Field.BODY_TAIL);
        last.setBodyStyle(Field.BODY_HEAD);

        drawPath(gameLogic.getPath());
    }

    /**
     * Draws the given path on the gridLayout's fields.
     *
     * @param path The path to draw
     */
    private void drawPath(List<Position> path) {

        for (int i = 0; i < path.size(); i++) {

            Position p = path.get(i);

            int index = p.getX() + p.getY() * difficulty;
            View v = gameSquareGridLayout.getChildAt(index);

            if (v != null && v instanceof Field) {
                Field f = (Field) v;

                Position before = null;
                Position after = null;

                if (index == 0) {
                    f.setBodyStyle(Field.BODY_TAIL);
                } else if (index == (difficulty * difficulty) - 1) {
                    f.setBodyStyle(Field.BODY_HEAD);
                } else {
                    f.setBodyStyle(Field.BODY_NORMAL);
                }

                if (i > 0) {
                    before = path.get(i - 1);
                }
                if (i < path.size() - 1) {
                    after = path.get(i + 1);
                }

                int xDif;
                int yDif;

                if (before != null) {

                    xDif = p.getX() - before.getX(); //-Rechts +Links
                    yDif = p.getY() - before.getY(); //-Unten +Oben

                    f.setDirection(getDirectionFromDif(xDif, yDif));
                }

                if (after != null) {

                    xDif = p.getX() - after.getX(); //-Rechts +Links
                    yDif = p.getY() - after.getY(); //-Unten +Oben

                    f.setDirection2(getDirectionFromDif(xDif, yDif));
                }

            }
        }
    }

    private void onFieldClick(Field f) {

        if (f == null || fieldCreationFragment == null || fieldCreationFragment.getZahlenschlange() == null) {
            return;
        }

        if (clickCount < MAX_CLICKS) {
            clickCount++;
        }
        updateClickCount();

        Zahlenschlange gameLogic = fieldCreationFragment.getZahlenschlange();

        int index = f.getIndex();
        int y = index / difficulty;
        index = index % difficulty;
        int x = index;

        Position end = gameLogic.getPathEnd();
        if (end.getX() == x && end.getY() == y) {

            gameLogic.undo();
            updateGridView();
            return;
        }

        Result res = gameLogic.makeStepTo(x, y);
        if (res.isOk() || res.isGameFinished()) {

            updateGridView();

            if (res.isGameFinished()) {

                finishGame(true);
            }
        } else if (res.isNotAllowed()) {

            showToast(getString(R.string.operation_not_allowed));
        }
    }

    /**
     * Calculate the direction from the difference of two positions.
     *
     * @param xDif X-Coordinate difference
     * @param yDif Y-Coordinate difference
     * @return The field direction constant
     */
    private int getDirectionFromDif(int xDif, int yDif) {

        int dir = Field.DIRECTION_NONE;

        if (xDif > 0) {
            dir = Field.DIRECTION_LEFT;
        } else if (xDif < 0) {
            dir = Field.DIRECTION_RIGHT;
        } else if (yDif > 0) {
            dir = Field.DIRECTION_UP;
        } else if (yDif < 0) {
            dir = Field.DIRECTION_DOWN;
        }

        return dir;
    }

    /**
     * Convert dp in px
     *
     * @param dip Dimesion as complex dip unit
     * @return usable pixel float value
     */
    private float getDIP(float dip) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    /**
     * Set the click count value from it's view
     *
     * @param clickCount
     */
    private void setClickCount(int clickCount) {

        clickCountTextView.setText(String.format(getString(R.string.click_count_format), clickCount));
    }

    /**
     * Update the click count view
     */
    private void updateClickCount() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                setClickCount(clickCount);
            }
        });
    }

    /**
     * Set the actual timer instance. Removes old ones and initializes Listeners.
     *
     * @param timer The new Timer
     */
    private void setTimer(Timer timer) {

        if (this.timer != null) {

            this.timer.stop();
            this.timer.setListener(null);
        }

        this.timer = timer;
        this.timer.setListener(this);
    }

    /**
     * Updates the time shown on the Timer TextView with the time from the timer's actual elapsedTime
     */
    private void updateTimerTime() {

        if (timer == null) {
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                setTimerTime(timer.getElapsedTime());
            }
        });
    }

    /**
     * Store elapsed time
     *
     * @param save Bundle where the elapsed time will be stored
     */
    private void saveElapsedTime(Bundle save) {

        if (timer == null) {
            return;
        }

        save.putLong(BUNDLE_ELAPSED_TIME, timer.getElapsedTime());
    }

    /**
     * Restore elapsed time
     *
     * @param load Bundle where the elapsed time can be found
     */
    private void restoreElapsedTime(Bundle load) {

        if (timer == null) {
            return;
        }

        timer.stop();
        timer.resetTime();
        timer.setElapsedTime(load.getLong(BUNDLE_ELAPSED_TIME, 0));
        updateTimerTime();
    }

    /**
     * Save click count
     *
     * @param save Bundle where the click count will be stored
     */
    private void saveClickCount(Bundle save) {

        save.putInt(BUNDLE_CLICK_COUNT, clickCount);
    }

    /**
     * Restore click count
     *
     * @param load Bundle where the click count can be found
     */
    private void restoreClickCount(Bundle load) {

        clickCount = load.getInt(BUNDLE_CLICK_COUNT, 0);
        updateClickCount();
    }

    /**
     * Set the time shown on the Timer TextView
     *
     * @param time The time to set in milliseconds
     */
    private void setTimerTime(long time) {

        if (timerTimeTextView == null) {
            return;
        }

        int min = (int) (time / 60000);
        time %= 60000;

        int sec = (int) (time / 1000);
        time %= 1000;

        int milli = (int) (time / 10);

        String timeString = String.format(getString(R.string.timer_format), min, sec, milli);
        timerTimeTextView.setText(timeString);

    }

    /**
     * Show a centered fullscreen dialog with given title and information text.
     * Doesn't do anything if already shown. Hide before show a new one.
     *
     * @param title Dialog title text
     * @param info  Dialog info text
     */
    private void showCenteredFullScreenDialog(String title, String info) {

        if (centeredFullScreenDialog != null) {

            return;
        }

        FragmentManager fragmentManager = getFragmentManager();

        centeredFullScreenDialog = OverlayDialog.newInstance(title, info);
        fragmentManager.beginTransaction().add(R.id.fullDialogFrameLayout, centeredFullScreenDialog).commit();
    }

    /**
     * Shows a pause dialog
     */
    private void showPauseDialog() {

        showCenteredFullScreenDialog(getString(R.string.paused_game_title), getString(R.string.paused_game_info));
    }

    /**
     * Hide centered fullscreen dialog. If it is not visible nothing is done.
     */
    private void hideCenteredFullScreenDialog() {

        if (centeredFullScreenDialog == null) {

            return;
        }

        try {

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().remove(centeredFullScreenDialog).commit();
        } finally {

            centeredFullScreenDialog = null;
        }
    }

    /**
     * Shows the loading dialog
     */
    private void showLoadingDialog() {

        if (loadingDialog != null) {
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();

        loadingDialog = LoadingDialog.newInstance(getString(R.string.loading_game_title), getString(R.string.loading_game_info));
        fragmentManager.beginTransaction().add(R.id.centerDialogFrameLayout, loadingDialog).commit();
    }

    /**
     * Hides the loading dialog
     */
    private void hideLoadingDialog() {

        if (loadingDialog == null) {
            return;
        }

        try {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().remove(loadingDialog).commit();
        } finally {

            loadingDialog = null;
        }

    }

    /**
     * Show a short message
     *
     * @param message The message to publish
     */
    private void showToast(String message) {

        if (toast == null) {

            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        }

        toast.setText(message);
        toast.show();
    }

    /**
     * Hide the toast message if visible
     */
    private void hideToast() {

        if (toast == null) {
            return;
        }

        toast.cancel();
    }

    /**
     * shows a dialog asking for the user's preference about quitting this activity.
     * if true finish() will be called.
     */
    private void showDoYouWantToExitDialog() {

        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.title_sure_exit).setMessage(R.string.message_sure_exit).setNegativeButton(R.string.no_sure_exit, null).setPositiveButton(R.string.yes_sure_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Game.this.finish();
            }
        }).show();
    }

    private void updateFinishButtonText() {

        if (finished == GAME_FINISHED_FAILED) {

            finishButton.setText(R.string.finish_btn_fail);
            drawPath(fieldCreationFragment.getZahlenschlange().getSolution());
        } else {

            finishButton.setText(R.string.finish_btn);
        }
    }
}
