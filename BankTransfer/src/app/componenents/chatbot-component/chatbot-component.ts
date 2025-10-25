// src/app/componenents/chatbot-component/chatbot-component.ts
import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ChatbotService, Message } from '../../services/ChatbotService/chatbot-service';

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './chatbot-component.html',
  styleUrls: ['./chatbot-component.css']
})
export class ChatbotComponent implements OnInit, AfterViewChecked {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  messages: Message[] = [];
  userMessage = '';
  isTyping = false;
  error: string | null = null;

  constructor(private chatbotService: ChatbotService) { }

  ngOnInit(): void {
    this.chatbotService.messages$.subscribe(messages => {
      this.messages = messages;
    });
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  sendMessage(): void {
    if (!this.userMessage.trim() || this.isTyping) {
      return;
    }

    const message = this.userMessage;
    this.userMessage = '';
    this.isTyping = true;
    this.error = null;

    this.chatbotService.sendMessage(message).subscribe({
      next: (response) => {
        this.chatbotService.addBotMessage(response.message);
        this.isTyping = false;
      },
      error: (err) => {
        console.error('Error sending message:', err);
        this.error = 'Failed to send message. Please try again.';
        this.chatbotService.addBotMessage('Sorry, I encountered an error. Please try again.');
        this.isTyping = false;
      }
    });
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
        this.messagesContainer.nativeElement.scrollTop =
          this.messagesContainer.nativeElement.scrollHeight;
      }
    } catch (err) {
      console.error('Error scrolling:', err);
    }
  }

  clearChat(): void {
    if (confirm('Are you sure you want to clear the chat history?')) {
      this.chatbotService.clearMessages();
    }
  }

  logout(): void {
    // Implement logout logic
    window.location.href = '/login';
  }
}
