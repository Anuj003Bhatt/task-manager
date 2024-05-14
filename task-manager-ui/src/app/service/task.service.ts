import { EventEmitter, Injectable, OnInit } from "@angular/core";
import { Task } from "../model/task.model";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: "root"
}
)
export class TaskService {
    themeModify = new EventEmitter<string>();

    private tasks: Task[] = [
        new Task('title', 'This is a sample description of a task to see how the card scales, This is a sample description of a task to see how the card scales','TODO', 3),
        new Task('title', 'This is a sample description of a task to see how the card scales, This is a sample description of a task to see how the card scales','INPROGRESS', 3),
        new Task('title', 'This is a sample description of a task to see how the card scales, This is a sample description of a task to see how the card scales','DONE', 3),
    ];

    constructor(
        private httpClient: HttpClient
    ){}

    getTasks(): Task[]{
        return this.tasks.slice();
    }

    addTasks(task: Task){
        this.tasks.push(task);
    }

    deleteTasks(index: number){
        if(index > -1){
            this.tasks.splice(index, 1);
        }
    }
}