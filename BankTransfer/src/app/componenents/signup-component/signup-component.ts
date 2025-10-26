// src/app/componenents/signup-component/signup-component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './signup-component.html',
  styleUrls: ['./signup-component.css']
})
export class SignUpComponent {
  fullName = '';
  email = '';
  password = '';
  confirmPassword = '';
  agreeTerms = false;
  loading = false;
  error = '';
  showPassword = false;
  showConfirmPassword = false;

  constructor(private router: Router) {}

  onSubmit(): void {
    this.error = '';

    // Validation
    if (!this.fullName || !this.email || !this.password || !this.confirmPassword) {
      this.error = 'Please fill in all fields';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.error = 'Passwords do not match';
      return;
    }

    if (this.password.length < 6) {
      this.error = 'Password must be at least 6 characters';
      return;
    }

    if (!this.agreeTerms) {
      this.error = 'Please agree to the terms and conditions';
      return;
    }

    this.loading = true;

    // Simulate API call
    setTimeout(() => {
      // In production, call your registration service here
      localStorage.setItem('isAuthenticated', 'true');
      localStorage.setItem('userEmail', this.email);
      localStorage.setItem('userName', this.fullName);
      this.router.navigate(['/dashboard']);
    }, 1500);
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  toggleConfirmPasswordVisibility(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  navigateToSignIn(): void {
    this.router.navigate(['/signin']);
  }
}
