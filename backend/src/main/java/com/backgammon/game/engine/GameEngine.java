package com.backgammon.game.engine;

import com.backgammon.game.model.Board;
import com.backgammon.game.model.GamePhase;
import com.backgammon.game.model.GameState;
import com.backgammon.game.model.Move;
import com.backgammon.game.model.Player;
import com.backgammon.game.rules.DiceRoller;
import com.backgammon.game.rules.FirstTurnResolver;
import com.backgammon.game.rules.MoveValidator;
import com.backgammon.game.rules.WinConditionChecker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameEngine {
    private final DiceRoller diceRoller;
    private final FirstTurnResolver firstTurnResolver;
    private final MoveValidator moveValidator;
    private final WinConditionChecker winConditionChecker;
    private final BoardUpdater boardUpdater;

    public GameEngine(DiceRoller diceRoller, FirstTurnResolver firstTurnResolver,
                      MoveValidator moveValidator, WinConditionChecker winConditionChecker,
                      BoardUpdater boardUpdater) {
        this.diceRoller = diceRoller;
        this.firstTurnResolver = firstTurnResolver;
        this.moveValidator = moveValidator;
        this.winConditionChecker = winConditionChecker;
        this.boardUpdater = boardUpdater;
    }

    public GameState rollForFirstTurn(GameState state, Player player, int die) {
        List<Integer> updatedDice = appendedDice(state.getDice(), die);
        if (updatedDice.size() < 2) return state.withDice(updatedDice);
        return resolveFirstTurn(state, player, updatedDice);
    }

    public GameState rollDice(GameState state) {
        return state.withDice(diceRoller.rollTwo()).withPhase(GamePhase.WAITING_FOR_MOVE);
    }

    public GameState applyMove(GameState state, Move move) {
        if (!moveValidator.isLegalMove(state, move)) {
            throw new IllegalArgumentException("Illegal move: " + move);
        }
        Board updatedBoard = boardUpdater.applyMove(state.getBoard(), state.getCurrentPlayer(), move);
        List<Integer> remainingDice = removeDie(state.getDice(), moveValidator.consumedDie(state, move));
        return nextState(state, updatedBoard, remainingDice);
    }

    private GameState resolveFirstTurn(GameState state, Player secondPlayer, List<Integer> rolledDice) {
        int whiteDie = secondPlayer == Player.WHITE ? rolledDice.get(1) : rolledDice.get(0);
        int blackDie = secondPlayer == Player.BLACK ? rolledDice.get(1) : rolledDice.get(0);
        return firstTurnResolver.resolve(whiteDie, blackDie)
            .map(winner -> state.withCurrentPlayer(winner)
                .withDice(List.of(whiteDie, blackDie))
                .withPhase(GamePhase.WAITING_FOR_MOVE))
            .orElseGet(() -> state.withDice(List.of()));
    }

    private GameState nextState(GameState state, Board updatedBoard, List<Integer> remainingDice) {
        Player player = state.getCurrentPlayer();
        if (winConditionChecker.hasWon(updatedBoard.getBorneOff(), player)) {
            return state.withBoard(updatedBoard).withDice(List.of()).withPhase(GamePhase.GAME_OVER).withWinner(player);
        }
        if (remainingDice.isEmpty()) {
            return state.withBoard(updatedBoard).withDice(List.of())
                .withCurrentPlayer(player.opponent())
                .withPhase(GamePhase.WAITING_FOR_ROLL);
        }
        return state.withBoard(updatedBoard).withDice(remainingDice);
    }

    private List<Integer> appendedDice(List<Integer> dice, int die) {
        List<Integer> mutable = new ArrayList<>(dice);
        mutable.add(die);
        return List.copyOf(mutable);
    }

    private List<Integer> removeDie(List<Integer> dice, int dieValue) {
        List<Integer> mutable = new ArrayList<>(dice);
        mutable.remove(Integer.valueOf(dieValue));
        return List.copyOf(mutable);
    }
}
