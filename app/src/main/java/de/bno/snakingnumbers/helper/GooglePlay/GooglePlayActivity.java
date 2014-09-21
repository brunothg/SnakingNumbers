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

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by marvin on 21.09.14.
 */
public abstract class GooglePlayActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final String RESOLVING_ERROR_KEY = "resolvingError";
    private static final String DIALOG_ERROR_KEY = "dialogError";
    protected static final int RESOLVE_ERROR_REQUEST = 1001;

    private static final String ERROR_DIALOG_TAG = "errorDialog";

    private GoogleApiClient apiClient = null;
    private boolean resolvingError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(GooglePlayActivity.class.getName(), "onCreate");

        super.onCreate(savedInstanceState);

        apiClient = GooglePlayGame.getClient(this, this, this);

        if(savedInstanceState != null){

            resolvingError = savedInstanceState.getBoolean(RESOLVING_ERROR_KEY, false);
        }
    }

    @Override
    protected void onStart() {
        Log.d(GooglePlayActivity.class.getName(), "onStart");

        super.onStart();

        if (!resolvingError) {

            Log.d(GooglePlayActivity.class.getName(), "Connect");
            apiClient.connect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle save) {
        Log.d(GooglePlayActivity.class.getName(), "onSaveInstanceState");

        super.onSaveInstanceState(save);

        save.putBoolean(RESOLVING_ERROR_KEY, resolvingError);
    }

    @Override
    protected void onStop() {
        Log.d(GooglePlayActivity.class.getName(), "onStop");

        apiClient.disconnect();

        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(GooglePlayActivity.class.getName(), "onActivityResult [requestCode, resultCode] "+requestCode+" "+resultCode);

        if (requestCode == RESOLVE_ERROR_REQUEST) {

            resolvingError = false;

            if (resultCode == RESULT_OK) {

                retryConnecting();
            }else if(resultCode == RESULT_CANCELED){

                onUserAbortedConnection();
            }
        }
    }

    protected abstract void onUserAbortedConnection();

    protected void retryConnecting() {
        Log.d(GooglePlayActivity.class.getName(), "retryConnecting");


        resolvingError = false;

        if (!apiClient.isConnecting() && !apiClient.isConnected()) {

            apiClient.connect();
        }
    }

    private void onDialogDismissed() {
        Log.d(GooglePlayActivity.class.getName(), "onDialogDismissed");

        resolvingError = false;
    }

    @Override
    public abstract void onConnected(Bundle connectionHint);

    @Override
    public abstract void onConnectionSuspended(int cause);

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d(GooglePlayActivity.class.getName(), "onConnectionFailed");

        if (resolvingError) {

            return;
        } else if (result.hasResolution()) {

            try {

                resolvingError = true;
                result.startResolutionForResult(this, RESOLVE_ERROR_REQUEST);
            } catch (IntentSender.SendIntentException e) {

                retryConnecting();
            }
        } else {

            resolvingError = true;
            showErrorDialog(result.getErrorCode());
        }
    }

    private void showErrorDialog(int errorCode) {
        Log.d(GooglePlayActivity.class.getName(), "showErrorDialog");

        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();

        Bundle data = new Bundle();
        data.putInt(DIALOG_ERROR_KEY, errorCode);

        dialogFragment.setArguments(data);
        dialogFragment.show(getSupportFragmentManager(), ERROR_DIALOG_TAG);
    }

    protected GoogleApiClient getApiClient() {

        return apiClient;
    }

    protected boolean isResolvingError() {

        return resolvingError;
    }

    protected boolean isConnected() {

        return apiClient.isConnected();
    }


    public static class ErrorDialogFragment extends DialogFragment {

        public ErrorDialogFragment() {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int errorCode = this.getArguments().getInt(DIALOG_ERROR_KEY);

            return GooglePlayServicesUtil.getErrorDialog(errorCode, this.getActivity(), RESOLVE_ERROR_REQUEST);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            Log.d(GooglePlayActivity.class.getName(), "onDismiss");

            GooglePlayActivity activity = ((GooglePlayActivity) getActivity());
            if(activity == null){

                return;
            }

            activity.onDialogDismissed();
        }

    }
}
