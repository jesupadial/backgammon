package com.backgammon.game.model;

public enum Player {
    WHITE, BLACK;

    public Player opponent() {
        return this == WHITE ? BLACK : WHITE;
    }
}
