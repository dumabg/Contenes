import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TemplateApi } from 'src/app/api/template.api';
import { LoadingModule } from '../../modules/loading/loading.module';
import { MaterialModule } from '../../material.module';
import { MatDialogModule } from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ConfirmationDialog } from 'src/app/components/dialogs/confirmation/confirmation.dialog.component';
import { HotkeysDialog } from 'src/app/services/hotkeys.dialog/hotkeys.dialog.component';
import { TemplateComponent } from './template.component';
import { ConfirmationDialogModule } from 'src/app/components/dialogs/confirmation/confirmation.dialog.module';
import { HotkeysDialogModule } from 'src/app/services/hotkeys.dialog/hotkeys.dialog.module';
import { InputEditComponent } from 'src/app/components/input.edit/input.edit.component';
import { TemplateRoutingModule } from './template-routing.module';
import { ItemTextComponent } from './item.text.component';
import { ItemHttpComponent } from './item.http.component';
import { ItemBaseHtmlComponent } from './item.base.html.component';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: '', component: TemplateComponent }
];

@NgModule({
  declarations: [
    TemplateComponent,
    InputEditComponent,
    ItemTextComponent,
    ItemHttpComponent,
    ItemBaseHtmlComponent
  ],
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    MaterialModule,
    MatDialogModule,
    FormsModule,
    LoadingModule,
    ConfirmationDialogModule,
    HotkeysDialogModule,
    TemplateRoutingModule,
    ReactiveFormsModule,
    ConfirmationDialogModule
  ],
  providers: [
    TemplateApi
  ],
  entryComponents: [
    ConfirmationDialog,
    HotkeysDialog
  ]
})
export class TemplateModule { }
