export type GamePhase = 'ROLLING_FOR_FIRST_TURN' | 'WAITING_FOR_ROLL' | 'WAITING_FOR_MOVE' | 'GAME_OVER';
export type PlayerColor = 'WHITE' | 'BLACK';

export interface PointModel {
  index: number;
  color: PlayerColor | null;
  count: number;
}

export interface BarModel {
  white: number;
  black: number;
}

export interface BorneOffModel {
  white: number;
  black: number;
}

export interface BoardModel {
  points: PointModel[];
  bar: BarModel;
  borneOff: BorneOffModel;
}

export interface GameStateModel {
  gameId: string;
  board: BoardModel;
  currentPlayer: PlayerColor | null;
  dice: number[];
  phase: GamePhase;
  winner: PlayerColor | null;
  validMoves: MoveModel[];
}

export interface MoveModel {
  from: number;
  to: number;
}

export const BAR_INDEX = 24;
export const BEAR_OFF_INDEX = 25;
