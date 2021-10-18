import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QRsComponent } from './qrs.component';

describe('QrsComponent', () => {
  let component: QRsComponent;
  let fixture: ComponentFixture<QRsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QRsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QRsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
