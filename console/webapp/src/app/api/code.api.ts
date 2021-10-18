import { Injectable } from '@angular/core';
import { UIRestDataSource } from './ui.rest.datasource';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class CodeApi extends UIRestDataSource {

  constructor(http: HttpClient) {
    super(http);
  }

  getCodes(num: number): Observable<string[]> {
    const callback = (): Observable<string[]> => {
      return this.post<string[]>('code/' + num.toString(), null);
    };
    return this.call(callback);
  }
}
