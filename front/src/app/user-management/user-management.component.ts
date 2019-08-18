import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpManagerService } from '../http-manager.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {

  form: FormGroup;
  username: String;
  password: String;
  isAdmin: boolean = false;
  displayedColumns: string[] = ['remove', 'username'];

  users: any = [];
  showSpinner: boolean = true;

  constructor(private router: Router, private formBuilder: FormBuilder,
    private httpManager: HttpManagerService, private toastr: ToastrService) {
    this.form = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    })
  }

  async ngOnInit() {
    try {
      const result = await this.httpManager.getRequest("/user");
      this.users = result;
      this.showSpinner = false;
    } catch (err) {
      this.toastr.error(err.error.message);
    }
  }

  async dispatchRegister(values) {
    this.username = values.username;
    this.password = values.password;

    try {
      const result = await this.httpManager.postRequest("/user", { username: this.username, password: this.password, isAdmin: this.isAdmin });
      this.users.push({ id: result["id"], username: result["username"] });
      this.toastr.success("User " + result["username"] + " was created");
      this.users = [...this.users];
      this.form.reset();
    } catch (err) {
      this.toastr.error(err.error.message);
    }
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.form.controls[controlName].hasError(errorName);
  }

  async handleRemove(e) {
    let target = e.target;

    if (e.target.type !== "submit") {
      target = e.target.parentNode;
    }

    const row = target.getAttribute("row");
    const id = target.getAttribute("id");

    try {
      const result = await this.httpManager.deleteRequest("/user/" + id);
      this.toastr.clear();
      this.toastr.success(result["message"]);
      this.users = this.users.filter((user, index) => index.toString() !== row);
    } catch (err) {
      this.toastr.error(err.error.message);
    }
  }
}
