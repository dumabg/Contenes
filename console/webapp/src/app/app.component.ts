import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { Router, ActivationEnd, Data } from '@angular/router';
import { filter, map, takeUntil } from 'rxjs/operators';
import { MatDrawer } from '@angular/material';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  private ngUnsubscribe = new Subject();

  links = [];
  position = '';

  @ViewChild('drawer', {static: false}) drawer: MatDrawer;
  // tslint:disable-next-line: variable-name
  private _headerVisible = true;
  get headerVisible(): boolean {
    return this._headerVisible;
  }
  set headerVisible(value: boolean) {
    this._headerVisible = value;
    if (!value) {
      this.drawer.close();
    }
  }

  constructor(private router: Router) {
    // Create links based on router data configuration
    router.config.forEach(route => {
      const data = route.data;
      if (data !== undefined) {
        const title = data.title;
        if (title !== undefined) {
          const drawerLink = data.drawerLink;
          const canAdd = (drawerLink === undefined) || (drawerLink);
          if (canAdd) {
            this.links.push( {
              name: title,
              path: route.path
            });
          }
        }
      }
    });
  }

  private value(data: Data, propertyName: string, defaultValue: any): any {
      if (propertyName in data) {
        return data[propertyName];
      } else {
        return defaultValue;
      }
  }

  ngOnInit() {
    this.router.events.pipe(
      filter(event => event instanceof ActivationEnd),
      map((event: ActivationEnd) => event.snapshot.routeConfig),
      takeUntil(this.ngUnsubscribe))
    .subscribe(route => {
        const data = route.data;
        if (data === undefined) {
          this.headerVisible = true;
          this.position = '';
        } else {
          this.position = this.value(data, 'title', '');
          this.headerVisible = this.value(data, 'headerVisible', true);
        }
      });
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
