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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by marvin on 16.09.14.
 */
public class SlidePageAdapter extends FragmentStatePagerAdapter {


    private int [] pageIds;

    public SlidePageAdapter(int[] pageIds, FragmentManager manager){

        super(manager);

        this.pageIds = pageIds;
    }


    @Override
    public Fragment getItem(int position) {

        return SlidePageFragment.newInstance(pageIds[position]);
    }

    @Override
    public int getCount() {

        return pageIds.length;
    }
}
