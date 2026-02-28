import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  set token(token: string) {
    localStorage.setItem('token', token);
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

  clearToken() {
    localStorage.removeItem('token');
  }

  isTokenValid() {
    const token = this.token;
    if (!token) {
      return false;
    }
    const decodeToken = this.decodePayload(token);
    if (!decodeToken) {
      return false;
    }
    const expiryTime = decodeToken.exp;
    if (expiryTime) {
      return ((1000 * expiryTime) - (new Date()).getTime()) > 0;
    }
    return false;
  }

  private decodePayload(token: string): any {
    try {
      return JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
    } catch (e) {
      return null;
    }
  }

  get fullName() {
    const token = this.token;
    if (token) {
      const decodedToken = this.decodePayload(token);
      return decodedToken?.fullName || "";
    }
    return "";
  }

  
}
