package com.backgammon.game.api.mapper;

import com.backgammon.game.api.dto.*;
import com.backgammon.game.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameMapper {

    public GameStateDto toDto(GameState state) {
        return new GameStateDto(
            state.getGameId(),
            toDto(state.getBoard()),
            state.getCurrentPlayer() != null ? state.getCurrentPlayer().name() : null,
            state.getDice(),
            state.getPhase().name(),
            state.getWinner() != null ? state.getWinner().name() : null
        );
    }

    public Move toDomain(MoveDto dto) {
        return new Move(dto.from(), dto.to());
    }

    private BoardDto toDto(Board board) {
        List<PointDto> points = board.getPoints().stream().map(this::toDto).toList();
        return new BoardDto(points, toDto(board.getBar()), toDto(board.getBorneOff()));
    }

    private PointDto toDto(Point point) {
        String color = point.getOwner() != null ? point.getOwner().name() : null;
        return new PointDto(point.getIndex(), color, point.getCheckerCount());
    }

    private BarDto toDto(Bar bar) {
        return new BarDto(bar.getWhiteCount(), bar.getBlackCount());
    }

    private BorneOffDto toDto(BorneOff borneOff) {
        return new BorneOffDto(borneOff.getWhiteCount(), borneOff.getBlackCount());
    }
}
