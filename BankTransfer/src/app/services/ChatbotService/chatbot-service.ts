// src/app/services/chatbot.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Message {
  id: string;
  content: string;
  sender: 'user' | 'bot';
  timestamp: Date;
}

export interface ChatResponse {
  message: string;
  timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private apiUrl = `${environment.apiUrl}/chatbot-service/api/chat`;
  private messagesSubject = new BehaviorSubject<Message[]>([
    {
      id: '1',
      content: 'Hello! How can I help you with your banking today?',
      sender: 'bot',
      timestamp: new Date()
    }
  ]);

  messages$ = this.messagesSubject.asObservable();

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

    // Send to backend
    return this.http.post<ChatResponse>(this.apiUrl, { message: content });
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
  }

  getMessages(): Message[] {
    return this.messagesSubject.value;
  }
}
