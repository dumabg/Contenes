import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http/';
import { LoginComponent } from './pages/login/login.component';
import { ReactiveFormsModule } from '@angular/forms';
import {FormsModule} from '@angular/forms';
import { MaterialModule } from './material.module';
import { LoginApi } from './api/login.api';
import { SnackBarService } from './services/snack-bar.service';
import { JwtInterceptor } from './auth/jwt.interceptor';
import { HttpErrorInterceptor } from './util/httperror.interceptor';
import { LoadingModule } from './modules/loading/loading.module';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    MaterialModule,
    LoadingModule
  ],
  providers: [
    LoginApi,
    SnackBarService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
