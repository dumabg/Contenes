// Based on https://netbasal.com/diy-keyboard-shortcuts-in-your-angular-application-4704734547a2

import { Injectable, Inject } from '@angular/core';
import { EventManager } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { DOCUMENT } from '@angular/common';
import { MatDialog } from '@angular/material';
import { HotkeysDialog } from './hotkeys.dialog/hotkeys.dialog.component';

interface Options {
  element: any;
  description: string | undefined;
  keys: string;
}

@Injectable({
  providedIn: 'root'
})
export class Hotkeys {
  hotkeys = new Map<string, string>();
  defaults: Partial<Options> = {
    element: this.document
  };

  constructor(private eventManager: EventManager,
              private dialog: MatDialog,
              @Inject(DOCUMENT) private document: Document) {
      this.addShortcut({ keys: 'shift.?' }).subscribe(() => {
        this.openHelpModal();
      });
  }

  addShortcut(options: Partial<Options>): Observable<void> {
    const merged = { ...this.defaults, ...options };
    const event = `keydown.${merged.keys}`;

    if (merged.description !== undefined) {
      this.hotkeys.set(merged.keys, merged.description);
    }

    return new Observable(observer => {
      const handler = (e: KeyboardEvent) => {
        this.dialog.closeAll();
        e.preventDefault();
        observer.next();
      };

      const dispose = this.eventManager.addEventListener(merged.element, event, handler);
      // return () => {
      //   dispose();
      //   this.hotkeys.delete(merged.keys);
      // };
      return {unsubscribe() {
        dispose();
        this.hotkeys.delete(merged.keys);
      }};
    });
  }

  openHelpModal() {
    this.dialog.open(HotkeysDialog, {
      width: '500px',
      data: this.hotkeys
    });
  }

}
