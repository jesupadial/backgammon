package com.backgammon.game.session;

import com.backgammon.game.model.GameState;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GameRepository {
    private final Map<String, GameState> games = new ConcurrentHashMap<>();

    public void save(GameState state) {
        games.put(state.getGameId(), state);
    }

    public Optional<GameState> findById(String gameId) {
        return Optional.ofNullable(games.get(gameId));
    }
}
