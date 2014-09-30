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
import java.util.ArrayList;
import java.util.List;

import de.bno.snakingnumbers.game.logic.path.Path;
import de.bno.snakingnumbers.game.logic.path.PathElement;

public class Zahlenschlange implements PlayerInterface, GameInterface, Serializable {

    private static final long serialVersionUID = 0L;

    Playground playground;
    Path path;

    public Zahlenschlange(int dimension, int maxNumber) {

        newGame(dimension, maxNumber);
    }

    public Zahlenschlange() {

        this(-1, -1);
    }

    @Override
    public boolean canUndo() {

        return path.size() > 1;
    }

    @Override
    public boolean undo() {

        return path.remove() != null;
    }

    @Override
    public void undoAll() {

        while (canUndo()) {

            undo();
        }
    }

    @Override
    public Result makeStepTo(int x, int y) {

        if (!(x >= 0 && y >= 0 && x < playground.getDimension() && y < playground
                .getDimension())) {

            return new Result().setOutOfBounds(true);
        }

        if (path.isComplete(playground.getDimension(),
                playground.getMaxNumber())) {

            return new Result().setGameFinished(true);
        }

        Position pathEnd = getPathEnd();
        if (!(pathEnd.getX() + 1 == playground.getDimension() && pathEnd.getY() + 1 == playground.getDimension()) && path.add(x, y, playground.getValue(x, y))) {

            return new Result().setOK(true).setGameFinished(
                    path.isComplete(playground.getDimension(),
                            playground.getMaxNumber()));
        } else {

            return new Result().setNotAllowed(true);
        }
    }

    @Override
    public Result canMakeStepTo(int x, int y) {

        if (!(x >= 0 && y >= 0 && x < playground.getDimension() && y < playground
                .getDimension())) {

            return new Result().setOutOfBounds(true);
        }

        if (path.isComplete(playground.getDimension(),
                playground.getMaxNumber())) {

            return new Result().setGameFinished(true);
        }

        if (path.canAdd(x, y, playground.getValue(x, y))) {

            return new Result().setOK(true);
        } else {

            return new Result().setNotAllowed(true);
        }
    }

    @Override
    public void newGame(int dimension, int maxNumber) {

        if (dimension < Playground.MINIMUM_DIMENSION) {

            playground = new Playground();
        } else if (maxNumber < 1) {

            playground = new Playground(dimension);
        } else {

            playground = new Playground(dimension, maxNumber);
        }

        path = new Path();
    }

    @Override
    public int getSize() {

        return playground.getDimension();
    }

    public int getMaxNumber() {

        return playground.getMaxNumber();
    }

    @Override
    public int getValue(int x, int y) {

        return playground.getValue(x, y);
    }

    @Override
    public boolean isPartOfPath(int x, int y) {

        for (PathElement e : path) {

            if (e.getX() == x && e.getY() == y) {

                return true;
            }
        }

        return false;
    }

    @Override
    public List<Position> getPath() {

        List<Position> path = new ArrayList<Position>(this.path.size());

        for (PathElement e : this.path) {

            path.add(new Position(e.getX(), e.getY()));
        }

        return path;
    }

    @Override
    public Position getPathEnd() {

        PathElement last = path.getLast();
        return new Position(last.getX(), last.getY());
    }

    @Override
    public List<Position> getSolution() {

        List<Position> path = new ArrayList<Position>(playground.getGoalPath().size());

        for (PathElement e : playground.getGoalPath()) {

            path.add(new Position(e.getX(), e.getY()));
        }

        return path;
    }
}
