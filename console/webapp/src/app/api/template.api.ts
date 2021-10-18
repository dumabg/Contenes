import { Injectable } from '@angular/core';
import { Template, Item } from '../models/template.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { UIRestDataSource } from './ui.rest.datasource';

@Injectable()
export class TemplateApi extends UIRestDataSource {

  constructor(http: HttpClient) {
    super(http);
  }

  getTemplates(): Observable<Template[]> {
    const callback = (): Observable<Template[]> => {
      return this.get<Template[]>('templates');
    };
    return this.call(callback);
  }

  addTemplate(title: string): Observable<number> {
    const callback = (): Observable<number> => {
      return this.postMultiPart<number>('template', {name: title});
    };
    return this.call(callback);
  }

  deleteTemplate(id: number): Observable<void> {
    const callback = (): Observable<void> => {
      return this.delete<void>('template/' + id.toString());
    };
    return this.call(callback);
  }

  getTemplate(id: number): Observable<Template> {
    const callback = (): Observable<Template> => {
      return this.get<Template>('template/' + id.toString());
    };
    return this.call(callback);
  }

  updateTemplate(id: number, name: string): Observable<void> {
    const callback = (): Observable<void> => {
      return this.putMultiPart<void>('template', {id, name});
    };
    return this.call(callback);
  }

  addItem(templateId: number, item: Item): Observable<number> {
    const callback = (): Observable<number> => {
      return this.postMultiPart<number>('template/' + templateId.toString(), item);
    };
    return this.call(callback);
  }

  updateItem(templateId: number, item: Item): Observable<void> {
    const callback = (): Observable<void> => {
      return this.putMultiPart<void>('template/' + templateId.toString(), item);
    };
    return this.call(callback);
  }

  deleteItem(templateId: number, itemId: number): Observable<void> {
    const callback = (): Observable<void> => {
      return this.delete<void>('template/' + templateId.toString() + '/' + itemId.toString());
    };
    return this.call(callback);
  }

  changePositionItems(templateId: number, idItem1: number, positionItem1: number, idItem2: number, positionItem2: number) {
    const callback = (): Observable<void> => {
      return this.putMultiPart<void>('template/' + templateId.toString(), {
          t: 'order',
          id1: idItem1,
          pos1: positionItem1,
          id2: idItem2,
          pos2: positionItem2
        });
    };
    return this.call(callback);
  }
}
