import { Component } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';


@Component({
  selector: 'app-button-renderer',
  template: `
    <button (click)="onEdit($event)" class="btn btn-primary" mat-button><i class="fa fa-edit"></i></button>
    <button (click)="onDelete($event)" style="margin-left: 15px;" class="btn btn-danger" mat-button><i class="fa fa-trash-o fa-lg"></i></button>
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
