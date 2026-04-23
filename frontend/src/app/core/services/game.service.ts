import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GameStateModel, MoveModel, PlayerColor } from '../models/game.models';

@Injectable({ providedIn: 'root' })
export class GameService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/games';

  createGame(): Observable<GameStateModel> {
    return this.http.post<GameStateModel>(this.baseUrl, {});
  }

  getGame(gameId: string): Observable<GameStateModel> {
    return this.http.get<GameStateModel>(`${this.baseUrl}/${gameId}`);
  }

  rollForFirstTurn(gameId: string, player: PlayerColor): Observable<GameStateModel> {
    return this.http.post<GameStateModel>(`${this.baseUrl}/${gameId}/first-roll`, { player });
  }

  rollDice(gameId: string): Observable<GameStateModel> {
    return this.http.post<GameStateModel>(`${this.baseUrl}/${gameId}/roll`, {});
  }

  makeMove(gameId: string, move: MoveModel): Observable<GameStateModel> {
    return this.http.post<GameStateModel>(`${this.baseUrl}/${gameId}/moves`, move);
  }
}
