package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static model.GameStatusEnum.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private static final int BOARD_SIZE = 9;
    private final Space[][] spaces;

    public Board(String[] positions) {
        this.spaces = new Space[BOARD_SIZE][BOARD_SIZE];
        initializeBoard(positions);
    }

    private void initializeBoard(String[] positions) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                spaces[row][col] = new Space(0, false);
            }
        }

        for (String pos : positions) {
            try {
                String[] parts = pos.split(";");
                String[] coords = parts[0].split(",");
                String[] values = parts[1].split(",");

                int row = Integer.parseInt(coords[0]);
                int col = Integer.parseInt(coords[1]);
                int expected = Integer.parseInt(values[0]);
                boolean fixed = Boolean.parseBoolean(values[1]);

                if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
                    spaces[row][col] = new Space(expected, fixed);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Formato de posição inválido: " + pos);
            }
        }
    }

    public GameStatusEnum getStatus() {
        boolean hasPlayerMoves = false;
        boolean isComplete = true;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Space space = spaces[i][j];
                if (isNull(space.getActual())) {
                    isComplete = false;
                }
                if (!space.isFixed() && nonNull(space.getActual())) {
                    hasPlayerMoves = true;
                }
            }
        }

        if (!hasPlayerMoves) return NON_STARTED;
        return isComplete ? COMPLETE : INCOMPLETE;
    }

    public boolean hasErrors() {
        if (getStatus() == NON_STARTED) {
            return false;
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Space space = spaces[i][j];
                if (nonNull(space.getActual()) && !space.getActual().equals(space.getExpected())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean changeValue(int row, int col, int value) {
        Space space = spaces[row][col];
        if (space.isFixed()) {
            return false;
        }
        space.setActual(value);
        return true;
    }

    public boolean clearValue(int row, int col) {
        Space space = spaces[row][col];
        if (space.isFixed()) {
            return false;
        }
        space.clearSpace();
        return true;
    }

    public void reset() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (!spaces[i][j].isFixed()) {
                    spaces[i][j].clearSpace();
                }
            }
        }
    }

    public boolean gameIsFinished() {
        return !hasErrors() && getStatus().equals(COMPLETE);
    }

    public String getFormattedBoard() {
        StringBuilder sb = new StringBuilder();
        String hLine = " *-------*-------*-------*";

        for (int row = 0; row < BOARD_SIZE; row++) {
            if (row % 3 == 0) {
                sb.append(hLine).append("\n");
            }
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (col % 3 == 0) {
                    sb.append("| ");
                }
                Integer actual = spaces[row][col].getActual();
                sb.append(isNull(actual) ? " " : actual).append(" ");
            }
            sb.append("|\n");
        }
        sb.append(hLine).append("\n");
        return sb.toString();
    }
}