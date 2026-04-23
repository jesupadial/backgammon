package com.backgammon.game.rules;

import com.backgammon.game.model.Board;
import com.backgammon.game.model.GameState;
import com.backgammon.game.model.Move;
import com.backgammon.game.model.Player;
import org.springframework.stereotype.Component;

@Component
public class MoveValidator {
    private final BearOffValidator bearOffValidator;

    public MoveValidator(BearOffValidator bearOffValidator) {
        this.bearOffValidator = bearOffValidator;
    }

    public boolean isLegalMove(GameState state, Move move) {
        if (mustEnterFromBar(state) && !move.isFromBar()) return false;
        if (move.isFromBar()) return isLegalBarEntry(state, move.to());
        if (move.isBearOff()) return isLegalBearOff(state, move.from());
        return isLegalRegularMove(state, move.from(), move.to());
    }

    private boolean mustEnterFromBar(GameState state) {
        return state.getBoard().getBar().hasCheckersFor(state.getCurrentPlayer());
    }

    private boolean isLegalBarEntry(GameState state, int destinationPoint) {
        Player player = state.getCurrentPlayer();
        return state.getDice().stream()
            .anyMatch(die -> barEntryPoint(player, die) == destinationPoint
                && state.getBoard().point(destinationPoint).isOpenFor(player));
    }

    private int barEntryPoint(Player player, int dieValue) {
        return player == Player.WHITE ? Move.BAR - dieValue : dieValue - 1;
    }

    private boolean isLegalBearOff(GameState state, int fromPoint) {
        Board board = state.getBoard();
        Player player = state.getCurrentPlayer();
        if (!bearOffValidator.canBearOff(board, player)) return false;
        return state.getDice().stream()
            .anyMatch(die -> bearOffValidator.isValidBearOff(board, player, fromPoint, die));
    }

    private boolean isLegalRegularMove(GameState state, int from, int to) {
        Player player = state.getCurrentPlayer();
        Board board = state.getBoard();
        if (!board.point(from).isOwnedBy(player)) return false;
        if (!board.point(to).isOpenFor(player)) return false;
        int distance = moveDistance(player, from, to);
        return distance > 0 && state.getDice().contains(distance);
    }

    private int moveDistance(Player player, int from, int to) {
        return player == Player.WHITE ? from - to : to - from;
    }
}
