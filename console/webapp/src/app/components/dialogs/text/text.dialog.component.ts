import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Inject, Component } from '@angular/core';

export interface DialogData {
  title: string;
  text: string;
}

@Component({
  templateUrl: 'text.dialog.component.html',
  styleUrls: ['text.dialog.component.scss']
})
// tslint:disable-next-line: component-class-suffix
export class TextDialog {

  constructor(
    public dialogRef: MatDialogRef<TextDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onCancelClick(): void {
    this.dialogRef.close();
  }
}
