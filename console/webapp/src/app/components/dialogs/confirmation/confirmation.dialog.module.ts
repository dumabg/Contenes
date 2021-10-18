import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfirmationDialog } from './confirmation.dialog.component';
import { MatDialogModule } from '@angular/material';
import { MaterialModule } from 'src/app/material.module';

@NgModule({
  declarations: [
    ConfirmationDialog
  ],
  imports: [
    CommonModule,
    MaterialModule,
    MatDialogModule
  ],
  exports: [
    ConfirmationDialog
  ]
})
export class ConfirmationDialogModule { }
