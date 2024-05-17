import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { TaskItemComponent } from './task-item/task-item.component';
import { TaskItemListComponent } from './task-item-list/task-item-list.component';
import { HttpClientModule } from '@angular/common/http';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCardModule } from '@angular/material/card';
import { HomeComponent } from './home/home.component';
import { AppRoutingModule } from './app-routing.module';
import { NotFoundComponent } from './not-found/not-found.component';
import { LoginComponent } from './login-container/login/login.component';
import { FormsModule } from '@angular/forms';
import { NewTaskComponent } from './new-task/new-task.component';
import { SignupComponent } from './login-container/signup/signup.component';
import { LoginContainerComponent } from './login-container/login-container.component';
import { ResetPasswordComponent } from './login-container/reset-password/reset-password.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { MatDialogModule } from "@angular/material/dialog";
import { AgGridModule } from 'ag-grid-angular';
import { ButtonRendererComponent } from './button-renderer/button-renderer.component';
import { EditTaskComponent } from './edit-task/edit-task.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    TaskItemComponent,
    TaskItemListComponent,
    HomeComponent,
    NotFoundComponent,
    LoginComponent,
    NewTaskComponent,
    SignupComponent,
    LoginContainerComponent,
    ResetPasswordComponent,
    ButtonRendererComponent,
    EditTaskComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    MatGridListModule,
    MatCardModule,
    AppRoutingModule,
    MatDialogModule,
    AgGridModule
  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
