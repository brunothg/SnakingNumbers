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

package de.bno.snakingnumbers.game.logic;

import java.io.Serializable;

public class Result implements Serializable {

    private static final long serialVersionUID = 0L;

    private boolean outOfBounds;
    private boolean gameFinished;
    private boolean notAllowed;
    private boolean ok;

    private Result(boolean outOfBounds, boolean gameFinished,
                   boolean notAllowed, boolean ok) {

        this.outOfBounds = outOfBounds;
        this.gameFinished = gameFinished;
        this.notAllowed = notAllowed;
        this.ok = ok;
    }

    public Result() {

        this(false, false, false, false);
    }

    public boolean isOutOfBounds() {

        return outOfBounds;
    }

    public Result setOutOfBounds(boolean outOfBounds) {

        this.outOfBounds = outOfBounds;

        return this;
    }

    public boolean isGameFinished() {

        return gameFinished;
    }

    public Result setGameFinished(boolean gameFinished) {

        this.gameFinished = gameFinished;

        return this;
    }

    public boolean isNotAllowed() {

        return notAllowed;
    }

    public Result setNotAllowed(boolean notAllowed) {

        this.notAllowed = notAllowed;

        return this;
    }

    public Result setOK(boolean ok) {

        this.ok = ok;

        return this;
    }

    public boolean isOk() {
        return ok;
    }

}
