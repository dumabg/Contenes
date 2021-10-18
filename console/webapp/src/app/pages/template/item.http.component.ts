import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { ItemHttp } from 'src/app/models/template.model';
import { LoadingComponent } from 'src/app/modules/loading/loading.component';
import { TemplateApi } from 'src/app/api/template.api';
import { ItemBaseComponent } from './item.base.component';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-item-http',
  templateUrl: 'item.http.component.html',
  styleUrls: ['item.http.component.scss']
})
export class ItemHttpComponent extends ItemBaseComponent<ItemHttp> {

  constructor(private fb: FormBuilder, templateApi: TemplateApi) {
    super(templateApi);
  }

  getFormGroup(): FormGroup {
    return this.fb.group({
      text: [this.item.text, Validators.required],
      url: [this.item.url, Validators.required]
    });
  }

  protected updateItemFromForm(item: ItemHttp) {
    const controls = this.formGroup.controls;
    item.text = controls.text.value;
    item.url = controls.url.value;
  }


  testUrl() {

  }

}
