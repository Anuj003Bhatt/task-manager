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
  statusFilter: number;
  priorityFilter: number;
  columns: number = 4;
  tasks: Task[] = [];
  updateSubscription: Subscription;
  deleteSubscription: Subscription;
  loaded = false;
  error: string;
  searchTerm: string;
  private gridApi!: GridApi<Task>;

  switchView(viewAs: boolean) {
    this.viewAsTile = viewAs;
  }

  public autoSizeStrategy:
    | SizeColumnsToFitGridStrategy
    | SizeColumnsToFitProvidedWidthStrategy
    | SizeColumnsToContentStrategy = {
      type: "fitGridWidth",
    };

  autoSizeAll() {
    const allColumnIds: string[] = [];
    this.gridApi.getColumns()!.forEach((column) => {
      allColumnIds.push(column.getId());
    });

    this.gridApi.setGridOption('domLayout', 'autoHeight');

    this.gridApi.autoSizeAllColumns(false);
  }

  onGridReady(params: GridReadyEvent<Task>) {
    this.gridApi = params.api;
    this.autoSizeAll();
  }

  colDefs: ColDef[] = [
    { field: "title" },
    { field: "description" },
    {
      field: "status", cellRenderer: (params) => {
        switch (params.data.status) {
          case 0: return '<span class="badge bg-info text-dark">To Do</span>';
          case 1: return '<span class="badge bg-primary">In Progress</span>';
          case 2: return '<span class="badge bg-success">Complete</span>';
          default: return '<span class="badge bg-danger">Invalid Status</span>';
        }
      }
    },
    {
      field: "priority", cellRenderer: (params) => {
        switch (params.data.priority) {
          case 0: return '<span class="badge bg-info text-dark">Low</span>';
          case 1: return '<span class="badge bg-light text-dark">Medium</span>';
          case 2: return '<span class="badge bg-primary">High</span>';
          case 3: return '<span class="badge bg-warning text dark">Critical</span>';
          case 4: return '<span class="badge bg-danger">Blocker</span>';
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
        this.filterByPriority = undefined;
        this.filterByStatus = undefined;
        this.taskService.fetchTasks(undefined, undefined);
      },
      (error) => { this.openSnackBar(JSON.stringify(error)) }
    )

    fromEvent(window, 'resize').pipe(
      map(event => (event.target as any).innerWidth),
      startWith(window.innerWidth)).subscribe(width => { this.columns = this.getCols(width) }
      );
    this.taskService.fetchTasks(undefined, undefined);
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
    this.deleteSubscription.unsubscribe();
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
    this.openDialog()
  }

  matchTerms(terms: string[], value: string): boolean {
    for (let term of terms) {
      if (value.toUpperCase().indexOf(term.toUpperCase()) !== -1) {
        return true;
      }
    }
    return false;
  }

  onSearch() {
    const terms: string[] = this.searchTerm.split(' ');
    this.loaded = false;
    this.tasks = this.taskService.getTasks().filter(value => {
      return this.matchTerms(terms, value.title)
        || this.matchTerms(terms, value.description)
    })
    this.loaded = true;
  }

  filterByStatus(status: number) {
    this.statusFilter = status;
    this.taskService.fetchTasks(this.statusFilter, this.priorityFilter);
  }

  filterByPriority(priority: number) {
    this.priorityFilter = priority;
    this.taskService.fetchTasks(this.statusFilter, this.priorityFilter);
  }
}
