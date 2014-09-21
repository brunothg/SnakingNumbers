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
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.bno.snakingnumbers.R;


public class OverlayDialog extends Fragment implements View.OnClickListener {

    private static final String ARG_TITLE = "title";
    private static final String ARG_INFO = "info";

    private String mTitle;
    private String mInfo;

    private TextView tvTitle;
    private TextView tvInfo;
    private RelativeLayout layout;
    private OnClickListener listener;

    public static OverlayDialog newInstance(String param1, String param2) {

        OverlayDialog fragment = new OverlayDialog();

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, param1);
        args.putString(ARG_INFO, param2);

        fragment.setArguments(args);

        return fragment;
    }

    public OverlayDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE, "TITLE");
            mInfo = getArguments().getString(ARG_INFO, "INFO");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overlay_dialog, container, false);

        fetchGUI(view);

        tvTitle.setText(getTitle());
        tvInfo.setText(getInfo());

        return view;
    }

    private void fetchGUI(View v) {

        tvTitle = (TextView) v.findViewById(R.id.tvDialogTitle);
        tvInfo = (TextView) v.findViewById(R.id.tvDialogInfo);
        layout = (RelativeLayout) v.findViewById(R.id.overlayDialog);

        layout.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        tvInfo.setOnClickListener(this);
    }

    public void setTitle(String s) {

        if (tvTitle != null) {

            tvTitle.setText(s);
            mTitle = s;
        }
    }

    public void setInfo(String s) {

        if (tvTitle != null) {

            tvTitle.setText(s);
            mInfo = s;
        }
    }

    public String getTitle(){

        return mTitle;
    }

    public String getInfo(){

        return mInfo;
    }

    @Override
    public void onAttach(Activity a) {

        super.onAttach(a);

        if (a instanceof OnClickListener) {
            setOnClickListener((OnClickListener) a);
        } else {

            throw new RuntimeException("Has to implement View.OnClickListener");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();

        setOnClickListener(null);
    }

    private void setOnClickListener(OnClickListener a) {

        this.listener = a;
    }

    @Override
    public void onClick(View v) {

        if (listener != null) {

            listener.onClick(this);
        }
    }
}
