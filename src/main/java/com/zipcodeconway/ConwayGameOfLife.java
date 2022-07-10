package com.zipcodeconway;

import java.util.Arrays;
import java.util.Random;

public class ConwayGameOfLife {
    int[][] currentGeneration;
    int[][] nextGeneration;
    SimpleWindow displayWindow;
    Random r = new Random();
    public ConwayGameOfLife(Integer dimension) {
        this.displayWindow = new SimpleWindow(dimension);
        currentGeneration = new int[dimension][dimension];
        nextGeneration = new int[dimension][dimension];
    }

    public ConwayGameOfLife(Integer dimension, int[][] startMatrix) {
        this.displayWindow = new SimpleWindow(dimension);
        currentGeneration = startMatrix.clone();
        nextGeneration = new int[dimension][dimension];
    }

    public static void main(String[] args) {
        ConwayGameOfLife sim = new ConwayGameOfLife(50);
        sim.createRandomStart(50);
        sim.simulate(50);
        int[][] endingWorld = sim.currentGeneration;
    }

    // Contains the logic for the starting scenario.
    // Which cells are alive or dead in generation 0.
    // allocates and returns the starting matrix of size 'dimension'
    private void createRandomStart(Integer dimension) {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                currentGeneration[i][j] = r.nextInt(2);
            }
        }
    }

    public int[][] simulate(Integer maxGenerations) {
        int generationCounter = 1;
        int matrixLength = currentGeneration.length;
        displayWindow.display(currentGeneration, 1);
        while (generationCounter < maxGenerations) {
            displayWindow.sleep(150);
            for (int i = 0; i < matrixLength; i++) {
                for (int j = 0; j < matrixLength; j++) {
                    nextGeneration[i][j] = isAlive(i, j, currentGeneration);
                }
            }
            copyAndZeroOut();
            generationCounter++;
            displayWindow.display(currentGeneration, generationCounter);
        }
        return currentGeneration;
    }

    // copy the values of 'next' matrix to 'current' matrix,
    // and then zero out the contents of 'next' matrix
    public void copyAndZeroOut() {
        currentGeneration = nextGeneration.clone();
        nextGeneration = new int[nextGeneration.length][nextGeneration.length];
    }

    // Calculate if an individual cell should be alive in the next generation.
    // Based on the game logic:
	/*
		Any live cell with fewer than two live neighbours dies, as if by needs caused by underpopulation.
		Any live cell with more than three live neighbours dies, as if by overcrowding.
		Any live cell with two or three live neighbours lives, unchanged, to the next generation.
		Any dead cell with exactly three live neighbours cells will come to life.
	*/
    private int isAlive(int row, int col, int[][] world) {
        int neighborsAlive = neighborsAlive(row, col, world);
        if (world[row][col] == 1) { // currently alive
            if (neighborsAlive >= 2 && neighborsAlive <= 3) return 1;
        } else { // currently dead
            if (neighborsAlive == 3) return 1;
        }
        return 0;
    }

    private int neighborsAlive(int row, int col, int[][] world) {
        int neighborsAlive = 0;
        int worldLength = world.length;
        if (row == 0) { // top side
            if (col == 0) { // top left corner
                neighborsAlive = world[worldLength - 1][worldLength - 1] + world[worldLength - 1][col] + world[worldLength - 1][col + 1]
                        + world[row][worldLength - 1] + world[row][col + 1] + world[row + 1][worldLength - 1]
                        + world[row + 1][col] + world[row + 1][col + 1];
            } else if (col == worldLength - 1) { // top right corner
                neighborsAlive = world[worldLength - 1][col - 1] + world[worldLength - 1][col] + world[worldLength - 1][0]
                        + world[row][col - 1] + world[row][0] + world[row + 1][col - 1]
                        + world[row + 1][col] + world[row + 1][0];
            } else { // top not corner
                neighborsAlive = world[worldLength - 1][col - 1] + world[worldLength - 1][col] + world[worldLength - 1][col + 1]
                        + world[row][col - 1] + world[row][col + 1] + world[row + 1][col - 1]
                        + world[row + 1][col] + world[row + 1][col + 1];
            }
        } else if (row == worldLength - 1) { // btm side
            if (col == 0) { // btm left corner
                neighborsAlive = world[row - 1][worldLength - 1] + world[row - 1][col] + world[row - 1][col + 1]
                        + world[row][worldLength - 1] + world[row][col + 1] + world[0][worldLength - 1]
                        + world[0][col] + world[0][col + 1];
            } else if (col == worldLength - 1) { // btm right corner
                neighborsAlive = world[row - 1][col - 1] + world[row - 1][col] + world[row - 1][0]
                        + world[row][col - 1] + world[row][0] + world[0][col - 1]
                        + world[0][col] + world[0][0];
            } else { // btm not corner
                neighborsAlive = world[row - 1][col - 1] + world[row - 1][col] + world[row - 1][col + 1]
                        + world[row][col - 1] + world[row][col + 1] + world[0][col - 1]
                        + world[0][col] + world[0][col + 1];
            }
        } else if (col == 0) { // left but not corner
            neighborsAlive = world[row - 1][worldLength - 1] + world[row - 1][col] + world[row - 1][col + 1]
                    + world[row][worldLength - 1] + world[row][col + 1] + world[row + 1][worldLength - 1]
                    + world[row + 1][col] + world[row + 1][col + 1];
        } else if (col == worldLength - 1) { // right but not corner
            neighborsAlive = world[row - 1][col - 1] + world[row - 1][col] + world[row - 1][0]
                    + world[row][col - 1] + world[row][0] + world[row + 1][col - 1]
                    + world[row + 1][col] + world[row + 1][0];
        } else {
            neighborsAlive = world[row - 1][col - 1] + world[row - 1][col] + world[row - 1][col + 1]
                    + world[row][col - 1] + world[row][col + 1] + world[row + 1][col - 1]
                    + world[row + 1][col] + world[row + 1][col + 1];
        }
        return neighborsAlive;
    }
}
