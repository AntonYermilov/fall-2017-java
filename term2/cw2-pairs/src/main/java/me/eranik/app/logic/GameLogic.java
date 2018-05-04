package me.eranik.app.logic;

import me.eranik.app.util.Pair;

import java.util.Random;

/**
 * Creates game field to play.
 */
public class GameLogic {

    private static Random random = new Random(13);

    private static int fieldSize;
    private static int[][] field;
    private static boolean[][] opened;

    private static Pair<Integer, Integer> positionFirst;
    private static Pair<Integer, Integer> positionSecond;

    /**
     * Initializes game field with random numbers.
     * @param fieldSize
     */
    public static void initialize(int fieldSize) {
        GameLogic.fieldSize = fieldSize;
        GameLogic.field = new int[fieldSize][fieldSize];
        GameLogic.opened = new boolean[fieldSize][fieldSize];

        int[] randomNumbers = new int[fieldSize * fieldSize];
        for (int i = 0; i < fieldSize * fieldSize; i += 2) {
            randomNumbers[i] = i / 2;
            randomNumbers[i + 1] = i / 2;
        }
        randomShuffle(randomNumbers);

        for (int i = 0; i < fieldSize; i++) {
            System.arraycopy(randomNumbers, i * fieldSize, GameLogic.field[i], 0, fieldSize);
        }

        GameLogic.positionFirst = null;
        GameLogic.positionSecond = null;
    }

    private static void randomShuffle(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int j = i + random.nextInt(array.length - i);
            swap(array, i, j);
        }
    }

    private static void swap(int[] array, int i, int j) {
        array[i] ^= array[j];
        array[j] ^= array[i];
        array[i] ^= array[j];
    }

    /**
     * Returns value in the specified position.
     * @param position position we want to get value from
     * @return value in the specified position
     */
    public static int getNumber(Pair<Integer, Integer> position) {
        return field[position.first][position.second];
    }

    /**
     * Returns number of already opened cells.
     * @return number of already opened cells
     */
    public static int countAlreadyOpened() {
        return (positionFirst == null ? 0 : 1) + (positionSecond == null ? 0 : 1);
    }

    /**
     * Checks if cell is already opened.
     * @param position position of cell we want to check
     * @return {@code true} if cell is opened; {@code false} otherwise
     */
    public static boolean alreadyOpened(Pair<Integer, Integer> position) {
        if (positionFirst != null && positionFirst.equals(position)) {
            return true;
        }
        if (positionSecond != null && positionSecond.equals(position)) {
            return true;
        }
        return false;
    }

    /**
     * Opens cell in the specified position and shows number in it.
     * @param position cell we want to open
     */
    public static void open(Pair<Integer, Integer> position) {
        if (opened[position.first][position.second]) {
            return;
        }
        if (positionFirst == null) {
            positionFirst = position;
            return;
        }
        if (!positionFirst.equals(position)) {
            positionSecond = position;
            return;
        }
    }

    /**
     * Closes opened cells.
     */
    public static void close() {
        positionFirst = null;
        positionSecond = null;
    }

    /**
     * Checks if opened cells are equal.
     * @return {@code true} if opened cells are equal; {@code false} otherwise
     */
    public static boolean equal() {
        if (positionFirst == null || positionSecond == null) {
            return false;
        }
        return field[positionFirst.first][positionFirst.second] == field[positionSecond.first][positionSecond.second];
    }

    /**
     * Sets opened cells opened forever.
     */
    public static void setOpened() {
        opened[positionFirst.first][positionFirst.second] = true;
        opened[positionSecond.first][positionSecond.second] = true;
    }

    /**
     * Returns first of the opened cells.
     * @return first of the opened cells
     */
    public static Pair<Integer, Integer> getPositionFirst() {
        return positionFirst;
    }

    /**
     * Returns second of the opened cells.
     * @return second of the opened cells
     */
    public static Pair<Integer, Integer> getPositionSecond() {
        return positionSecond;
    }
}
