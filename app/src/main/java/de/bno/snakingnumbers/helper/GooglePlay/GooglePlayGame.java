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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import de.bno.snakingnumbers.R;
import de.bno.snakingnumbers.data.Settings;


/**
 * Created by marvin on 20.09.14.
 */
public class GooglePlayGame {


    public static GoogleApiClient getClient(Context context, GoogleApiClient.ConnectionCallbacks callbacks, GoogleApiClient.OnConnectionFailedListener failedListener, View popupView) {

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context);

        if (callbacks != null) {

            builder = builder.addConnectionCallbacks(callbacks);
        }

        if (failedListener != null) {

            builder = builder.addOnConnectionFailedListener(failedListener);
        }

        builder = builder.addApi(Games.API).addScope(Games.SCOPE_GAMES);

        if(popupView != null) {
            builder = builder.setViewForPopups(popupView);
        }

        return builder.build();
    }

    public static class NetworkErrorDialogFragment extends DialogFragment {

        public NetworkErrorDialogFragment() {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.result_error_network_title).setMessage(R.string.result_error_network_message).setPositiveButton(android.R.string.ok, null);

            return builder.create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
        }

    }

    public static class FirstSignAttemptDialogFragment extends DialogFragment {

        public Settings settings;
        public GooglePlayActivity gameResult;

        public static FirstSignAttemptDialogFragment create(Settings setting, GooglePlayActivity game) {

            FirstSignAttemptDialogFragment ret = new FirstSignAttemptDialogFragment();
            ret.settings = setting;
            ret.gameResult = game;

            return ret;
        }

        public FirstSignAttemptDialogFragment() {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.result_first_sign_title).setMessage(R.string.result_first_sign_message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (settings == null || gameResult == null) {
                        return;
                    }

                    settings.setExplicitOffline(false);
                    gameResult.retryConnecting();
                }
            }).setNegativeButton(android.R.string.no, null);

            return builder.create();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
        }

    }
}
