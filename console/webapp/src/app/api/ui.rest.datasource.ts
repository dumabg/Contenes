import { RestDataSource } from './rest.datasource';
import { LoadingComponent } from '../modules/loading/loading.component';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

export class UIRestDataSource extends RestDataSource {

  loadingComponent: LoadingComponent;

  constructor(http: HttpClient) {
    super(http);
  }

  protected call<T>(method: () => Observable<T>): Observable<T> {
    if (this.loadingComponent === undefined) {
      return method();
    }
    this.loadingComponent.show();
    return method().pipe(
      tap(complete => this.loadingComponent.hide(),
        error => this.loadingComponent.hide())
    );
  }
}
