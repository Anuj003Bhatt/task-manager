import { Component, OnInit } from '@angular/core';
import { TaskService } from '../service/task.service';
import { Task } from '../model/task.model';
import { Router } from '@angular/router';
@Component({
  selector: 'app-task-item-list',
  templateUrl: './task-item-list.component.html',
  styleUrl: './task-item-list.component.css'
})
export class TaskItemListComponent implements OnInit {
  tasks: Task[];

  constructor(
    private router: Router,
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    this.tasks = this.taskService.getTasks();
  }

  newTaskNavigate() {
    this.router.navigate(['/newtask'])
  }
}
