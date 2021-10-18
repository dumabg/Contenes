import { NgModule } from '@angular/core';
import {MatButtonModule, MatToolbarModule, MatCardModule, MatInputModule, MatProgressSpinnerModule,
  MatTableModule, MatPaginatorModule, MatSortModule, MatSidenavModule, MatSnackBarModule, MatIconModule,
  MatListModule
} from '@angular/material';

@NgModule({
imports: [MatButtonModule, MatToolbarModule, MatCardModule, MatInputModule, MatProgressSpinnerModule,
  MatTableModule, MatPaginatorModule, MatSortModule, MatSidenavModule, MatSnackBarModule, MatIconModule,
  MatListModule],
exports: [MatButtonModule, MatToolbarModule, MatCardModule, MatInputModule, MatProgressSpinnerModule,
  MatTableModule, MatPaginatorModule, MatSortModule, MatSidenavModule, MatSnackBarModule, MatIconModule,
  MatListModule]
})

export class MaterialModule {}
