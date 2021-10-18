import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, Inject } from '@angular/core';


export enum ConfirmationDialogResult {
  Yes, No
}

export interface ConfirmationDialogData {
  title: string;
  content: string;
}

@Component({
  templateUrl: 'confirmation.dialog.component.html',
  styleUrls: ['confirmation.dialog.component.scss']
})
// tslint:disable-next-line: component-class-suffix
export class ConfirmationDialog {

  ConfirmationDialogResult: any = ConfirmationDialogResult;

  constructor(
    public dialogRef: MatDialogRef<ConfirmationDialog>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmationDialogData) {}
}
