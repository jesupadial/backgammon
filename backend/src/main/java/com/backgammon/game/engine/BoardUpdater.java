package com.backgammon.game.engine;

import com.backgammon.game.model.Board;
import com.backgammon.game.model.Move;
import com.backgammon.game.model.Player;
import com.backgammon.game.model.Point;
import org.springframework.stereotype.Component;

@Component
public class BoardUpdater {

    public Board applyMove(Board board, Player player, Move move) {
        if (move.isFromBar()) return applyBarEntry(board, player, move.to());
        if (move.isBearOff()) return applyBearOff(board, player, move.from());
        return applyRegularMove(board, player, move.from(), move.to());
    }

    private Board applyBarEntry(Board board, Player player, int to) {
        Board afterBarRemoval = board.withBar(board.getBar().removeCheckerFor(player));
        return placeCheckerAt(afterBarRemoval, player, to);
    }

    private Board applyRegularMove(Board board, Player player, int from, int to) {
        Board afterRemoval = board.withUpdatedPoint(board.point(from).removeChecker());
        return placeCheckerAt(afterRemoval, player, to);
    }

    private Board applyBearOff(Board board, Player player, int from) {
        Board afterRemoval = board.withUpdatedPoint(board.point(from).removeChecker());
        return afterRemoval.withBorneOff(afterRemoval.getBorneOff().addCheckerFor(player));
    }

    private Board placeCheckerAt(Board board, Player player, int pointIndex) {
        Point destination = board.point(pointIndex);
        Board boardAfterHit = (destination.isBlot() && !destination.isOwnedBy(player))
            ? hitBlot(board, destination.getOwner(), pointIndex)
            : board;
        return boardAfterHit.withUpdatedPoint(boardAfterHit.point(pointIndex).addChecker(player));
    }

    private Board hitBlot(Board board, Player blotOwner, int blotIndex) {
        Board afterRemoval = board.withUpdatedPoint(board.point(blotIndex).removeChecker());
        return afterRemoval.withBar(afterRemoval.getBar().addCheckerFor(blotOwner));
    }
}
