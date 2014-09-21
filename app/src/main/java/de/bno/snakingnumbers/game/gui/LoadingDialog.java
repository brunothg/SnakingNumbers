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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.bno.snakingnumbers.R;


public class LoadingDialog extends Fragment {

    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_INFO = "arg_info";

    private String mTilte;
    private String mInfo;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Title.
     * @param info  Information text.
     * @return A new instance of fragment LoadingDialog.
     */
    public static LoadingDialog newInstance(String title, String info) {

        LoadingDialog fragment = new LoadingDialog();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_INFO, info);

        fragment.setArguments(args);

        return fragment;
    }

    public LoadingDialog() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            mTilte = getArguments().getString(ARG_TITLE, "Title");
            mInfo = getArguments().getString(ARG_INFO, "Info text");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_loading_dialog, container, false);

        ((TextView) view.findViewById(R.id.tv_title)).setText(mTilte);
        ((TextView) view.findViewById(R.id.tv_Info)).setText(mInfo);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

}
