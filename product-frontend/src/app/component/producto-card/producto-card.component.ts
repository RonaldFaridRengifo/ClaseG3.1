import { Component, Input } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { Producto } from '../../model/producto';

@Component({
  selector: 'app-producto-card',
  standalone: true,
  imports: [DecimalPipe],
  templateUrl: './producto-card.component.html',
  styleUrl: './producto-card.component.css'
})
export class ProductoCardComponent {
  @Input() producto!: Producto;
}
