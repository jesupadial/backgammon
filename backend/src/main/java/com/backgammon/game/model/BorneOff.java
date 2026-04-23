package com.backgammon.game.model;

import lombok.Value;

@Value
public class BorneOff {
    int whiteCount;
    int blackCount;

    public static BorneOff empty() {
        return new BorneOff(0, 0);
    }

    public int countFor(Player player) {
        return player == Player.WHITE ? whiteCount : blackCount;
    }
}
