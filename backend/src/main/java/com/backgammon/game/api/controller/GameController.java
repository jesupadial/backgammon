package com.backgammon.game.api.controller;

import com.backgammon.game.api.dto.FirstTurnRollDto;
import com.backgammon.game.api.dto.GameStateDto;
import com.backgammon.game.api.dto.MoveDto;
import com.backgammon.game.model.Player;
import com.backgammon.game.api.mapper.GameMapper;
import com.backgammon.game.session.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;
    private final GameMapper gameMapper;

    public GameController(GameService gameService, GameMapper gameMapper) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameStateDto createGame() {
        return gameMapper.toDto(gameService.createGame());
    }

    @GetMapping("/{gameId}")
    public GameStateDto getGame(@PathVariable String gameId) {
        return gameMapper.toDto(gameService.findGame(gameId));
    }

    @PostMapping("/{gameId}/first-roll")
    public GameStateDto rollForFirstTurn(@PathVariable String gameId, @RequestBody FirstTurnRollDto dto) {
        Player player = Player.valueOf(dto.player());
        return gameMapper.toDto(gameService.rollForFirstTurn(gameId, player));
    }

    @PostMapping("/{gameId}/roll")
    public GameStateDto rollDice(@PathVariable String gameId) {
        return gameMapper.toDto(gameService.rollDice(gameId));
    }

    @PostMapping("/{gameId}/moves")
    public GameStateDto makeMove(@PathVariable String gameId, @RequestBody MoveDto moveDto) {
        return gameMapper.toDto(gameService.applyMove(gameId, gameMapper.toDomain(moveDto)));
    }
}
