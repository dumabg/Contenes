import { Component, ContentChild, TemplateRef, ElementRef, ViewChild, OnInit, Output, EventEmitter, OnDestroy } from '@angular/core';
import { LoadingComponent } from 'src/app/modules/loading/loading.component';

@Component({
  selector: 'app-item',
  templateUrl: 'item.base.html.component.html',
  styleUrls: ['item.base.html.component.scss']
})
export class ItemBaseHtmlComponent {
  editing: boolean;
  canSaveChanges: boolean;

  @ContentChild('noEditingContent', {static: true}) noEditingContent: TemplateRef<ElementRef>;
  @ContentChild('editingContent', {static: true}) editingContent: TemplateRef<ElementRef>;
  @ViewChild(LoadingComponent, {static: true}) loadingComponent: LoadingComponent;
  @Output() cancelEvent = new EventEmitter<void>();
  @Output() saveEvent = new EventEmitter<void>();
}
