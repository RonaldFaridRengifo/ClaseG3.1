import { Routes } from '@angular/router';
import { ProductoListComponent } from './component/producto-list/producto-list.component';

export const routes: Routes = [
  { path: '', component: ProductoListComponent },
  { path: 'productos', component: ProductoListComponent }
];
