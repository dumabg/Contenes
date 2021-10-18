import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { HttpClient } from '@angular/common/http';
import { UIRestDataSource } from './ui.rest.datasource';

@Injectable()
export class LoginApi extends UIRestDataSource {

  constructor(http: HttpClient) {
    super(http);
  }

  login(user: string, password: string): Observable<User> {
    const json = {
      email: user,
      password
    };
    const callback = (): Observable<User> => {
      return this.post<User>('auth/login', json);
    }
    return this.call(callback);
  }
}
