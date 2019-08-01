import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router/src/utils/preactivation';
import { Router, ActivatedRouteSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AdminGuardService implements CanActivate {
  path: ActivatedRouteSnapshot[];
  route: ActivatedRouteSnapshot;

  constructor(private router: Router) {

  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (window.localStorage.getItem("isAdmin") === "true") {
      return true
    }

    this.router.navigateByUrl("");
    return false;
  }
}