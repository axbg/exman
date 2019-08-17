import { Injectable } from '@angular/core';

enum maxLengths {
  account = 8
}

@Injectable({
  providedIn: 'root'
})
export class InputValidatorService {

  private isDigit(keyCode) {
    return keyCode === 8 || keyCode === 9 || (keyCode >= 48 && keyCode <= 57);
  }

  private isFloatDigit(keyCode, value) {
    return this.isDigit(keyCode) || keyCode === 190 && !value.includes('.');
  }

  public isEmpty(value) {
    return value === null || value.trim().length === 0 || value.length === 0;
  }

  public hasMoreThanRequiredLength(value, keyCode, cell) {
    return cell === "account" ?
      keyCode === 8 ? value.length - 1 > maxLengths.account : value.length + 1 > maxLengths.account
      : false;
  }

  public hasRequiredLength(value, keyCode, cell) {
    return cell === "account" ?
      keyCode === 8 ? value.length - 1 === maxLengths.account : value.length + 1 === maxLengths.account
      : true;
  }

  public validateFormat(keyCode, cell, value) {
    switch (cell) {
      case "account":
      case "unit":
        return this.isDigit(keyCode) ? true : false;
      case "amount":
        return this.isFloatDigit(keyCode, value) ? true : false;
      default:
        return true;
    }
  }

}
