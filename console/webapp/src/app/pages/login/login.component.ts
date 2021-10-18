import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { SnackBarService } from 'src/app/services/snack-bar.service';
import { LoadingComponent } from 'src/app/modules/loading/loading.component';
import { Subscription } from 'rxjs/internal/Subscription';

@Component(
  { templateUrl: 'login.component.html',
    styleUrls: ['login.component.css']
  }
)
export class LoginComponent implements OnInit, OnDestroy {
  private subscription: Subscription;
  returnUrl: string;
  username: string;
  password: string;

  @ViewChild('loading', {static: true}) loadingComponent: LoadingComponent;
  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private authenticationService: AuthenticationService,
      private snackBar: SnackBarService
  ) {
      // redirect to home if already logged in
      if (this.authenticationService.isLogged()) {
          this.router.navigate(['/']);
      }
  }

  ngOnInit() {
      // get return url from route parameters or default to '/'
      this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  onSubmit() {
    this.subscription = this.authenticationService.login(this.username, this.password, this.loadingComponent)
          .subscribe(
              data => {
                  this.router.navigate([this.returnUrl]);
              },
              error => {
                if (error.status === 401) {
                  this.snackBar.show('Incorrect email or password');
                }
              });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
