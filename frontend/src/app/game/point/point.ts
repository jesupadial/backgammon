import { Component, computed, input, output } from '@angular/core';
import { PointModel } from '../../core/models/game.models';

@Component({
  selector: 'app-point',
  templateUrl: './point.html',
  styleUrl: './point.scss'
})
export class PointComponent {
  readonly point = input.required<PointModel>();
  readonly isSelected = input(false);
  readonly isTop = input(false);
  readonly isDark = input(false);
  readonly pointClick = output<void>();

  protected readonly checkerIndices = computed(() =>
    Array.from({ length: this.point().count }, (_, i) => i)
  );
}
