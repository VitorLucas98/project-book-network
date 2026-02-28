import { Injectable } from '@angular/core';
import { FeedbackRequest } from './models';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

const BASE_URL = 'http://localhost:8081/api/v1/feedbacks';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {

  constructor(private http: HttpClient) { }

  saveFeedback(feedbackRequest: FeedbackRequest): Observable<void> {
    return this.http.post<void>(`${BASE_URL}`, feedbackRequest);
  }


}
