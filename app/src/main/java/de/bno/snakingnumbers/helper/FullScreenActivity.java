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

package de.bno.snakingnumbers.helper;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;

/**
 * Created by marvin on 06.09.14.
 */
public abstract class FullScreenActivity extends Activity {

    /**
     * Disable Title Bar
     */
    protected void noTitle() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * Try set Fullscreen features depending on API level
     *
     * @param statusBar     true if you want to hide the status bar
     * @param navigationBar true if you want to hide the navigation bar
     * @param sticky        true if you want to enter immersive sticky mode
     * @param immersive     true if you want to enter immersive mode. If true ignores sticky flag
     */
    protected void requestFullscreen(boolean statusBar, boolean navigationBar, boolean sticky, boolean immersive) {

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;


        if (navigationBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        if (statusBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
            newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }

        if (!immersive && sticky && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        if (immersive && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }
}
