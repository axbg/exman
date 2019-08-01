import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router/src/utils/preactivation';
import { Router, ActivatedRouteSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {
  path: ActivatedRouteSnapshot[];
  route: ActivatedRouteSnapshot;

  constructor(private router: Router) {

  }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (window.localStorage.getItem("token")) {
      return true
    }
    
    this.router.navigateByUrl("");
    return false;
  }
}
