import { Injectable } from '@angular/core';
import { UIRestDataSource } from './ui.rest.datasource';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, zip } from 'rxjs';
import { View } from '../models/view.model';
import { TemplateApi } from './template.api';
import { Template } from '../models/template.model';


@Injectable()
export class ViewApi extends UIRestDataSource {

  constructor(http: HttpClient, private templateApi: TemplateApi) {
    super(http);
  }

  getViews(): Observable<[Template[], View[]]> {
    const callback = (): Observable<[Template[], View[]]> => {
      return forkJoin<Template[], View[]>([this.templateApi.getTemplates(), this.get<View[]>('views')]);
    };
    return this.call(callback);
  }

  addView(name: string, templateId: number, title: string): Observable<number> {
    const callback = (): Observable<number> => {
      return this.postMultiPart<number>('view', {name, templateId, title});
    };
    return this.call(callback);
  }

  editView(id: number, name: string, templateId: number, title: string): Observable<void> {
    const callback = (): Observable<void> => {
      return this.putMultiPart<void>('view', {id, name, templateId, title});
    };
    return this.call(callback);
  }


  deleteView(id: number): Observable<void> {
    const callback = (): Observable<void> => {
      return this.delete<void>('view/' + id.toString());
    };
    return this.call(callback);
  }
}
