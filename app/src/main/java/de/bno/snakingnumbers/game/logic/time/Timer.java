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

package de.bno.snakingnumbers.game.logic.time;

import android.os.SystemClock;
import android.util.Log;

/**
 * Created by marvin on 09.09.14.
 */
public class Timer {

    private long startTime;

    private int tickTime;

    private OnTickListener listener;

    private TimerThread thread;

    private long stopTime;

    public Timer(int tickTime){

        this.thread = null;
        this.tickTime = tickTime;
        this.stopTime = 0;
    }

    public Timer(){

        this(1000);
    }

    private synchronized void tick(){

        if(listener != null){

            listener.onTick(getElapsedTime());
        }
    }

    public void start(){

        Log.d(Timer.class.getName(), "startTimer");

        if(thread != null){
            return;
        }

        if(stopTime > -1){
            setElapsedTime(stopTime);
            stopTime = -1;
        }
        thread = new TimerThread();
    }

    public void stop(){

        Log.d(Timer.class.getName(), "stopTimer");

        if(thread == null){
            return;
        }

        stopTime = getElapsedTime();

        thread.finish();
        thread = null;
    }

    public void resetTime(){

        stopTime = -1;
        setElapsedTime(0);
    }

    public long getElapsedTime() {

        if(thread == null && stopTime > -1){

            return stopTime;
        }

        return SystemClock.elapsedRealtime() - getStartTime();
    }

    public void setElapsedTime(long elapsedTime){

        if (thread == null) {

            stopTime = elapsedTime;
        }

        startTime = SystemClock.elapsedRealtime() - elapsedTime;
    }

    public synchronized int getTickTime() {

        return tickTime;
    }

    public synchronized void setTickTime(int tickTime) {

        this.tickTime = tickTime;
    }

    public long getStartTime() {

        return startTime;
    }

    public synchronized OnTickListener getListener() {

        return listener;
    }

    public synchronized void setListener(OnTickListener listener) {

        this.listener = listener;
    }

    class TimerThread{

        private volatile boolean running;

        public TimerThread(){

            running = true;

            new Thread(new Runnable() {
                @Override
                public void run() {

                    while(running){

                        try {
                            Thread.sleep(getTickTime());
                        } catch (InterruptedException e) {
                        }

                        tick();
                    }
                }
            }).start();
        }

        public void finish(){
            running = false;
        }
    }
}
