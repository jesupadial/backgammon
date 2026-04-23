package com.backgammon.game.api.dto;

import java.util.List;

public record BoardDto(List<PointDto> points, BarDto bar, BorneOffDto borneOff) {}
