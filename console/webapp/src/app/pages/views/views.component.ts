import { Component, ViewChild, OnInit } from '@angular/core';
import { LoadingComponent } from 'src/app/modules/loading/loading.component';
import { MatPaginator, MatSort, MatTableDataSource, MatDialog } from '@angular/material';
import { View } from 'src/app/models/view.model';
import { ViewApi } from 'src/app/api/view.api';
import { take, filter } from 'rxjs/operators';
import { Template } from 'src/app/models/template.model';
import { FormlyDialog } from 'src/app/components/dialogs/formly/formly.dialog.component';
import { FormGroup } from '@angular/forms';
import { ConfirmationDialog, ConfirmationDialogResult } from 'src/app/components/dialogs/confirmation/confirmation.dialog.component';
import { Observable } from 'rxjs';

class ViewWithTemplate extends View {
  templateName: string;
}

@Component({
  templateUrl: 'views.component.html',
  styleUrls: ['views.component.scss']
})
export class ViewsComponent implements OnInit {
  displayedColumns: string[] = ['name', 'title', 'templateName', 'iconEdit', 'iconDelete'];
  dataSource = new MatTableDataSource<ViewWithTemplate>();
  templates: Template[];
  @ViewChild(LoadingComponent, {static: true}) loadingComponent: LoadingComponent;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private viewApi: ViewApi,
              private dialog: MatDialog) {
    }

  ngOnInit() {
    this.viewApi.loadingComponent = this.loadingComponent;
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.dataSource.sortingDataAccessor = (data, sortHeaderId) =>
                                           data[sortHeaderId] === undefined ? undefined : data[sortHeaderId].toLocaleLowerCase();
    this.viewApi.getViews()
      .pipe(take(1))
      .subscribe(([templates, views]) => {
        this.templates = templates;
        const viewsWithTemplate = views.map<ViewWithTemplate>(view => {
          return {... view, templateName: this.templateName(view.templateId)};
        });
        this.dataSource.data = viewsWithTemplate;
      });
  }

  private templateName(templateId: number): string {
    const templateFound = this.templates.find(template => template.id === templateId);
    return templateFound === undefined ? '' : templateFound.name;
  }

  newView() {
    this.showDialog({}).
    subscribe(result => {
      this.viewApi.addView(result.name, result.templateId, result.title)
        .pipe(take(1))
        .subscribe((viewId: number) => {
          const viewWithTemplate: ViewWithTemplate = {id: viewId,
                              name: result.name,
                              templateId: result.templateId,
                              title: result.title,
                              templateName: this.templateName(result.templateId)};
          this.dataSource.data.push(viewWithTemplate);
          this.refreshViews();
      });
    });
  }

  private showDialog(model: {}): Observable<any> {
    return this.dialog.open(FormlyDialog, {
      width: '250px',
      data: {
        title: 'New view',
        form: new FormGroup({}),
        model,
        fields: [{
          key: 'name',
          type: 'input',
          templateOptions: {
            label: 'Name',
            required: true
          }
        }, {
          key: 'templateId',
          type: 'select',
          templateOptions: {
            label: 'Template',
            required: true,
            options: this.templates.map(template => ({ value: template.id, label: template.name }))
              .sort((a, b) => a.label.localeCompare(b.label))
          }
        }, {
          key: 'title',
          type: 'input',
          templateOptions: {
            label: 'Title',
            required: true
          }
        }]
      }
    }).afterClosed().pipe(filter(result => result !== undefined), take(1));
  }

  edit(id: number) {
    const index = this.viewIndex(id);
    const view = this.dataSource.data[index];
    this.showDialog({
      name: view.name,
      templateId: view.templateId,
      title: view.title
    }).
    subscribe(result => {
      const newName = result.name;
      const newTemplateId = result.templateId;
      const newTitle = result.title;
      this.viewApi.editView(view.id, newName, newTemplateId, newTitle)
        .pipe(take(1))
        .subscribe(() => {
          view.name = newName;
          view.templateId = newTemplateId;
          view.title = newTitle;
          this.dataSource.data[index] = view;
          this.refreshViews();
      });
    });
  }

  delete(id: number) {
    const index = this.viewIndex(id);
    const title = this.dataSource.data[index].name;
    this.dialog.open(ConfirmationDialog, {
      width: '250px',
      data: {
        title: 'Delete this view?',
        content: title
      }
    }).afterClosed().pipe(
        filter(result => result === ConfirmationDialogResult.Yes),
        take(1)
    ).
    subscribe(() => {
      this.viewApi.deleteView(id)
        .pipe(take(1))
        .subscribe(() => {
          const i = this.viewIndex(id);
          if (i !== -1) {
            this.dataSource.data.splice(i, 1);
            this.refreshViews();
          }
        });
    });
  }

  private viewIndex(id: number): number {
    return this.dataSource.data.findIndex(viewWithTemplate => viewWithTemplate.id === id);
  }

  private refreshViews() {
    // push doesn't refresh the table, because Angular detects changes on references,
    // and the reference is template. Slice creates a copy of part of the array,
    // but used like this, with no arguments, it just makes a shallow copy of the entire array.
    this.dataSource.data = this.dataSource.data.slice();
  }
}
