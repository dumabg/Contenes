import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, filter} from 'rxjs/operators';
import { AuthenticationService } from '../services/authentication.service';
import { Router, ActivationEnd, ActivatedRoute } from '@angular/router';
import { SnackBarService } from '../services/snack-bar.service';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

    constructor(private authenticationService: AuthenticationService,
                private router: Router,
                private activatedRoute: ActivatedRoute,
                private snackBar: SnackBarService) {
    }

    intercept(req: HttpRequest<any>, next: HttpHandler):
        Observable<HttpEvent<any>> {
          return next.handle(req).pipe(
             catchError( (error) => {
              if (error instanceof HttpErrorResponse) {
                  switch (error.status) {
                      case 401:
                          //   const intercept401 = this.activatedRoute.snapshot.url[0].path !== 'login';
                          // if (intercept401) {
                            this.authenticationService.logout();
                            this.router.navigateByUrl('/login');
//                          }
                  }
                  this.snackBar.show(error.statusText + ': ' + error.message);
              }
              return throwError(error);
          }),
        );
    }
}
