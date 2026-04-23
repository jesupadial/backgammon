package com.backgammon.game.rules;

import com.backgammon.game.model.Board;
import com.backgammon.game.model.Player;
import org.springframework.stereotype.Component;

@Component
public class BearOffValidator {
    private static final int WHITE_HOME_START = 0;
    private static final int WHITE_HOME_END = 5;
    private static final int BLACK_HOME_START = 18;
    private static final int BLACK_HOME_END = 23;
    private static final int BOARD_SIZE = 24;

    public boolean canBearOff(Board board, Player player) {
        return !board.getBar().hasCheckersFor(player) && allCheckersInHomeBoard(board, player);
    }

    public boolean isValidBearOff(Board board, Player player, int fromPoint, int dieValue) {
        int distance = distanceToBearOff(fromPoint, player);
        if (dieValue == distance) return true;
        if (dieValue > distance) return isHighestOccupiedPoint(board, player, fromPoint);
        return false;
    }

    private boolean allCheckersInHomeBoard(Board board, Player player) {
        return board.getPoints().stream()
            .filter(point -> !isInHomeBoard(point.getIndex(), player))
            .noneMatch(point -> point.isOwnedBy(player));
    }

    private boolean isInHomeBoard(int index, Player player) {
        if (player == Player.WHITE) return index >= WHITE_HOME_START && index <= WHITE_HOME_END;
        return index >= BLACK_HOME_START && index <= BLACK_HOME_END;
    }

    private int distanceToBearOff(int fromPoint, Player player) {
        return player == Player.WHITE ? fromPoint + 1 : BOARD_SIZE - fromPoint;
    }

    private boolean isHighestOccupiedPoint(Board board, Player player, int fromPoint) {
        return board.getPoints().stream()
            .filter(point -> isInHomeBoard(point.getIndex(), player))
            .filter(point -> point.isOwnedBy(player))
            .noneMatch(point -> isFurtherFromBearOff(point.getIndex(), fromPoint, player));
    }

    private boolean isFurtherFromBearOff(int index, int referenceIndex, Player player) {
        return player == Player.WHITE ? index > referenceIndex : index < referenceIndex;
    }
}
