import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { TaskService } from '../service/task.service';
import { Task } from '../model/task.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-new-task',
  templateUrl: './new-task.component.html',
  styleUrl: './new-task.component.css'
})
export class NewTaskComponent implements OnInit, OnDestroy {
  @ViewChild('taskForm') form: NgForm;
  subscription: Subscription;
  taskTitle:string;
  taskDescription:string;
  taskStatus:number;
  taskPriority:number;


  constructor(
    private dialogRef: MatDialogRef<NewTaskComponent>,
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    this.subscription = this.taskService.taskUpdated.subscribe(
      (task) => {
        this.closeDialog();
      },
      (error) => {
        console.log(`Error in new task cmp: ${JSON.stringify(error)}`)
      }
    )    
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  onCreateTask() {
    const newTask = new Task(null, this.taskTitle, this.taskDescription, this.taskStatus, this.taskPriority);
    this.taskService.addTask(newTask);
    this.dialogRef.close();
  }

  closeDialog() {
    console.log(`Close requested`)
    this.dialogRef.close();
  }
}
