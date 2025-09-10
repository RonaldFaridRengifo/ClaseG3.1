import { Injectable } from '@angular/core';
import { Producto } from '../model/producto';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  constructor() { }

  getProductos(): Producto[] {
    return [
      {
        id: 1,
        nombre: 'Laptop Gaming',
        precio: 1299.99,
        imagenUrl: 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400'
      },
      {
        id: 2,
        nombre: 'Smartphone Pro',
        precio: 899.99,
        imagenUrl: 'https://images.unsplash.com/photo-1511707171638-5f8978b6d6bb?w=400'
      },
      {
        id: 3,
        nombre: 'Auriculares Inalámbricos',
        precio: 199.99,
        imagenUrl: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400'
      },
      {
        id: 4,
        nombre: 'Cámara Digital',
        precio: 599.99,
        imagenUrl: 'https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=400'
      },
      {
        id: 5,
        nombre: 'Tablet Pro',
        precio: 749.99,
        imagenUrl: 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400'
      },
      {
        id: 6,
        nombre: 'Smartwatch',
        precio: 299.99,
        imagenUrl: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400'
      }
    ];
  }
}
