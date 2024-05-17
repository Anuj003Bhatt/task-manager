import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Task } from '../model/task.model';
import { HttpClient } from '@angular/common/http';
import { TaskService } from '../service/task.service';
import { Subscription } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { EditTaskComponent } from '../edit-task/edit-task.component';

@Component({
  selector: 'app-task-item',
  templateUrl: './task-item.component.html',
  styleUrl: './task-item.component.css'
})
export class TaskItemComponent {
  @Input({ required: true }) task: Task;
  subscription: Subscription;

  constructor(
    private taskService: TaskService,
    private dialog: MatDialog,
  ) { }

  openDialog(): void {
    const dialogRef = this.dialog.open(EditTaskComponent);
    console.log(JSON.stringify(this.task))
    dialogRef.componentInstance.task = new Task(
      this.task.id,
      this.task.title,
      this.task.description,
      this.task.status,
      this.task.priority
    );

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  editTask() {
    this.openDialog();
  }

  deleteTask() {
    this.taskService.deleteTask(this.task.id);
  }
}
