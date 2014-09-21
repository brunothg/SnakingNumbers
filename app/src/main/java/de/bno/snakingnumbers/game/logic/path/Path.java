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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Path implements Iterable<PathElement>, Serializable {

    private static final long serialVersionUID = 0L;

    List<PathElement> path;

    public Path() {

        this.path = new LinkedList<PathElement>();
        path.add(new PathElement(0, 0, 1));
    }

    public boolean add(int x, int y, int val) {

        if (!canAdd(x, y, val)) {

            return false;
        }

        path.add(new PathElement(x, y, val));
        return true;
    }

    public boolean canAdd(int x, int y, int val) {

        if (x < 0 || y < 0 || val < 1) {
            return false;
        }

        PathElement last = getLast();
        if (Math.abs(last.getX() - x) + Math.abs(last.getY() - y) != 1) {
            return false;
        }

        for (PathElement e : path) {

            if (e.getX() == x && e.getY() == y) {
                return false;
            }

            if (e.getVal() == val) {
                return false;
            }
        }

        return true;
    }

    public boolean isComplete(int dimension, int maxNumber) {

        boolean[] numbers = new boolean[maxNumber];

        PathElement last = getLast();
        if (last.getVal() != maxNumber || last.getX() != dimension - 1
                || last.getY() != dimension - 1) {
            return false;
        }

        for (PathElement e : path) {

            if (e.getVal() < 1 || e.getVal() > maxNumber) {
                return false;
            }
            numbers[e.getVal() - 1] = true;
        }

        for (int i = 0; i < numbers.length; i++) {
            if (!numbers[i]) {
                return false;
            }
        }
        return true;
    }

    public int size() {

        return path.size();
    }

    public PathElement remove() {

        if (path.size() == 1) {

            return null;
        }

        return path.remove(path.size() - 1);
    }

    public PathElement getLast() {

        return path.get(path.size() - 1);
    }

    public List<Integer> getValues() {

        List<Integer> vals = new ArrayList<Integer>(path.size());

        for (PathElement e : path) {

            vals.add(e.getVal());
        }

        return vals;
    }

    @Override
    public Iterator<PathElement> iterator() {

        return path.iterator();
    }

    public String toString() {

        String ret = "";

        for (PathElement e : path) {

            ret += String.format("%d:%d-%d, ", e.getX(), e.getY(), e.getVal());
        }

        return ret;
    }
}
