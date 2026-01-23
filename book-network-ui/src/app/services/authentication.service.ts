import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthenticationRequest, AuthenticationResponse, RegistrationRequest } from './models';
import { Observable } from 'rxjs';

const BASE_URL = 'http://localhost:8081/api/v1';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {


  constructor(private http: HttpClient) { }

  authenticate(params: AuthenticationRequest): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${BASE_URL}/auth/authenticate`, params);
  }

  register(registerRequest: RegistrationRequest): Observable<void> {
    return this.http.post<void>(`${BASE_URL}/auth/register`, registerRequest);
  }
}
