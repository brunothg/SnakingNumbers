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
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;


/**
 * Created by marvin on 20.09.14.
 */
public class GooglePlayGame {


    public static GoogleApiClient getClient(Context context, GoogleApiClient.ConnectionCallbacks callbacks, GoogleApiClient.OnConnectionFailedListener failedListener) {

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context);

        if (callbacks != null) {

            builder = builder.addConnectionCallbacks(callbacks);
        }

        if (failedListener != null) {

            builder = builder.addOnConnectionFailedListener(failedListener);
        }

        builder = builder.addApi(Games.API).addScope(Games.SCOPE_GAMES);

        return builder.build();
    }

}
