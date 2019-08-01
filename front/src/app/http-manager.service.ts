import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Constants } from './Constants';

@Injectable({
  providedIn: 'root'
})
export class HttpManagerService {

  constructor(private httpClient: HttpClient) { }

  createHeaders(customHeaders?) {
    return new HttpHeaders({ Authorization: window.localStorage.getItem("token"), ...customHeaders });
  }

  async getRequest(url: String) {
    return this.httpClient.get(Constants.API_ENDPOINT + url, { headers: this.createHeaders() })
      .toPromise();
  }

  async postRequest(url: String, params: Object) {
    const formData = new FormData();
    Object.keys(params).map(param => formData.append(param, params[param]))

    return this.httpClient.post(Constants.API_ENDPOINT + url, formData, { headers: this.createHeaders({ 'enctype': 'multipart/form-data' }) })
      .toPromise();
  }

  async putRequest(url: String, params: Object) {

  }

  async deleteRequest(url: String) {

  }
}
