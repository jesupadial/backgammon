import { Component, input } from '@angular/core';

@Component({
  selector: 'app-dice',
  templateUrl: './dice.html',
  styleUrl: './dice.scss'
})
export class DiceComponent {
  readonly dice = input.required<number[]>();
}
