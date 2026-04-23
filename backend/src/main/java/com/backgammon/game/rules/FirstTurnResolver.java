package com.backgammon.game.rules;

import com.backgammon.game.model.Player;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FirstTurnResolver {
    public Optional<Player> resolve(int whiteDie, int blackDie) {
        if (whiteDie > blackDie) return Optional.of(Player.WHITE);
        if (blackDie > whiteDie) return Optional.of(Player.BLACK);
        return Optional.empty();
    }
}
