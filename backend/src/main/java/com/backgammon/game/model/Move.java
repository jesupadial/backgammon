package com.backgammon.game.model;

public record Move(int from, int to) {
    public static final int BAR = 24;
    public static final int BEAR_OFF = 25;

    public boolean isFromBar() {
        return from == BAR;
    }

    public boolean isBearOff() {
        return to == BEAR_OFF;
    }
}
