package com.backgammon.game.rules;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DiceRoller {
    private static final int DIE_FACES = 6;

    public int rollOne() {
        return ThreadLocalRandom.current().nextInt(1, DIE_FACES + 1);
    }

    public List<Integer> rollTwo() {
        int first = rollOne();
        int second = rollOne();
        return (first == second) ? List.of(first, first, first, first) : List.of(first, second);
    }
}
