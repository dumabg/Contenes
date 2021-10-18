import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TextDialog } from './text.dialog.component';
import { MaterialModule } from 'src/app/material.module';
import { MatDialogModule } from '@angular/material';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    TextDialog
  ],
  imports: [
    CommonModule,
    MaterialModule,
    MatDialogModule,
    FormsModule
  ],
  exports: [
    TextDialog
  ]
})
export class TextDialogModule { }
