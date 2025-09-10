import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductoService } from '../../service/producto.service';
import { Producto } from '../../model/producto';
import { ProductoCardComponent } from '../producto-card/producto-card.component';

@Component({
  selector: 'app-producto-list',
  standalone: true,
  imports: [CommonModule, ProductoCardComponent],
  templateUrl: './producto-list.component.html',
  styleUrl: './producto-list.component.css'
})
export class ProductoListComponent implements OnInit {
  productos: Producto[] = [];

  constructor(private productoService: ProductoService) { }

  ngOnInit(): void {
    this.productos = this.productoService.getProductos();
  }
}
