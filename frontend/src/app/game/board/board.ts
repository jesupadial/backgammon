import { Component, computed, input, output } from '@angular/core';
import { PointComponent } from '../point/point';
import { BAR_INDEX, BEAR_OFF_INDEX, GameStateModel, PointModel } from '../../core/models/game.models';

const TOP_LEFT_INDICES = [12, 13, 14, 15, 16, 17];
const TOP_RIGHT_INDICES = [18, 19, 20, 21, 22, 23];
const BOTTOM_LEFT_INDICES = [11, 10, 9, 8, 7, 6];
const BOTTOM_RIGHT_INDICES = [5, 4, 3, 2, 1, 0];

@Component({
  selector: 'app-board',
  imports: [PointComponent],
  templateUrl: './board.html',
  styleUrl: './board.scss'
})
export class BoardComponent {
  readonly gameState = input.required<GameStateModel>();
  readonly selectedFrom = input<number | null>(null);
  readonly pointClick = output<number>();

  protected readonly barIndex = BAR_INDEX;
  protected readonly bearOffIndex = BEAR_OFF_INDEX;
  protected readonly topLeftIndices = TOP_LEFT_INDICES;
  protected readonly topRightIndices = TOP_RIGHT_INDICES;
  protected readonly bottomLeftIndices = BOTTOM_LEFT_INDICES;
  protected readonly bottomRightIndices = BOTTOM_RIGHT_INDICES;

  protected readonly blackBarCheckers = computed(() =>
    Array.from({ length: this.gameState().board.bar.black }, (_, i) => i)
  );

  protected readonly whiteBarCheckers = computed(() =>
    Array.from({ length: this.gameState().board.bar.white }, (_, i) => i)
  );

  protected pointAt(index: number): PointModel {
    return this.gameState().board.points[index];
  }

  protected isSelected(index: number): boolean {
    return this.selectedFrom() === index;
  }
}
