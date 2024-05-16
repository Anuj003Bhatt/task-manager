import { Component, OnDestroy, OnInit } from '@angular/core';
import { TaskService } from '../service/task.service';
import { Task } from '../model/task.model';
import { MatDialog } from '@angular/material/dialog';
import { NewTaskComponent } from '../new-task/new-task.component';
import { Subscription, fromEvent, map, startWith } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ColDef, GridApi, GridReadyEvent, SizeColumnsToContentStrategy, SizeColumnsToFitGridStrategy, SizeColumnsToFitProvidedWidthStrategy } from 'ag-grid-community';
import { ButtonRendererComponent } from '../button-renderer/button-renderer.component';
import { EditTaskComponent } from '../edit-task/edit-task.component';

@Component({
  selector: 'app-task-item-list',
  templateUrl: './task-item-list.component.html',
  styleUrl: './task-item-list.component.css'
})
export class TaskItemListComponent implements OnInit, OnDestroy {
  viewAsTile: boolean = false;
  columns: number=4;
  tasks: Task[] = [];
  updateSubscription: Subscription;
  deleteSubscription: Subscription;
  loaded = false;
  error: string;
  private gridApi!: GridApi<Task>;

  switchView() {
    this.viewAsTile = !this.viewAsTile;
  }

  public autoSizeStrategy:
    | SizeColumnsToFitGridStrategy
    | SizeColumnsToFitProvidedWidthStrategy
    | SizeColumnsToContentStrategy = {
      type: "fitCellContents",
    };

    autoSizeAll(skipHeader: boolean) {
      const allColumnIds: string[] = [];
      this.gridApi.getColumns()!.forEach((column) => {
        allColumnIds.push(column.getId());
      });
      this.gridApi.setDomLayout('autoHeight');
      this.gridApi.autoSizeColumns(allColumnIds, skipHeader);
    }

    onGridReady(params: GridReadyEvent<Task>) {
      this.gridApi = params.api;
      this.autoSizeAll(false);
    }

  colDefs: ColDef[] = [
    { field: "title", suppressSizeToFit: true },
    { field: "description" },
    {
      field: "status", cellRenderer: (params) => {
        switch (params.data.status) {
          case 0: return '<span class="badge bg-info">To Do</span>';
          case 1: return '<span class="badge bg-primary">In Progress</span>';
          case 2: return '<span class="badge bg-success">Complete</span>';
          default: return '<span class="badge bg-danger">Invalid Status</span>';
        }
      }
    },
    {
      field: "priority", cellRenderer: (params) => {
        switch (params.data.status) {
          case 0: return 'Low';
          case 1: return 'Medium';
          case 2: return 'High';
          case 3: return 'Critical';
          case 4: return 'Blocker';
          default: return 'Invalid Status';

        }
      }
    },
    {
      field: "actions",
      cellRenderer: ButtonRendererComponent,
      cellRendererParams: {
        onEdit: this.editTask.bind(this),
        onDelete: this.deleteTask.bind(this),
      }
    }
  ];


  openEditDialog(task: Task): void {
    const dialogRef = this.dialog.open(EditTaskComponent);
    console.log(JSON.stringify(task))
    dialogRef.componentInstance.task = new Task(
      task.id,
      task.title,
      task.description,
      task.status,
      task.priority
    );

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  editTask(event: any) {
    const task = event.data as Task;
    this.openEditDialog(task);
  }

  deleteTask(event: any) {
    const task = event.data as Task;
    this.taskService.deleteTask(task.id);
  }

  constructor(
    private taskService: TaskService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  openSnackBar(message: string) {
    this.snackBar.open(message, null, {
      duration: 2000,
    });
  }

  ngOnInit(): void {
    this.updateSubscription = this.taskService.taskModified.subscribe(() => {
      this.error = null;
      this.tasks = this.taskService.getTasks()
    },

      (error) => {
        this.error = JSON.stringify(error)
      })

    this.deleteSubscription = this.taskService.taskDeleted.subscribe(
      () => {
        this.openSnackBar('Task Deleted Successfully');
        this.tasks = this.taskService.getTasks();
      },
      (error) => { this.openSnackBar(JSON.stringify(error)) }
    )

    fromEvent(window, 'resize').pipe(
      map(event => (event.target as any).innerWidth),
      startWith(window.innerWidth)).subscribe(width => { this.columns = this.getCols(width)}
    );
    this.taskService.fetchTasks();
    this.loaded = true;
  }

  getCols(width: number) {
    if (width <= 800) {
      return 1;
    }

    if (width <= 900) {
      return 2;
    }
    if (width <= 1000) {
      return 3;
    }
    return 4;
  }

  ngOnDestroy(): void {
    this.updateSubscription.unsubscribe();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(NewTaskComponent, {
      data: {},
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  newTaskNavigate() {
    // this.router.navigate(['/newtask'])
    this.openDialog()
  }
}
