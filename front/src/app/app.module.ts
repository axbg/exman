import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ErrorHandler } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppContainerComponent } from './app-container/app-container.component';
import { PanelComponent } from './panel/panel.component';
import { GridComponent } from './grid/grid.component';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { UserManagementComponent } from './user-management/user-management.component';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { ErrorHandlerService } from './error-handler.service';
import { ToastrModule } from 'ngx-toastr';
import { LoadingBarModule } from '@ngx-loading-bar/core';
import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {HotkeyModule} from 'angular2-hotkeys';

@NgModule({
  declarations: [
    AppComponent,
    AppContainerComponent,
    PanelComponent,
    GridComponent,
    LoginComponent,
    UserManagementComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatIconModule,
    HttpClientModule,
    NgxDatatableModule,
    MatSelectModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatTableModule,
    MatSlideToggleModule,
    ToastrModule.forRoot(),
    LoadingBarModule, 
    LoadingBarHttpClientModule,
    LoadingBarRouterModule,
    MatProgressSpinnerModule,
    HotkeyModule.forRoot()
  ],
  providers: [
    {
      provide: ErrorHandler, 
      useClass: ErrorHandlerService
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
