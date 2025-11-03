// src/app/components/chatbot-component/chatbot-component.ts
import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ChatbotService, Message } from '../../services/ChatbotService/chatbot-service';
import { NavbarComponent } from '../navbar-component/navbar-component';

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, NavbarComponent],
  templateUrl: './chatbot-component.html',
  styleUrls: ['./chatbot-component.css'],
})
export class ChatbotComponent implements OnInit, AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  messages: Message[] = [];
  userMessage = '';
  isTyping = false;
  error: string | null = null;
  private shouldScroll = false;

  constructor(private chatbotService: ChatbotService) { }

  ngOnInit(): void {
    this.chatbotService.messages$.subscribe(messages => {
      this.messages = messages;
      this.shouldScroll = true;
    });
  }

  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  sendMessage(): void {
    if (!this.userMessage.trim() || this.isTyping) {
      return;
    }

    const message = this.userMessage.trim();
    this.userMessage = '';
    this.isTyping = true;
    this.error = null;

    this.chatbotService.sendMessage(message).subscribe({
      next: (response) => {

        this.isTyping = false;
      },
      error: (err) => {
        console.error('Error sending message:', err);

        // More detailed error handling
        if (err.status === 0) {
          this.error = 'Unable to connect to the server. Please check your connection.';
        } else if (err.status === 404) {
          this.error = 'Chatbot service not found. Please contact support.';
        } else if (err.status === 500) {
          this.error = 'Server error. Please try again later.';
        } else {
          this.error = err.error?.message || 'Failed to send message. Please try again.';
        }

        this.chatbotService.addBotMessage(
          'Sorry, I encountered an error processing your request. Please try again.'
        );
        this.isTyping = false;
      }
    });
  }

  sendQuickMessage(message: string): void {
    this.userMessage = message;
    this.sendMessage();
  }

  onKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  private scrollToBottom(): void {
    try {
      if (this.messagesContainer) {
        const element = this.messagesContainer.nativeElement;
        element.scrollTop = element.scrollHeight;
      }
    } catch (err) {
      console.error('Error scrolling:', err);
    }
  }

  clearChat(): void {
    if (this.messages.length <= 1) { // Only initial greeting message
      return;
    }

    if (confirm('Are you sure you want to clear the chat history?')) {
      this.chatbotService.clearMessages();
      this.error = null;
      this.userMessage = '';
      this.isTyping = false;
    }
  }

  // Optional: Method to get current session ID
  getSessionId(): string | null {
    return this.chatbotService.getSessionId();
  }

  // Optional: Method to view chat history
  viewHistory(): void {
    const sessionId = this.chatbotService.getSessionId();
    if (sessionId) {
      this.chatbotService.getChatHistory(sessionId).subscribe({
        next: (history) => {
          console.log('Chat history:', history);
          // You can display this in a modal or separate component
        },
        error: (err) => {
          console.error('Error fetching history:', err);
          this.error = 'Failed to load chat history.';
        }
      });
    }
  }

  logout(): void {
    if (confirm('Are you sure you want to logout?')) {
      // Clear chat before logout
      this.chatbotService.clearMessages();

      // Clear authentication
      localStorage.removeItem('isAuthenticated');
      localStorage.removeItem('userEmail');
      localStorage.removeItem('userName');

      // Redirect to signin
      window.location.href = '/signin';
    }
  }
}
