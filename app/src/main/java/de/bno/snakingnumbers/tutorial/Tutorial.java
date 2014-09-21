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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import de.bno.snakingnumbers.R;
import de.bno.snakingnumbers.helper.Dimension;

public class Tutorial extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {


    public static final String EXTRA_DIFFICULTY = "extra_difficulty";

    private static final int[] pages = new int[]{R.layout.tutorial_page_goal, R.layout.tutorial_page_controls, R.layout.tutorial_page_rules, R.layout.tutorial_page_start};

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private ImageButton btnNext;
    private ImageButton btnPrevious;

    private ViewGroup indicatorLayout;
    private int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        noTitle();

        difficulty = getIntent().getIntExtra(EXTRA_DIFFICULTY, -1);

        setContentView(R.layout.activity_tutorial);
        fetchGUI();


        pagerAdapter = new SlidePageAdapter(pages, getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        updateIndicator(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (pager.getCurrentItem() > 0) {

                gotoPreviousPage();
                return true;
            } else {

                startGame();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * Disable Title Bar
     */
    protected void noTitle() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void fetchGUI() {

        indicatorLayout = (ViewGroup) findViewById(R.id.indicator_layout);

        btnNext = (ImageButton) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        btnPrevious = (ImageButton) findViewById(R.id.btn_previous);
        btnPrevious.setOnClickListener(this);

        pager = (ViewPager) findViewById(R.id.slide_pager);
        pager.setOnPageChangeListener(this);
    }

    private void updateIndicator(int index) {

        int numberOfPages = pages.length;

        indicatorLayout.removeAllViews();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) Dimension.getDIP(20, this), (int) Dimension.getDIP(20, this));

        for (int i = 0; i < numberOfPages; i++) {

            ImageView img = new ImageView(this);

            int padding = (int) Dimension.getDIP(5, this);
            img.setPadding(padding, padding, padding, padding);

            if (i == index) {

                img.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_active));
            } else {

                img.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator));
            }

            indicatorLayout.addView(img, params);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == btnNext) {

            gotoNextPage();
        } else if (v == btnPrevious) {

            gotoPreviousPage();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        updateIndicator(position);

        if (position == pages.length - 1) {

            startGame();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void gotoPreviousPage() {

        pager.setCurrentItem(pager.getCurrentItem() - 1);
    }

    private void gotoNextPage() {

        pager.setCurrentItem(pager.getCurrentItem() + 1);
    }

    private void startGame() {

        Intent result = new Intent();
        result.putExtra(EXTRA_DIFFICULTY, difficulty);
        setResult(RESULT_OK, result);
        finish();
    }


}
