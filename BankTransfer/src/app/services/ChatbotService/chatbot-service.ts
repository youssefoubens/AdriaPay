// src/app/services/chatbot.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface Message {
  id: string;
  content: string;
  sender: 'user' | 'bot';
  timestamp: Date;
}

export interface ChatRequest {
  message: string;
  sessionId?: string;
}

export interface ChatResponse {
  response: string;
  sessionId: string;
  timestamp: string;
  sources?: string[];
}

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private apiUrl = `${environment.apiUrl}/api/chatbot`;
  private messagesSubject = new BehaviorSubject<Message[]>([
    {
      id: '1',
      content: 'Hello! How can I help you with your banking today?',
      sender: 'bot',
      timestamp: new Date()
    }
  ]);

  messages$ = this.messagesSubject.asObservable();
  private currentSessionId: string | null = null;

  constructor(private http: HttpClient) { }

  sendMessage(content: string): Observable<ChatResponse> {
    // Add user message to local state
    const userMessage: Message = {
      id: Date.now().toString(),
      content,
      sender: 'user',
      timestamp: new Date()
    };

    const currentMessages = this.messagesSubject.value;
    this.messagesSubject.next([...currentMessages, userMessage]);

    // Prepare request payload
    const requestBody: ChatRequest = {
      message: content
    };

    // Add sessionId if exists
    if (this.currentSessionId) {
      requestBody.sessionId = this.currentSessionId;
    }

    // Send to backend - CORRECTED ENDPOINT
    return this.http.post<ChatResponse>(`${this.apiUrl}/chat`, requestBody).pipe(
      tap(response => {
        // Store session ID for future requests
        if (response.sessionId) {
          this.currentSessionId = response.sessionId;
        }

        // Add bot response to messages
        this.addBotMessage(response.response);
      })
    );
  }

  addBotMessage(content: string): void {
    const botMessage: Message = {
      id: Date.now().toString(),
      content,
      sender: 'bot',
      timestamp: new Date()
    };

    const currentMessages = this.messagesSubject.value;
    this.messagesSubject.next([...currentMessages, botMessage]);
  }

  clearMessages(): void {
    this.messagesSubject.next([{
      id: '1',
      content: 'Hello! How can I help you with your banking today?',
      sender: 'bot',
      timestamp: new Date()
    }]);
    this.currentSessionId = null;
  }

  getMessages(): Message[] {
    return this.messagesSubject.value;
  }

  getSessionId(): string | null {
    return this.currentSessionId;
  }

  // Additional methods to match your controller endpoints

  uploadDocument(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post(`${this.apiUrl}/documents`, formData);
  }

  getAllDocuments(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/documents`);
  }

  getChatHistory(sessionId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/history/${sessionId}`);
  }

  getStats(): Observable<any> {
    return this.http.get(`${this.apiUrl}/stats`);
  }
}
