package com.backgammon.game.api.dto;

import java.util.List;

public record GameStateDto(
    String gameId,
    BoardDto board,
    String currentPlayer,
    List<Integer> dice,
    String phase,
    String winner,
    List<MoveDto> validMoves
) {}
