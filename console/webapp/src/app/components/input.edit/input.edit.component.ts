import { Component, Input, EventEmitter, Output } from '@angular/core';

export class SaveEvent {
  value: string;
  oldValue: string;
}

@Component({
  selector: 'app-input-edit',
  templateUrl: 'input.edit.component.html'
})
export class InputEditComponent {
  @Input() value: string;
  @Output() saveEvent = new EventEmitter<SaveEvent>();
  editing = false;
  private oldValue: string;

  edit() {
    this.oldValue = this.value;
    this.editing = true;
  }

  save() {
    this.saveEvent.emit({value: this.value, oldValue: this.oldValue});
    this.editing = false;
  }

  cancel() {
    this.value = this.oldValue;
    this.editing = false;
  }
}
