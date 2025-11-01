// src/app/componenents/navbar/navbar.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar-component.html',
  styleUrls: ['./navbar-component.css']
})
export class NavbarComponent implements OnInit {
  currentRoute = '/dashboard';
  userName = 'Youssef Ouben Said';
  userEmail = 'john.doe@example.com';
  isMenuOpen = false;
  isMobileMenuOpen = false;

  navItems = [
    {
      label: 'Dashboard',
      route: '/dashboard',
      icon: 'dashboard',
      active: true
    },
    {
      label: 'Beneficiaries',
      route: '/beneficiaries',
      icon: 'group',
      active: false
    },
    {
      label: 'Transfers',
      route: '/transfer',
      icon: 'sync_alt',
      active: false
    },
    {
      label: 'Chatbot',
      route: '/chatbot',
      icon: 'chat',
      active: false
    }
  ];

  constructor(private router: Router) {}

  ngOnInit(): void {
    // Get user info from localStorage
    this.userName = localStorage.getItem('userName') || 'Youssef Ouben Said ';
    this.userEmail = localStorage.getItem('userEmail') || 'john.doe@example.com';

    // Set initial active route
    this.currentRoute = this.router.url;
    this.updateActiveNavItem(this.currentRoute);

    // Listen to route changes
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        this.currentRoute = event.url;
        this.updateActiveNavItem(this.currentRoute);
        this.isMobileMenuOpen = false; // Close mobile menu on navigation
      });
  }

  updateActiveNavItem(route: string): void {
    this.navItems.forEach(item => {
      item.active = item.route === route;
    });
  }

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  logout(): void {
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userName');
    this.router.navigate(['/signin']);
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }
}
