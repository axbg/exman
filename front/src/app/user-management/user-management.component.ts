import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {

  private form: FormGroup;
  username: String;
  password: String;
  isAdmin: boolean = false;
  displayedColumns: string[] = ['remove', 'username'];

  users: any[] = [{
    id: 1,
    username: "alexb"
  },
  {
    id: 2,
    username: 'abcdefg'
  },
  {
    id: 2,
    username: 'abcdefg'
  }
  ,
  {
    id: 2,
    username: 'abcdefg'
  }
  ,
  {
    id: 2,
    username: 'abcdefg'
  }
  ,
  {
    id: 2,
    username: 'abcdefg'
  }
  ,
  {
    id: 2,
    username: 'abcdefg'
  }
  ,
  {
    id: 2,
    username: 'abcdefg'
  }
  ,
  {
    id: 2,
    username: 'abcdefg'
  },
  {
    id: 2,
    username: 'abcdefg'
  },
  {
    id: 2,
    username: 'abcdefg'
  },
  {
    id: 2,
    username: 'abcdefg'
  }];

  constructor(private router: Router, private formBuilder: FormBuilder) {
    this.form = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required])
    })
  }

  ngOnInit() {
  }

  dispatchRegister(values) {
    this.username = values.username;
    this.password = values.password;
    console.log(this.isAdmin);
  }

  public hasError = (controlName: string, errorName: string) => {
    return this.form.controls[controlName].hasError(errorName);
  }

  handleRemove(e) {
    let target = e.target;

    if (e.target.type !== "submit") {
      target = e.target.parentNode;
    }

    console.log(target.getAttribute("id"));
  }
}
