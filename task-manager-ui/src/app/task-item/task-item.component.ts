import { Component, Input } from '@angular/core';
import { Task } from '../model/task.model';

@Component({
  selector: 'app-task-item',
  templateUrl: './task-item.component.html',
  styleUrl: './task-item.component.css'
})
export class TaskItemComponent {
  @Input({required: true}) task: Task;
}
