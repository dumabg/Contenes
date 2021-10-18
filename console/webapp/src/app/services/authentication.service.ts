import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { User } from '../models/user';
import { LoginApi } from '../api/login.api';
import { Observable } from 'rxjs/internal/Observable';
import { LoadingComponent } from '../modules/loading/loading.component';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    // tslint:disable-next-line: variable-name
    private _user: User;
    get user(): User {return this._user;}

    constructor(private loginApi: LoginApi) {
        // this._user = JSON.parse(localStorage.getItem('user')) as User;
        const token = localStorage.getItem('token');
        if (token != null) {
          this._user = {
            name: localStorage.getItem('user'),
            token };
          }
    }

    isLogged(): boolean {
      return this.user != null;
    }

    login(username: string, password: string, loadingComponent: LoadingComponent): Observable<User> {
      this.loginApi.loadingComponent = loadingComponent;
      return this.loginApi.login(username, password)
            .pipe(map(user => {
                // store user details and jwt token in local storage to keep user logged in between page refreshes
                // localStorage.setItem('user', JSON.stringify(user));
                localStorage.setItem('user', username);
                localStorage.setItem('token', user.token);
                user.name = username;
                this._user = user;
                return user;
            }));
    }

    logout() {
        // remove user from local storage and set current user to null
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        this._user = null;
    }

    newToken(token: string) {
      this._user.token = token;
      localStorage.setItem('token', token);
    }
}
