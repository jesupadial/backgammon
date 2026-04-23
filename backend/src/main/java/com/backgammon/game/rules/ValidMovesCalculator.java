package com.backgammon.game.rules;

import com.backgammon.game.model.GamePhase;
import com.backgammon.game.model.GameState;
import com.backgammon.game.model.Move;
import com.backgammon.game.model.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class ValidMovesCalculator {

    private static final List<Integer> ALL_BOARD_POINTS = IntStream.range(0, 24).boxed().toList();
    private static final List<Integer> ALL_DESTINATIONS = Stream.concat(
        IntStream.range(0, 24).boxed(),
        Stream.of(Move.BEAR_OFF)
    ).toList();

    private final MoveValidator moveValidator;

    public ValidMovesCalculator(MoveValidator moveValidator) {
        this.moveValidator = moveValidator;
    }

    public List<Move> calculate(GameState state) {
        if (state.getPhase() != GamePhase.WAITING_FOR_MOVE || state.getDice().isEmpty()) {
            return List.of();
        }
        return sourcesFor(state).stream()
            .flatMap(from -> ALL_DESTINATIONS.stream()
                .map(to -> new Move(from, to))
                .filter(move -> moveValidator.isLegalMove(state, move)))
            .distinct()
            .toList();
    }

    private List<Integer> sourcesFor(GameState state) {
        Player player = state.getCurrentPlayer();
        if (state.getBoard().getBar().hasCheckersFor(player)) return List.of(Move.BAR);
        return ALL_BOARD_POINTS.stream()
            .filter(index -> state.getBoard().point(index).isOwnedBy(player))
            .toList();
    }
}
