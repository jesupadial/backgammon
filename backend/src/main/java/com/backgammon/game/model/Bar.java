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

    public Bar addCheckerFor(Player player) {
        return player == Player.WHITE ? new Bar(whiteCount + 1, blackCount) : new Bar(whiteCount, blackCount + 1);
    }

    public Bar removeCheckerFor(Player player) {
        return player == Player.WHITE ? new Bar(whiteCount - 1, blackCount) : new Bar(whiteCount, blackCount - 1);
    }
}
