package com.backgammon.game.model;

import lombok.Value;

@Value
public class Point {
    int index;
    Player owner;
    int checkerCount;

    public static Point empty(int index) {
        return new Point(index, null, 0);
    }

    public static Point of(int index, Player owner, int count) {
        return new Point(index, owner, count);
    }

    public boolean isEmpty() {
        return checkerCount == 0;
    }

    public boolean isBlot() {
        return checkerCount == 1;
    }

    public boolean isOwnedBy(Player player) {
        return owner == player && checkerCount > 0;
    }

    public boolean isOpenFor(Player player) {
        if (isEmpty()) return true;
        if (isOwnedBy(player)) return true;
        return isBlot();
    }

    public Point addChecker(Player player) {
        return new Point(index, player, checkerCount + 1);
    }

    public Point removeChecker() {
        int newCount = checkerCount - 1;
        return new Point(index, newCount == 0 ? null : owner, newCount);
    }
}
