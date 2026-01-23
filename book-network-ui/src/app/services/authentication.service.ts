import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthenticationRequest, AuthenticationResponse, RegistrationRequest } from './models';
import { Observable } from 'rxjs';

const BASE_URL = 'http://localhost:8081/api/v1/auth';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {


  constructor(private http: HttpClient) { }

  authenticate(params: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${BASE_URL}/authenticate`, params);
  }

  register(registerRequest: RegistrationRequest): Observable<void> {
    return this.http.post<void>(`${BASE_URL}/register`, registerRequest);
  }

  confirm(token: string): Observable<void> {
    return this.http.get<void>(`${BASE_URL}/activate-account?token=${token}`);
  }
}
