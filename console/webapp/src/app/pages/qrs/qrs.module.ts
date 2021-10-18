import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { QRsComponent } from './qrs.component';
import { MaterialModule } from '../../material.module';
import { NgxPrintModule } from 'ngx-print';
import { MatStepperModule, MatSliderModule, MatRadioModule } from '@angular/material';
import { ReactiveFormsModule } from '@angular/forms';
import { LoadingModule } from 'src/app/modules/loading/loading.module';
import { CodeApi } from '../../api/code.api';

const routes: Routes = [
  { path: '', component: QRsComponent }
];

@NgModule({
  declarations: [
    QRsComponent
  ],
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    MaterialModule,
    NgxPrintModule,
    MatStepperModule,
    ReactiveFormsModule,
    MatSliderModule,
    MatRadioModule,
    LoadingModule
  ],
  providers: [
    CodeApi
  ]
})
export class QrsModule { }
