import {Component, Inject} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {FormlyFieldConfig} from '@ngx-formly/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

  // form = new FormGroup({});
  // model = { email: 'email@gmail.com' };
  // fields: FormlyFieldConfig[] = [{
  //   key: 'email',
  //   type: 'input',
  //   templateOptions: {
  //     label: 'Email address',
  //     placeholder: 'Enter email',
  //     required: true,
  //   }
  // }];

export interface FormlyDialogData {
  title: string;
  form: FormGroup | undefined;
  model: any;
  fields: FormlyFieldConfig[];
}

@Component({
  selector: 'app-formly-dialog',
  templateUrl: 'formly.dialog.component.html',
  styleUrls: ['formly.dialog.component.scss']
})
// tslint:disable-next-line: component-class-suffix
export class FormlyDialog {
  constructor(
    public dialogRef: MatDialogRef<FormlyDialog>,
    @Inject(MAT_DIALOG_DATA) public data: FormlyDialogData) {}

  onCancelClick(): void {
    this.dialogRef.close();
  }
}
