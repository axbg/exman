import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from 'selenium-webdriver/http';
import { HttpManagerService } from '../http-manager.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private form: FormGroup;
  username: String;
  password: String;

  constructor(private router: Router, private formBuilder: FormBuilder,
    private httpManager: HttpManagerService, private toastr: ToastrService) {
    this.form = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    })
  }

  ngOnInit() {
    if (window.localStorage.getItem("token")) {
      this.router.navigateByUrl("/menu");
    }
  }

  async dispatchLogin(values) {
    this.username = values.username;
    this.password = values.password;

    try {
      const response = await this.httpManager.postRequest("/user/login", { username: this.username, password: this.password });
      window.localStorage.setItem("token", response["token"]);

      if (response["admin"] === true) {
        window.localStorage.setItem("isAdmin", response["admin"]);
      }

      this.router.navigateByUrl("/menu");
    } catch (err) {
      this.toastr.clear();
      this.toastr.error(err.error.message);
    }
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.form.controls[controlName].hasError(errorName);
  }
}
