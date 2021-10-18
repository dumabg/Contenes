import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

export class RestDataSource {

  constructor(private http: HttpClient) {}

  post<T>(uri: string, obj: any): Observable<T> {
    return this.http.post<T>(environment.apiRoot + uri, obj);
  }

  postMultiPart<T>(uri: string, obj: any): Observable<T> {
    const formData = this.objectToFormData(obj);
    return this.http.post<T>(environment.apiRoot + uri, formData);
  }

  get<T>(uri: string): Observable<T> {
    return this.http.get<T>(environment.apiRoot + uri);
  }

  put<T>(uri: string, obj: any): Observable<T> {
    return this.http.put<T>(environment.apiRoot + uri, obj);
  }

  putMultiPart<T>(uri: string, obj: any): Observable<T> {
    const formData = this.objectToFormData(obj);
    return this.http.put<T>(environment.apiRoot + uri, formData);
  }

  delete<T>(uri: string): Observable<T> {
    return this.http.delete<T>(environment.apiRoot + uri);
  }

  private objectToFormData(obj: any): FormData {
    const formData = new FormData();
    for (const key in obj) {
      if (key === 'id') {
        const value = Reflect.get(obj, key);
        if (value !== undefined) {
          formData.append(key, value.toString());
        }
      } else {
        formData.append(key, Reflect.get(obj, key).toString());
      }
    }
    return formData;
  }
}
