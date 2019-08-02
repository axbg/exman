import { Injectable, ErrorHandler } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler{

  constructor(private toastr: ToastrService) { }
    
  handleError(error: any): void {
    console.log(error);
  }

}
