import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { NotFoundComponent } from "./not-found/not-found.component";
import { LoginComponent } from "./login/login.component";
import { NewTaskComponent } from "./new-task/new-task.component";

const appRoutes: Routes = [
    {path: '', component: LoginComponent, pathMatch:"prefix"},
    {path:'login', component: LoginComponent},
    {path:'dashboard', component: HomeComponent},
    {path:'newtask', component: NewTaskComponent},
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