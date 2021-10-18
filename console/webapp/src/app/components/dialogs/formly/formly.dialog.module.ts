import { NgModule } from '@angular/core';
import { FormlyDialog } from './formly.dialog.component';
import { ReactiveFormsModule } from '@angular/forms';
import { FormlyModule } from '@ngx-formly/core';
import { FormlyMaterialModule } from '@ngx-formly/material';
import { MaterialModule } from 'src/app/material.module';
import { MatDialogModule } from '@angular/material';

@NgModule({
  declarations: [
    FormlyDialog
  ],
  imports: [
    ReactiveFormsModule,
    MaterialModule,
    MatDialogModule,
    FormlyModule.forRoot(),
    FormlyMaterialModule
  ],
  exports: [
    FormlyDialog
  ]
})
export class FormlyDialogModule { }
