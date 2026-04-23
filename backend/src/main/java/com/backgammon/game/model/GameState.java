package com.backgammon.game.model;

import lombok.Value;
import lombok.With;
import java.util.List;

@Value
@With
public class GameState {
    String gameId;
    Board board;
    Player currentPlayer;
    List<Integer> dice;
    GamePhase phase;
    Player winner;

    public static GameState newGame(String gameId) {
        return new GameState(gameId, Board.initial(), null, List.of(), GamePhase.ROLLING_FOR_FIRST_TURN, null);
    }

    public boolean hasWinner() {
        return winner != null;
    }
}
