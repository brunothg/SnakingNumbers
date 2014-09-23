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
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.games.GamesActivityResultCodes;

import de.bno.snakingnumbers.R;
import de.bno.snakingnumbers.data.Settings;
import de.bno.snakingnumbers.helper.GooglePlay.GooglePlayActivity;
import de.bno.snakingnumbers.helper.GooglePlay.GooglePlayGame;
import de.bno.snakingnumbers.helper.Network;
import de.bno.snakingnumbers.result.GameResult;

public class SettingsActivity extends GooglePlayActivity {

    private static final String NETWORK_ERROR_DIALOG_TAG = "networkErrorDialog";
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = new Settings(this);
        addFragment();
    }

    private void addFragment() {

        getFragmentManager().beginTransaction().replace(R.id.settings_layout, new SettingsFragment()).commit();
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

    private void checkNetwork() {
        Log.d(GameResult.class.getName(), "checkNetwork " + settings.isExplicitOffline() + " " + Network.isNetworkConnectionAvailable(this));

        if (!Network.isNetworkConnectionAvailable(this) && (!settings.isExplicitOffline())) {
            Log.d(GameResult.class.getName(), "NetworkErrorDialog");
            new GooglePlayGame.NetworkErrorDialogFragment().show(getSupportFragmentManager(), NETWORK_ERROR_DIALOG_TAG);
        }
    }

    @Override
    protected boolean autoStartConnection() {
        return (!settings.isExplicitOffline()) && Network.isNetworkConnectionAvailable(this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        settings.setFirstServiceTry(false);
        settings.setExplicitOffline(false);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        retryConnecting();
    }

    @Override
    protected View getViewForPopups() {
        return findViewById(R.id.main_layout);
    }

}
