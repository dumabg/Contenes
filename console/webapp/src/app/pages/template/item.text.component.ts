import { Component, OnInit } from '@angular/core';
import { ItemText, Item } from 'src/app/models/template.model';
import { ItemBaseComponent } from './item.base.component';
import { FormBuilder, Validators, FormGroup, ValidatorFn, AbstractControl } from '@angular/forms';
import { TemplateApi } from '../../api/template.api';

@Component({
  selector: 'app-item-text',
  templateUrl: 'item.text.component.html',
  styleUrls: ['item.text.component.scss']
})
export class ItemTextComponent extends ItemBaseComponent<ItemText> {
//  get itemText(): ItemText { return this.item as ItemText; }

  constructor(private fb: FormBuilder, templateApi: TemplateApi) {
    super(templateApi);
  }

  getFormGroup(): FormGroup {
    return this.fb.group({
      text: [this.item.text, Validators.required]
    });
  }

  protected updateItemFromForm(item: ItemText) {
    item.text = this.formGroup.controls.text.value;
}
}
