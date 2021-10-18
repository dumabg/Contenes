import { Component, OnInit, ChangeDetectionStrategy, ViewChild, ContentChild, ElementRef, ViewChildren, QueryList } from '@angular/core';
import * as JSZip from 'jszip';
import * as FileSaver from 'file-saver';
import {QrCode, Ecc} from './qrcodegen';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { CodeApi } from '../../api/code.api';
import { LoadingComponent } from 'src/app/modules/loading/loading.component';
import { element } from 'protractor';

// https://www.npmjs.com/package/ngx-print
// https://stuk.github.io/jszip/

@Component({
  selector: 'app-qrs',
  templateUrl: './qrs.component.html',
  styleUrls: ['./qrs.component.scss']
})
export class QRsComponent implements OnInit {

  formGroup: FormGroup;
  counter = Array;

  @ViewChild(LoadingComponent, {static: true}) loadingComponent: LoadingComponent;
  @ViewChild('qrConfiguration', {static: false}) set qrConfiguration2(el: ElementRef) {
    if (el !== undefined) {
      this.createQr(el, 'Configuration QR code');
    }
  }
  @ViewChildren('qrPrint') set qrsPrint(qrs: QueryList<any>) {
    if (qrs.length > 0) {
      this.codeApi.getCodes(qrs.length).subscribe((codes: string[]) => {
        let i = 0;
        qrs.forEach(el => {
          this.createQr(el, codes[i]);
          i++;
        });
      });
    }
  }

  constructor(private formBuilder: FormBuilder,
              private codeApi: CodeApi) { }

  private createQr(el: ElementRef, code: string) {
    const qr = QrCode.encodeText(code, Ecc.HIGH);
    const svg = qr.toSvgString(0);
    el.nativeElement.innerHTML = svg.substring(svg.indexOf('<svg'));
  }

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
       numQrs: [1, [Validators.required, Validators.max(100), Validators.min(1)]],
       outputType: ['1' , Validators.required],
       fileName: ['QRCodes.zip', Validators.required],
       printConfigureSize: ['3.5', Validators.required]
    });
    this.codeApi.loadingComponent = this.loadingComponent;
  }

  save(): void {
    const num = this.formGroup.controls.numQrs.value;
    this.codeApi.getCodes(num).subscribe((codes: string[]) => {
      const zip = new JSZip();
      const prefix = 'QRCode_';
      const sufix = '.svg';
      for (let i = 1; i <= num; i++) {
        const qr = QrCode.encodeText(codes[i - 1], Ecc.HIGH);
        const svg = qr.toSvgString(0);
        const name = prefix + i + sufix;
        zip.file(name, svg);
      }
      zip.generateAsync({type: 'blob'})
        .then(content => {
          FileSaver.saveAs(content, this.formGroup.controls.fileName.value);
        });
    });
   }
}
