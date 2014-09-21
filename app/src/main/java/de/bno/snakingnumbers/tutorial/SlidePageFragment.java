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

package de.bno.snakingnumbers.tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.bno.snakingnumbers.R;


public class SlidePageFragment extends Fragment {

    private static final String CONTENT_ID = "content_id";

    private int contentId;

    public static SlidePageFragment newInstance(int pageId) {

        SlidePageFragment fragment = new SlidePageFragment();

        Bundle args = new Bundle();
        args.putInt(CONTENT_ID, pageId);

        fragment.setArguments(args);

        return fragment;
    }

    public SlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            contentId = getArguments().getInt(CONTENT_ID, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_screen_slide, container, false);

        if (contentId == 0) {

            return v;
        }

        ViewGroup viewContainer = (ViewGroup) v.findViewById(R.id.viewContainer);
        inflater.inflate(contentId, viewContainer, true);

        return v;
    }


}
