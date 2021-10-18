import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HotkeysDialog } from './hotkeys.dialog.component';

@NgModule({
  declarations: [
    HotkeysDialog
  ],
  imports: [
    CommonModule
  ],
  exports: [
    HotkeysDialog
  ]
})
export class HotkeysDialogModule { }
