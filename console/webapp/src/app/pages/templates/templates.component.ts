import { Component, ViewChild, OnInit } from '@angular/core';
import { TemplateApi } from 'src/app/api/template.api';
import { Template } from 'src/app/models/template.model';
import { LoadingComponent } from 'src/app/modules/loading/loading.component';
import { MatDialog, MatTableDataSource, MatPaginator, MatSort } from '@angular/material';
import { TextDialog } from '../../components/dialogs/text/text.dialog.component';
import { filter, take } from 'rxjs/operators';
import { ConfirmationDialog, ConfirmationDialogResult } from 'src/app/components/dialogs/confirmation/confirmation.dialog.component';
import { Hotkeys } from '../../services/hotkeys.service';

@Component({
  selector: 'app-templates',
  templateUrl: './templates.component.html',
  styleUrls: ['./templates.component.scss']
})
export class TemplatesComponent implements OnInit {
  displayedColumns: string[] = ['name', 'iconEdit', 'iconDelete'];
  dataSource = new MatTableDataSource<Template>();
  @ViewChild(LoadingComponent, {static: true}) loadingComponent: LoadingComponent;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private templateApi: TemplateApi,
              private dialog: MatDialog,
              hotkeys: Hotkeys) {
    hotkeys.addShortcut({ keys: '+', description: 'New template' })
      .subscribe(() => this.newTemplate());
    hotkeys.addShortcut({ keys: 'control.meta', description: 'Menu' })
      .subscribe(() => this.newTemplate());
  }

  ngOnInit() {
    this.templateApi.loadingComponent = this.loadingComponent;
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.dataSource.sortingDataAccessor = (data, sortHeaderId) => data[sortHeaderId].toLocaleLowerCase();
    this.templateApi.getTemplates()
      .pipe(take(1))
      .subscribe(data => {
        this.dataSource.data = data;
      });
  }

  newTemplate() {
    this.dialog.open(TextDialog, {
      width: '250px',
      data: {
        title: 'New template',
        name: ''
      }
    }).afterClosed().pipe(
        filter(result => result !== undefined),
        take(1)
    ).
    subscribe((templateName: string) => {
      this.templateApi.addTemplate(templateName)
        .pipe(take(1))
        .subscribe((templateId: number) => {
          const template: Template = {id: templateId, name: templateName, items: []};
          this.dataSource.data.push(template);
          this.refreshTemplates();
      });
    });
  }

  delete(id: number) {
    const index = this.templateIndex(id);
    const title = this.dataSource.data[index].name;
    this.dialog.open(ConfirmationDialog, {
      width: '250px',
      data: {
        title: 'Delete this template?',
        content: title
      }
    }).afterClosed().pipe(
        filter(result => result === ConfirmationDialogResult.Yes),
        take(1)
    ).
    subscribe(() => {
      this.templateApi.deleteTemplate(id)
        .pipe(take(1))
        .subscribe(() => {
          const i = this.templateIndex(id);
          if (i !== -1) {
            this.dataSource.data.splice(i, 1);
            this.refreshTemplates();
          }
        });
    });
  }

  private refreshTemplates() {
    // push doesn't refresh the table, because Angular detects changes on references,
    // and the reference is template. Slice creates a copy of part of the array,
    // but used like this, with no arguments, it just makes a shallow copy of the entire array.
    this.dataSource.data = this.dataSource.data.slice();
  }

  private templateIndex(id: number): number {
    return this.dataSource.data.findIndex(template => template.id === id);
  }


}
