import { Component } from '@angular/core';

@Component(
  { selector: 'app-loading',
    templateUrl: 'loading.component.html',
    styleUrls: ['loading.component.scss']
  })
export class LoadingComponent {
  isLoading = false;

  show() {
    this.isLoading = true;
  }

  hide() {
    this.isLoading = false;
  }
}
