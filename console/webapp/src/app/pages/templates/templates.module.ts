import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TemplateApi } from 'src/app/api/template.api';
import { TemplatesComponent } from './templates.component';
import { LoadingModule } from '../../modules/loading/loading.module';
import { MaterialModule } from '../../material.module';
import { MatDialogModule, MatDialog } from '@angular/material';
import { TextDialog } from '../../components/dialogs/text/text.dialog.component';
import { FormsModule } from '@angular/forms';
import { ConfirmationDialog } from 'src/app/components/dialogs/confirmation/confirmation.dialog.component';
import { HotkeysDialog } from 'src/app/services/hotkeys.dialog/hotkeys.dialog.component';
import { ConfirmationDialogModule } from 'src/app/components/dialogs/confirmation/confirmation.dialog.module';
import { HotkeysDialogModule } from 'src/app/services/hotkeys.dialog/hotkeys.dialog.module';
import { RouterModule, Routes } from '@angular/router';
import { TextDialogModule } from '../../components/dialogs/text/text.dialog.module';
import { Hotkeys } from '../../services/hotkeys.service';


const routes: Routes = [
  { path: '', component: TemplatesComponent }
];

@NgModule({
  declarations: [
    TemplatesComponent
  ],
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    MaterialModule,
    MatDialogModule,
    FormsModule,
    LoadingModule,
    ConfirmationDialogModule,
    TextDialogModule,
    HotkeysDialogModule
  ],
  providers: [
    TemplateApi,
    MatDialog,
    Hotkeys
  ],
  entryComponents: [
    ConfirmationDialog,
    HotkeysDialog,
    TextDialog
  ]
})
export class TemplatesModule { }
