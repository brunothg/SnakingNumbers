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

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import de.bno.snakingnumbers.game.logic.Zahlenschlange;

/**
 * Created by marvin on 11.09.14.
 */
public class FieldCreationFragment extends Fragment {

    /**
     * Callback interface through which the fragment will report the task's status
     */
    static interface Callback {
        void onStartLoading();

        void onFinishedLoading();
    }

    private Callback callback = null;
    private Zahlenschlange zahlenschlange = null;
    private FieldCreationTask task = null;

    /**
     * Keep Track of the actual Activity
     */
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        if (activity instanceof Callback) {

            callback = (Callback) activity;
        }
    }

    /**
     * Keep Track of the actual Activity
     */
    @Override
    public void onDetach() {

        super.onDetach();

        callback = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    public boolean createNewZahlenschlange(int dimension){

        setZahlenschlange(null);

        if(task != null){

            return false;
        }

        task = new FieldCreationTask();
        task.execute(dimension);

        return true;
    }

    public Zahlenschlange getZahlenschlange() {

        return zahlenschlange;
    }

    private void setZahlenschlange(Zahlenschlange zahlenschlange) {

        this.zahlenschlange = zahlenschlange;
    }

    /**
     * Creation Task. Create Zahlenschlange and inform about progress.
     */
    private class FieldCreationTask extends AsyncTask<Integer, Void, Zahlenschlange> {

        @Override
        protected void onPreExecute() {

            if (callback != null) {

                callback.onStartLoading();
            }
        }

        @Override
        protected Zahlenschlange doInBackground(Integer... params) {

            if (params.length <= 0) {

                return null;
            }

            return new Zahlenschlange(params[0].intValue(), (params.length >= 2) ? params[1] : -1);
        }

        @Override
        protected void onPostExecute(Zahlenschlange zahlenschlange) {

            setZahlenschlange(zahlenschlange);
            task = null;

            if (callback != null) {

                callback.onFinishedLoading();
            }
        }
    }
}
