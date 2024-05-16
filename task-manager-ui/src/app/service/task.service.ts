import { EventEmitter, Injectable, OnInit } from "@angular/core";
import { Task } from "../model/task.model";
import { HttpClient } from "@angular/common/http";
import { environment } from "../environment";
import { AuthService } from "./auth.service";

@Injectable({
    providedIn: "root"
}
)
export class TaskService {
    taskModified = new EventEmitter<void>();
    taskDeleted = new EventEmitter<void>();
    taskAdded = new EventEmitter<Task>();
    taskUpdated = new EventEmitter<Task>();

    private tasks: Task[];
    

    private headers = {
        'content-type': 'application/json',
        'Access-Control-Allow-Origin': 'http://localhost:4200'
    };

    constructor(
        private authService: AuthService,
        private httpClient: HttpClient
    ){}

    getTasks(): Task[]{
        return this.tasks.slice();
    }

    fetchTasks(){
        // if (this.authService.getUser() === undefined) {
        //     this.tasks = [ new Task('id','Title', 'Description', 0, 0), new Task('id','Title', 'Description', 0, 0), new Task('id','Title', 'Description', 0, 0) ];
        //     this.taskModified.emit();
        //     return
        // }

        const userId = this.authService.getUser().id;
        this.headers['Authorization'] = `Bearer ${this.authService.getToken()}`
        
        this.httpClient.get(
            `${environment.server_url}/users/${userId}/tasks/list`,
            {
                'headers': this.headers
            }
        ).subscribe({
            next: (response) => {
                this.tasks = response['list'];
                this.taskModified.emit();
            },
            error: (error) => {
                this.taskModified.error(error.error.error);
            }
        });
    }

    addTask(task: Task){
        const userId = this.authService.getUser().id;
        this.headers['Authorization'] = `Bearer ${this.authService.getToken()}`
        this.httpClient.post<Task>(
            `${environment.server_url}/users/${userId}/tasks/add`,
            task,
            {
                'headers': this.headers
            }
        ).subscribe({
            next: (response) => {

                this.tasks.push(response);
                this.taskAdded.emit(response);
                this.taskModified.emit();
            },
            error: (error) => {
                this.taskAdded.error(error.error.error);
                console.log(`Tasks add error : ${JSON.stringify(error)}`)
            }
        });
    }

    editTask(task:Task) {
        const userId = this.authService.getUser().id;
        this.headers['Authorization'] = `Bearer ${this.authService.getToken()}`
        this.httpClient.put<Task>(
            `${environment.server_url}/users/${userId}/tasks/${task.id}`,
            task,
            {
                'headers': this.headers
            }
        ).subscribe({
            next: (response) => {
                this.fetchTasks();
                this.taskUpdated.emit(response);
                this.taskModified.emit();
            },
            error: (error) => {
                this.taskUpdated.error(error.error.error);
                console.log(`Tasks add error : ${JSON.stringify(error)}`)
            }
        });
    }

    deleteTask(id: string){
        this.tasks = this.tasks.filter(f => f.id !== id);
        this.headers['Authorization'] = `Bearer ${this.authService.getToken()}`
        this.httpClient.delete(
            `${environment.server_url}/tasks/${id}`
        ).subscribe({
            next: (response) => {
                this.taskDeleted.emit();
             },
             error: (error) => {
                this.taskDeleted.error(error.error.error);
             }
        })

    }
}