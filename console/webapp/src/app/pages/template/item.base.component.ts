import { OnInit, Input, Output, EventEmitter, ViewChild, Component, OnDestroy } from '@angular/core';
import { Item } from 'src/app/models/template.model';
import { ItemBaseHtmlComponent } from './item.base.html.component';
import { FormGroup } from '@angular/forms';
import { Subject, Subscription } from 'rxjs';
import { takeUntil, take } from 'rxjs/operators';
import { TemplateApi } from 'src/app/api/template.api';
// https://github.com/hughsk/shallow-equals
import equals from 'shallow-equals';

// Child items must derive from this base.
// The child html, must declare two templates, one with the name editingContent and
// another with the name noEditingContent inside an <app-item> tag. Exemple:
//  <app-item>
//     <ng-template #editingContent>
//          Content that must appear when is editing
//     </ng-template>
//      <ng-template #noEditingContent>
//          Content that must appear when isn't editing
//      </ng-template>
//  </app-item>
//
// Like ItemBaseComponent can't have a templateUrl, because no appears, the app-item
// is delegated to ItemBaseHtmlComponent.
//
// When editing, ItemBaseHtmlComponent has two buttons, Save Changes and Cancel Changes, that appears below
// editingContent. The button Save changes is enabled when canSaveChanges is true. For validation,
// a formGroup is used. The child classes must override getFormGroup method to provide the formGroup.
// Child class must use the formGroup property for attach it in the html: <form [formGroup]="formGroup">.

@Component({
})
export abstract class ItemBaseComponent<T extends Item> implements OnDestroy {
  private ngUnsubscribe = new Subject();

  get formGroup() {return this._formGroup; }
  // tslint:disable-next-line: variable-name
  private _formGroup: FormGroup;

  @Input() idTemplate: number;

  private formGroupSubscription: Subscription;
  @Input('editing')
  set editing(value: boolean) {
    this._itemBaseHtmlComponent.editing = value;
    this.initializeEditing();
  }
  get editing() {
    return this._itemBaseHtmlComponent.editing;
  }

  @Input('item')
  set item(value: T) {
    this._item = value;
    this.initializeEditing();
  }
  get item() {
    return this._item;
  }
  // tslint:disable-next-line: variable-name
  private _item: T;

  @Output() endEditEvent = new EventEmitter<T>();
  @ViewChild(ItemBaseHtmlComponent, {static: true})
  set itemBaseHtmlComponent(value: ItemBaseHtmlComponent) {
    this._itemBaseHtmlComponent = value;
    this._itemBaseHtmlComponent.cancelEvent
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(() => this.cancel());
    this._itemBaseHtmlComponent.saveEvent
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(() => this.save());
    this.templateApi.loadingComponent = this._itemBaseHtmlComponent.loadingComponent;
  }
  // tslint:disable-next-line: variable-name
  private _itemBaseHtmlComponent: ItemBaseHtmlComponent;

  constructor(private templateApi: TemplateApi) {
  }

  private initializeEditing() {
    if ((this._item !== undefined) && (this._itemBaseHtmlComponent !== undefined) && (this._itemBaseHtmlComponent.editing)) {
      if (this.formGroupSubscription !== undefined) {
        this.formGroupSubscription.unsubscribe();
      }
      this._formGroup = this.getFormGroup();
      this.formGroupSubscription = this._formGroup.statusChanges
        .pipe(takeUntil(this.ngUnsubscribe))
        .subscribe(() => this._itemBaseHtmlComponent.canSaveChanges = this._formGroup.valid);
      this._itemBaseHtmlComponent.canSaveChanges = this._formGroup.valid;
    }
  }

  save() {
    const formItem = {...this.item};
    this.updateItemFromForm(formItem);
    if (this.item.id === undefined) {
      this.templateApi.addItem(this.idTemplate, formItem)
        .pipe(take(1))
        .subscribe(id => {
          this.item = formItem;
          this.item.id = id;
          this.endEditEvent.emit(this._item);
        });
    } else {
      if (equals(this.item, formItem)) {
        this.endEditEvent.emit(null);
      } else {
        this.templateApi.updateItem(this.idTemplate, formItem)
          .pipe(take(1))
          .subscribe(() => {
            this.item = formItem;
            this.endEditEvent.emit(this._item);
          });
      }
    }
  }

  cancel() {
    this.endEditEvent.emit(null);
  }

  protected abstract getFormGroup(): FormGroup;

  protected abstract updateItemFromForm(item: T): void;

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
