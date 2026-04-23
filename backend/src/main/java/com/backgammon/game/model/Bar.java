package com.backgammon.game.model;

import lombok.Value;

@Value
public class Bar {
    int whiteCount;
    int blackCount;

    public static Bar empty() {
        return new Bar(0, 0);
    }

    public int countFor(Player player) {
        return player == Player.WHITE ? whiteCount : blackCount;
    }

    public boolean hasCheckersFor(Player player) {
        return countFor(player) > 0;
    }
}
