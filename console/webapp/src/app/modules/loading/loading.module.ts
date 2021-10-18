import { NgModule } from '@angular/core';
import { LoadingComponent } from './loading.component';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material';

@NgModule({
  declarations: [
    LoadingComponent
  ],
  imports: [
    CommonModule,
    MatProgressSpinnerModule
  ],
  exports: [
    LoadingComponent
  ]
})
export class LoadingModule { }
