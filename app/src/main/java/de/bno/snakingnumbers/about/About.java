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

package de.bno.snakingnumbers.about;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.bno.snakingnumbers.R;


public class About extends Activity {

    private ExpandableListView lvAbout;
    private ExpandableListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        fetchGUIElements();

        listAdapter = getListAdapter(false);
        lvAbout.setAdapter(listAdapter);

        if (listAdapter.getGroupCount() > 0) {
            lvAbout.expandGroup(0);
        }
    }

    private ExpandableListAdapter getListAdapter(boolean invertOrder) {

        Log.d(About.class.getName(), String.format("getListAdapter(%b)", invertOrder));

        List<String> order = new LinkedList<String>();
        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

        String[] values = getResources().getStringArray(R.array.about_values);

        for (String value : values) {

            String[] headerAndChild = splitIntoHeaderAndChild(value);

            order.add(headerAndChild[0]);

            List<String> valueWrapper = new ArrayList<String>(1);
            valueWrapper.add(headerAndChild[1]);
            listDataChild.put(headerAndChild[0], valueWrapper);
        }

        if (invertOrder) {

            Collections.reverse(order);
        }

        return (listAdapter != null) ? listAdapter : new ExpandableListAdapter(this, order, listDataChild);
    }

    private String[] splitIntoHeaderAndChild(String value) {

        String[] ret = new String[2];

        int index = value.indexOf(':');

        ret[0] = value.substring(0, (index >= 0) ? index : value.length());
        ret[1] = value.substring((index >= 0) ? index + 1 : value.length());

        return ret;
    }

    private void fetchGUIElements() {

        lvAbout = (ExpandableListView) findViewById(R.id.lvAbout);
    }

}
