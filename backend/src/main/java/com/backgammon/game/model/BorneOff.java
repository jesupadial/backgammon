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

    public BorneOff addCheckerFor(Player player) {
        return player == Player.WHITE ? new BorneOff(whiteCount + 1, blackCount) : new BorneOff(whiteCount, blackCount + 1);
    }
}
