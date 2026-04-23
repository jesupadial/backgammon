package com.backgammon.game.rules;

import com.backgammon.game.model.BorneOff;
import com.backgammon.game.model.Player;
import org.springframework.stereotype.Component;

@Component
public class WinConditionChecker {
    private static final int CHECKERS_TO_WIN = 15;

    public boolean hasWon(BorneOff borneOff, Player player) {
        return borneOff.countFor(player) == CHECKERS_TO_WIN;
    }
}
