import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Task } from '../model/task.model';
import { MatDialogRef } from '@angular/material/dialog';
import { TaskService } from '../service/task.service';
import { HttpClient } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { Subscription } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-edit-task',
  templateUrl: './edit-task.component.html',
  styleUrl: './edit-task.component.css'
})
export class EditTaskComponent implements OnInit, OnDestroy {
  @Input() task: Task;
  @ViewChild('taskForm') form: NgForm;
  subscription: Subscription

  constructor(
    private dialogRef: MatDialogRef<EditTaskComponent>,
    private taskService: TaskService,
    private snackBar: MatSnackBar
  ) {}

  openSnackBar(message: string) {
    this.snackBar.open(message, null, {
      duration: 2000,
    });
  }

  ngOnInit(): void {
    this.taskService.taskUpdated.subscribe(
      (task) => {
        this.snackBar.open(`Task updated successfully`)
        this.closeDialog();
      },
      (error) => {
        this.snackBar.open(`Error while updating task cmp: ${JSON.stringify(error.error.error)}`)
      }
    )
  }

  ngOnDestroy(): void {
    // this.taskService.taskUpdated.unsubscribe();
  }

  onEditTask() {
    this.taskService.editTask(this.task);
    this.dialogRef.close();
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
