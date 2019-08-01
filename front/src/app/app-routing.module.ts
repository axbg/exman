import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PanelComponent } from './panel/panel.component';
import { GridComponent } from './grid/grid.component';
import { LoginComponent } from './login/login.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { AuthGuardService } from './auth-guard.service';
import { AdminGuardService } from './admin-guard.service';

const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'menu', component: PanelComponent, canActivate: [AuthGuardService] },
  { path: 'grid', component: GridComponent, canActivate: [AuthGuardService] },
  { path: 'users', component: UserManagementComponent, canActivate: [AdminGuardService] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
