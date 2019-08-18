import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HttpManagerService } from '../http-manager.service';

@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.css']
})
export class PanelComponent implements OnInit {

  showUserManagement: boolean = false;

  constructor(private router: Router, private toastr: ToastrService,
    private httpManager: HttpManagerService) { }

  ngOnInit() {
    if (window.localStorage.getItem("isAdmin") === "true") {
      this.showUserManagement = true;
    }
  }

  async handleUpload(event) {
    const file = event.target.files[0];
    
    if (file.type !== "application/vnd.ms-excel" && file.type !== "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
      this.toastr.error("File should be either .xlsx or .xls");
      return;
    }

    try {
      const result = await this.httpManager.postRequest("/documents", { document: file })
      this.toastr.success(result["message"]);
    } catch (err) {
      this.toastr.error(err.error.message);
    } finally {
      event.target.value = "";
    }
  }

  logout() {
    window.localStorage.clear();
    this.router.navigateByUrl("/");
  }
}
