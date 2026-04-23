package com.backgammon.game.model;

import lombok.Value;
import java.util.ArrayList;
import java.util.List;

@Value
public class Board {
    private static final int POINT_COUNT = 24;

    private static final int INITIAL_ANCHOR_COUNT  = 2;
    private static final int INITIAL_BUILDER_COUNT = 3;
    private static final int INITIAL_STACK_COUNT   = 5;

    private static final int WHITE_ANCHOR   = 23;
    private static final int WHITE_MIDPOINT = 12;
    private static final int WHITE_BUILDERS =  7;
    private static final int WHITE_HOME     =  5;
    private static final int BLACK_HOME     = 18;
    private static final int BLACK_BUILDERS = 16;
    private static final int BLACK_MIDPOINT = 11;
    private static final int BLACK_ANCHOR   =  0;

    List<Point> points;
    Bar bar;
    BorneOff borneOff;

    public static Board initial() {
        return new Board(initialPoints(), Bar.empty(), BorneOff.empty());
    }

    public Point point(int index) {
        return points.get(index);
    }

    private static List<Point> initialPoints() {
        List<Point> points = new ArrayList<>(POINT_COUNT);
        for (int index = 0; index < POINT_COUNT; index++) {
            points.add(Point.empty(index));
        }
        points.set(WHITE_ANCHOR,   Point.of(WHITE_ANCHOR,   Player.WHITE, INITIAL_ANCHOR_COUNT));
        points.set(WHITE_MIDPOINT, Point.of(WHITE_MIDPOINT, Player.WHITE, INITIAL_STACK_COUNT));
        points.set(WHITE_BUILDERS, Point.of(WHITE_BUILDERS, Player.WHITE, INITIAL_BUILDER_COUNT));
        points.set(WHITE_HOME,     Point.of(WHITE_HOME,     Player.WHITE, INITIAL_STACK_COUNT));
        points.set(BLACK_ANCHOR,   Point.of(BLACK_ANCHOR,   Player.BLACK, INITIAL_ANCHOR_COUNT));
        points.set(BLACK_MIDPOINT, Point.of(BLACK_MIDPOINT, Player.BLACK, INITIAL_STACK_COUNT));
        points.set(BLACK_BUILDERS, Point.of(BLACK_BUILDERS, Player.BLACK, INITIAL_BUILDER_COUNT));
        points.set(BLACK_HOME,     Point.of(BLACK_HOME,     Player.BLACK, INITIAL_STACK_COUNT));
        return List.copyOf(points);
    }
}
