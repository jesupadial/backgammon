import { Component, computed, inject, signal } from '@angular/core';
import { BAR_INDEX, BEAR_OFF_INDEX, GameStateModel, PlayerColor } from '../../core/models/game.models';
import { GameService } from '../../core/services/game.service';
import { BoardComponent } from '../board/board';
import { DiceComponent } from '../dice/dice';

@Component({
  selector: 'app-game-page',
  imports: [BoardComponent, DiceComponent],
  templateUrl: './game-page.html',
  styleUrl: './game-page.scss'
})
export class GamePageComponent {
  private readonly gameService = inject(GameService);

  protected readonly gameState = signal<GameStateModel | null>(null);
  protected readonly selectedFrom = signal<number | null>(null);

  protected readonly validDestinations = computed(() => {
    const from = this.selectedFrom();
    const state = this.gameState();
    if (from === null || !state) return [];
    return state.validMoves
      .filter(move => move.from === from)
      .map(move => move.to);
  });

  protected createGame(): void {
    this.gameService.createGame().subscribe(state => this.gameState.set(state));
  }

  protected rollForFirstTurn(player: PlayerColor): void {
    const gameId = this.gameState()?.gameId;
    if (!gameId) return;
    this.gameService.rollForFirstTurn(gameId, player).subscribe(state => this.gameState.set(state));
  }

  protected rollDice(): void {
    const gameId = this.gameState()?.gameId;
    if (!gameId) return;
    this.gameService.rollDice(gameId).subscribe(state => this.gameState.set(state));
  }

  protected onPointClick(index: number): void {
    if (index === BEAR_OFF_INDEX) {
      this.submitBearOff();
      return;
    }
    const currentFrom = this.selectedFrom();
    if (currentFrom === null || index === BAR_INDEX) {
      this.selectedFrom.set(index);
      return;
    }
    if (currentFrom === index) {
      this.selectedFrom.set(null);
      return;
    }
    this.submitMove(currentFrom, index);
  }

  private submitBearOff(): void {
    const from = this.selectedFrom();
    if (from !== null) this.submitMove(from, BEAR_OFF_INDEX);
  }

  private submitMove(from: number, to: number): void {
    const gameId = this.gameState()?.gameId;
    if (!gameId) return;
    this.gameService.makeMove(gameId, { from, to }).subscribe({
      next: state => { this.gameState.set(state); this.selectedFrom.set(null); },
      error: () => this.selectedFrom.set(null)
    });
  }
}
