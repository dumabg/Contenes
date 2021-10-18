import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpHeaderResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';
import { filter, tap } from 'rxjs/operators';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    private readonly tokenIniPos = 'Bearer '.length;

    constructor(private authenticationService: AuthenticationService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        // add authorization header with jwt token if available
        if (this.authenticationService.isLogged()) {
          request = request.clone({
            setHeaders: {
                Authorization: `Bearer ${this.authenticationService.user.token}`
            }
          });
        }

        return next.handle(request);
        // return next.handle(request).pipe(
        //   filter(event => event instanceof HttpHeaderResponse),
        //   tap((event: HttpHeaderResponse) => {
        //     const headers = event.headers;
        //     if (headers.has('Authorization')) {
        //       let token = headers.get('Authorization');
        //       token = token.substring(this.tokenIniPos);
        //       this.authenticationService.newToken(token);
        //     } else {
        //       this.authenticationService.logout();
        //     }
        //     return event;
        //   })
        // );
    }
}
