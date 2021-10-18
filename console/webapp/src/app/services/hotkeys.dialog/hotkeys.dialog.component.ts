import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  templateUrl: 'hotkeys.dialog.component.html',
  styleUrls: ['hotkeys.dialog.component.scss']
})
// tslint:disable-next-line: component-class-suffix
export class HotkeysDialog {
  hotkeys = Array.from(this.data);

  constructor(@Inject(MAT_DIALOG_DATA) public data) { }

}
