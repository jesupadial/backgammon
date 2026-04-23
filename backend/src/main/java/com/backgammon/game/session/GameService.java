package com.backgammon.game.session;

import com.backgammon.game.engine.GameEngine;
import com.backgammon.game.model.GameState;
import com.backgammon.game.model.Move;
import com.backgammon.game.model.Player;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final GameEngine gameEngine;

    public GameService(GameRepository gameRepository, GameEngine gameEngine) {
        this.gameRepository = gameRepository;
        this.gameEngine = gameEngine;
    }

    public GameState createGame() {
        GameState newGame = GameState.newGame(UUID.randomUUID().toString());
        gameRepository.save(newGame);
        return newGame;
    }

    public GameState findGame(String gameId) {
        return gameRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));
    }

    public GameState rollForFirstTurn(String gameId, Player player, int die) {
        GameState updated = gameEngine.rollForFirstTurn(findGame(gameId), player, die);
        gameRepository.save(updated);
        return updated;
    }

    public GameState rollDice(String gameId) {
        GameState updated = gameEngine.rollDice(findGame(gameId));
        gameRepository.save(updated);
        return updated;
    }

    public GameState applyMove(String gameId, Move move) {
        GameState updated = gameEngine.applyMove(findGame(gameId), move);
        gameRepository.save(updated);
        return updated;
    }
}
