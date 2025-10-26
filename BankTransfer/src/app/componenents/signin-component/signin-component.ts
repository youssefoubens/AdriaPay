// src/app/componenents/signin-component/signin-component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './signin-component.html',
  styleUrls: ['./signin-component.css']
})
export class SignInComponent {
  email = '';
  password = '';
  rememberMe = false;
  loading = false;
  error = '';
  showPassword = false;

  constructor(private router: Router) {}

  onSubmit(): void {
    if (!this.email || !this.password) {
      this.error = 'Please fill in all fields';
      return;
    }

    this.loading = true;
    this.error = '';

    // Simulate API call
    setTimeout(() => {
      // For demo purposes, accept any credentials
      // In production, call your authentication service here
      if (this.email && this.password) {
        localStorage.setItem('isAuthenticated', 'true');
        localStorage.setItem('userEmail', this.email);
        this.router.navigate(['/dashboard']);
      } else {
        this.error = 'Invalid credentials';
        this.loading = false;
      }
    }, 1000);
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  navigateToSignUp(): void {
    this.router.navigate(['/signup']);
  }
}
