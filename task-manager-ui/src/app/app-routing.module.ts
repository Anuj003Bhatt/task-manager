import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { NotFoundComponent } from "./not-found/not-found.component";
import { LoginComponent } from "./login-container/login/login.component";
import { NewTaskComponent } from "./new-task/new-task.component";
import { canActivate } from "./service/auth-guard.service";
import { LoginContainerComponent } from "./login-container/login-container.component";
import { SignupComponent } from "./login-container/signup/signup.component";
import { ResetPasswordComponent } from "./login-container/reset-password/reset-password.component";

const appRoutes: Routes = [
    {path: '', component: LoginContainerComponent, pathMatch:"prefix", children: [
        {path:'', component: LoginComponent},
        {path:'login', component: LoginComponent},
        {path:'signup', component: SignupComponent},
        {path:'reset', component: ResetPasswordComponent},
    ]},
    {path:'dashboard', component: HomeComponent, canActivate: [canActivate]},
    {path:'newtask', component: NewTaskComponent, canActivate: [canActivate]},
    {path:'**', component: NotFoundComponent}
]

@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [
        RouterModule
    ]
})
export class AppRoutingModule{

}