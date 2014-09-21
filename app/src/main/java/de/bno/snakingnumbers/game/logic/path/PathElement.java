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

package de.bno.snakingnumbers.game.logic.path;

import java.io.Serializable;

public class PathElement implements Serializable {

    private static final long serialVersionUID = 0L;

    private int x;
    private int y;
    private int val;

    public PathElement(int x, int y, int val) {

        this.x = x;
        this.y = y;
        this.val = val;
    }

    public int hashCode() {

        return x + y + val;
    }

    public boolean equals(Object o) {

        if (o instanceof PathElement) {
            PathElement cmp = (PathElement) o;

            if (x == cmp.x && y == cmp.y && val == cmp.val) {

                return true;
            }
        }

        return false;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public int getVal() {

        return val;
    }
}
