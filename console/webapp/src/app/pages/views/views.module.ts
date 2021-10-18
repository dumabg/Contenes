import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../material.module';
import { MatDialogModule, MatDialog } from '@angular/material';
import { FormsModule } from '@angular/forms';
import { LoadingModule } from '../../modules/loading/loading.module';
import { ConfirmationDialogModule } from '../../components/dialogs/confirmation/confirmation.dialog.module';
import { HotkeysDialogModule } from '../../services/hotkeys.dialog/hotkeys.dialog.module';
import { RouterModule, Routes } from '@angular/router';
import { ConfirmationDialog } from 'src/app/components/dialogs/confirmation/confirmation.dialog.component';
import { HotkeysDialog } from 'src/app/services/hotkeys.dialog/hotkeys.dialog.component';
import { ViewApi } from 'src/app/api/view.api';
import { ViewsComponent } from './views.component';
import { FormlyDialogModule } from 'src/app/components/dialogs/formly/formly.dialog.module';
import { FormlyDialog } from 'src/app/components/dialogs/formly/formly.dialog.component';
import { TemplateApi } from '../../api/template.api';

const routes: Routes = [
  { path: '', component: ViewsComponent }
];

@NgModule({
  declarations: [
    ViewsComponent
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
    FormlyDialogModule
  ],
  providers: [
    ViewApi,
    MatDialog,
    TemplateApi
  ],
  entryComponents: [
    FormlyDialog,
    ConfirmationDialog,
    HotkeysDialog
  ]
})
export class ViewsModule {}
