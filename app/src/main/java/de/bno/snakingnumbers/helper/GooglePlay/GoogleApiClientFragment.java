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
import android.support.v4.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by marvin on 21.09.14.
 */
public class GoogleApiClientFragment extends Fragment {

    private GoogleApiClient apiClient;

    public GoogleApiClientFragment(){

    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

    }

    @Override
    public void onDetach() {

        if(getActivity() instanceof GoogleApiClient.ConnectionCallbacks){

            apiClient.unregisterConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) getActivity());
        }

        if(getActivity() instanceof GoogleApiClient.OnConnectionFailedListener){

            apiClient.unregisterConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) getActivity());
        }

        super.onDetach();
    }

    public GoogleApiClient getApiClient(){

        return apiClient;
    }

    public void setApiClient(GoogleApiClient apiClient){

        this.apiClient = apiClient;
    }
}
