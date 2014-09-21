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
import java.util.List;
import java.util.Random;

import de.bno.snakingnumbers.game.logic.path.Path;
import de.bno.snakingnumbers.game.logic.path.PathElement;

public class Playground implements Serializable {

    private static final long serialVersionUID = 0L;

    public static final int MINIMUM_DIMENSION = 3;

    private int dimension;
    private int maxNumber;
    private int[][] field;
    private Path goalPath;

    public Playground(int dimension, int maxNumber) {

        if (dimension < MINIMUM_DIMENSION || maxNumber < dimension * 2 - 1
                || maxNumber > dimension * dimension) {
            throw new IllegalArgumentException(
                    "dimension must be greater or equal "
                            + MINIMUM_DIMENSION
                            + ". maxNumber must be at least dimension * 2 - 1 and lower dimension * dimension.");
        }

        this.dimension = dimension;
        this.maxNumber = maxNumber;
        this.field = new int[dimension][dimension];

        generateNewField();
    }

    public Playground(int dimension) {

        this(dimension, ((dimension * dimension) / 2) + 1);
    }

    public Playground() {

        this(8);
    }

    // Generate Field
    public void generateNewField(Random rand) {

        clearField();

        field[0][0] = 1;
        goalPath = generateGoalPath(rand, new Path());

        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {

                if (field[y][x] <= 0) {
                    field[y][x] = getRandomNumber(rand);
                }
            }
        }

    }

    private int getRandomNumber(Random rand) {
        return rand.nextInt(maxNumber - 1) + 1;
    }

    private Path generateGoalPath(Random rand, Path p) {

        PathElement last = p.getLast();
        List<Integer> values = p.getValues();

        int val = maxNumber;
        if (values.size() < maxNumber - 1) {

            do {
                val = getRandomNumber(rand);
            } while (values.contains(val));
        } else {

            boolean ready = p.add(dimension - 1, dimension - 1, val);

            if (ready) {
                for (PathElement e : p) {
                    field[e.getY()][e.getX()] = e.getVal();
                }
            }

            return (ready) ? p : null;
        }

        boolean xAxis = rand.nextBoolean();
        boolean positive = rand.nextBoolean();

        int x;
        int y;

        for (int i = 0; i < 4; i++) {

            x = last.getX();
            y = last.getY();

            switch (i) {
                case 1:
                    positive = !positive;
                    break;
                case 2:
                    xAxis = !xAxis;
                    break;
                case 3:
                    positive = !positive;
                    break;
            }

            x += (xAxis) ? ((positive) ? +1 : -1) : 0;
            y += (!xAxis) ? ((positive) ? +1 : -1) : 0;

            if (x >= 0 && y >= 0 && x < dimension && y < dimension) {

                if (p.add(x, y, val)) {
                    Path tmpGP = generateGoalPath(rand, p);

                    boolean succ = tmpGP != null;

                    if (!succ) {

                        p.remove();
                    } else {

                        return tmpGP;
                    }
                }
            }
        }

        return null;
    }

    private void clearField() {

        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {

                field[y][x] = -1;
            }
        }
    }

    public void generateNewField() {

        generateNewField(new Random());
    }

    // Generate Field

    public int getValue(int x, int y) {

        return field[y][x];
    }

    public int getMaxNumber() {

        return maxNumber;
    }

    public int getDimension() {

        return dimension;
    }

    public Path getGoalPath() {

        return goalPath;
    }

    public boolean isGoalPath(Path p) {

        return p.isComplete(dimension, maxNumber);
    }

    @Override
    public String toString() {

        String ret = "";

        int len = String.valueOf(maxNumber).length();

        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {

                ret += String.format("%" + len + "d", field[y][x])
                        + ((x >= field[y].length) ? "" : " ");
            }
            ret += "\n";
        }

        return ret;
    }
}
