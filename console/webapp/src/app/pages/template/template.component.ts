import { Component, ViewChild, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TemplateApi } from 'src/app/api/template.api';
import { Template, Item, ItemText, ItemHttp } from 'src/app/models/template.model';
import { LoadingComponent } from 'src/app/modules/loading/loading.component';
import { SaveEvent } from 'src/app/components/input.edit/input.edit.component';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material';
import { ConfirmationDialog, ConfirmationDialogResult } from 'src/app/components/dialogs/confirmation/confirmation.dialog.component';
import { filter, tap, take } from 'rxjs/operators';

@Component({
  selector: 'app-template',
  templateUrl: 'template.component.html',
  styleUrls: ['template.component.scss']
})
export class TemplateComponent implements OnInit {
    id: number;
    template: Template;
    @ViewChild(LoadingComponent, {static: true}) loadingComponent: LoadingComponent;
    displayedColumns: string[] = ['iconType', 'title', 'iconEdit', 'iconDelete', 'iconUp', 'iconDown'];
    editing = false;
    editingItemId: number;

    constructor(private templateApi: TemplateApi,
                private dialog: MatDialog,
                private router: Router,
                activatedRoute: ActivatedRoute) {
      this.template = {id: 0, name: '', items: []};
      this.id = activatedRoute.snapshot.params.id;
    }

    onEndEditEvent(item: Item) {
      this.editing = false;
      if (item == null) {
        this.removeTemplateItem(undefined);
      } else {
        const index = this.itemIndex(this.editingItemId);
        this.template.items[index] = item;
        this.refreshItems();
      }
    }

    ngOnInit() {
      this.templateApi.loadingComponent = this.loadingComponent;
      this.templateApi.getTemplate(this.id)
        .pipe(take(1))
        .subscribe({
          next: template => {
            template.items = template.items.sort((item1, item2) => item1.position - item2.position);
            this.template = template;
          },
          error: (e: HttpErrorResponse) => {
            if (e.status === 404) {
            this.router.navigateByUrl('/templates');
            }
          }
        });
    }

    onSaveName(saveEvent: SaveEvent) {
      this.templateApi.updateTemplate(this.id, saveEvent.value)
        .pipe(take(1))
        .subscribe({
          next: () => {
            this.template.name = saveEvent.value;
          },
          error: () => {
            this.template.name = saveEvent.oldValue;
          }
        });
    }

    newItemText() {
      this.newItem(new ItemText(this.lastPosition()));
    }

    newItemHttp() {
      this.newItem(new ItemHttp(this.lastPosition()));
    }

    private lastPosition(): number {
      return Math.max(...this.template.items.map(item => item.position), 0) + 1;
    }

    private newItem(item: Item) {
      this.template.items.push(item);
      this.edit(undefined);
      this.refreshItems();
    }

    up(index: number) {
      const item1 = this.template.items[index];
      const item2 = this.template.items[index - 1];
      this.swapItems(item1, item2);
    }

    private swapItems(item1: Item, item2: Item) {
      this.templateApi.changePositionItems(this.id, item1.id, item2.position, item2.id, item1.position)
        .pipe(take(1))
        .subscribe(() => {
          const item1Position = item1.position;
          item1.position = item2.position;
          item2.position = item1Position;
          this.template.items = this.template.items.sort((i1, i2) => i1.position - i2.position);
          this.refreshItems();
      });
    }

    down(index: number) {
      const item1 = this.template.items[index + 1];
      const item2 = this.template.items[index];
      this.swapItems(item1, item2);
    }

    edit(idItem: number) {
      this.editingItemId = idItem;
      this.editing = true;
    }

    delete(idItem: number) {
      this.edit(idItem);
      this.dialog.open(ConfirmationDialog, {
        width: '250px',
        data: {
          title: 'Delete this item?',
          content: ''
        }
      }).afterClosed().pipe(
          tap(() => this.editing = false),
          filter(result => result === ConfirmationDialogResult.Yes),
          take(1)
      ).
      subscribe(() => {
        this.templateApi.deleteItem(this.id, idItem)
          .pipe(take(1))
          .subscribe(() => {
            this.removeTemplateItem(idItem);
          });
      });
    }

  private removeTemplateItem(idItem: number) {
    const i = this.itemIndex(idItem);
    if (i !== -1) {
      this.template.items.splice(i, 1);
      this.refreshItems();
    }
  }

    private itemIndex(idItem: number): number {
      return this.template.items.findIndex(item => item.id === idItem);
    }

    private refreshItems() {
      // push doesn't refresh the table, because Angular detects changes on references,
      // and the reference is items. Slice creates a copy of part of the array,
      // but used like this, with no arguments, it just makes a shallow copy of the entire array.
      this.template.items = this.template.items.slice();
    }
}
