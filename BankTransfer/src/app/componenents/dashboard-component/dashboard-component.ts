// src/app/components/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TransferService, Transfer, DashboardStats } from '../../services/TransferService/transfer-service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  standalone:true
})
export class DashboardComponent implements OnInit {
  stats: DashboardStats = {
    totalBeneficiaries: 0,
    totalTransfers: 0,
    recentTransactions: 0
  };

  recentTransactions: Transfer[] = [];
  loading = false;
  error: string | null = null;

  constructor(
    private transferService: TransferService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;
    this.error = null;

    // Load dashboard stats
    this.transferService.getDashboardStats().subscribe({
      next: (stats) => {
        this.stats = stats;
      },
      error: (err) => {
        console.error('Error loading stats:', err);
        this.error = 'Failed to load dashboard statistics';
        this.loading = false;
      }
    });

    // Load recent transactions
    this.transferService.getRecentTransfers(7).subscribe({
      next: (transactions) => {
        this.recentTransactions = transactions.slice(0, 5);
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading transactions:', err);
        this.error = 'Failed to load recent transactions';
        this.loading = false;
      }
    });
  }

  navigateToBeneficiaries(): void {
    this.router.navigate(['/beneficiaries']);
  }

  navigateToNewTransfer(): void {
    this.router.navigate(['/transfers/new']);
  }

  navigateToTransfers(): void {
    this.router.navigate(['/transfers']);
  }

  getStatusClass(status?: string): string {
    switch (status?.toUpperCase()) {
      case 'COMPLETED':
        return 'bg-green-100 text-green-800';
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800';
      case 'FAILED':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  formatAmount(amount: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  }

  logout(): void {
    // Implement logout logic
    this.router.navigate(['/login']);
  }
}
