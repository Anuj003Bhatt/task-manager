import { Component } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';


@Component({
  selector: 'app-button-renderer',
  template: `
  <div class="m-1 responsive-grid">
    <button (click)="onEdit($event)" class="small btn btn-primary"><i class="fa fa-edit"></i></button>
    <button (click)="onDelete($event)" class="small ml-1 btn btn-danger"><i class="fa fa-trash-o fa-lg"></i></button>
  </div>
    `
})

export class ButtonRendererComponent implements ICellRendererAngularComp {

  params: any;
  label: string;

  agInit(params): void {
    this.params = params;
    this.label = this.params.label || null;
  }

  refresh(params?: any): boolean {
    return true;
  }

  onEdit(event: any) {
    if (this.params.onEdit instanceof Function) {
      const params = {
        event: event,
        rowData: this.params.node.data
      }
      this.params.onEdit(this.params);
    }
  }

  onDelete(event: any) {
    if (this.params.onDelete instanceof Function) {
      const params = {
        event: event,
        rowData: this.params.node.data
      }
      this.params.onDelete(this.params);
    }
  }
}
