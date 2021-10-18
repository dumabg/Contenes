import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from 'src/environments/environment';
import { User } from '../models/user';

@Injectable({ providedIn: 'root' })
export class UserService {
    constructor(private http: HttpClient) { }

    getAll() {
        return this.http.get<User[]>(`${environment.apiRoot}/users`);
    }

    register(user: User) {
        return this.http.post(`${environment.apiRoot}/users/register`, user);
    }

    delete(id: number) {
        return this.http.delete(`${environment.apiRoot}/users/${id}`);
    }
}
