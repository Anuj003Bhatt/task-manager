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
    taskUpdated = new EventEmitter<Task>();

    private tasks: Task[];


    private headers = {
        'content-type': 'application/json',
        'Access-Control-Allow-Origin': 'http://localhost:4200'
    };

    constructor(
        private authService: AuthService,
        private httpClient: HttpClient
    ) { }

    getTasks(): Task[] {
        return this.tasks.slice();
    }

    fetchTasks(status: number, priority: number) {
        let params = '';
        if (status !== undefined) {
            params = `?status=${status}`
        }

        if (priority !== undefined) {
            if (status === undefined) {
                params = `?priority=${priority}`
            } else {
                params = params + `&priority=${priority}`
            }
        }

        this.headers['Authorization'] = `Bearer ${this.authService.getToken()}`

        this.httpClient.get(
            `${environment.server_url}/tasks/page${params}`,
            {
                'headers': this.headers
            }
        ).subscribe({
            next: (response) => {
                this.tasks = response['list'];
                this.taskModified.emit();
            },
            error: (error) => {
                if (error.error) {
                    this.taskModified.error(error.error.error);
                }
            }
        });
    }

    addTask(task: Task) {
        this.headers['Authorization'] = `Bearer ${this.authService.getToken()}`
        this.httpClient.post<Task>(
            `${environment.server_url}/tasks/add`,
            task,
            {
                'headers': this.headers
            }
        ).subscribe({
            next: (response) => {
                this.fetchTasks(undefined, undefined);
                this.taskUpdated.emit(response);
            },
            error: (error) => {
                this.taskUpdated.error(error.error.error);
                console.log(`Tasks add error : ${JSON.stringify(error)}`)
            }
        });
    }

    editTask(task: Task) {
        this.headers['Authorization'] = `Bearer ${this.authService.getToken()}`
        this.httpClient.put<Task>(
            `${environment.server_url}/tasks/${task.id}`,
            task,
            {
                'headers': this.headers
            }
        ).subscribe({
            next: (response) => {
                this.fetchTasks(undefined, undefined);
                this.taskUpdated.emit(response);
            },
            error: (error) => {
                this.taskUpdated.error(error.error.error);
                console.log(`Tasks add error : ${JSON.stringify(error)}`)
            }
        });
    }

    deleteTask(id: string) {
        this.headers['Authorization'] = `Bearer ${this.authService.getToken()}`
        this.httpClient.delete(
            `${environment.server_url}/tasks/${id}`, {
            'headers': this.headers
        }
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