// src/app/components/transfer/transfer.component.ts
import { Component, OnInit } from '@angular/core';
import { TransferService, Transfer } from '../../services/TransferService/transfer-service';
import { BeneficiaryService, Beneficiary } from '../../services/BeneficiaryService/beneficiary-service';

@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.css']
})
export class TransferComponent implements OnInit {
  transfers: Transfer[] = [];
  filteredTransfers: Transfer[] = [];
  beneficiaries: Beneficiary[] = [];
  searchQuery = '';
  loading = false;
  error: string | null = null;

  showModal = false;
  isEditMode = false;
  currentTransfer: Transfer = this.getEmptyTransfer();

  currentPage = 1;
  totalPages = 10;

  constructor(
    private transferService: TransferService,
    private beneficiaryService: BeneficiaryService
  ) { }

  ngOnInit(): void {
    this.loadTransfers();
    this.loadBeneficiaries();
  }

  loadTransfers(): void {
    this.loading = true;
    this.error = null;

    this.transferService.getAllTransfers().subscribe({
      next: (data) => {
        this.transfers = data;
        this.filteredTransfers = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading transfers:', err);
        this.error = 'Failed to load transfers';
        this.loading = false;
      }
    });
  }

  loadBeneficiaries(): void {
    this.beneficiaryService.getAllBeneficiaries().subscribe({
      next: (data) => {
        this.beneficiaries = data;
      },
      error: (err) => {
        console.error('Error loading beneficiaries:', err);
      }
    });
  }

  searchTransfers(): void {
    if (!this.searchQuery.trim()) {
      this.filteredTransfers = this.transfers;
      return;
    }

    const query = this.searchQuery.toLowerCase();
    this.filteredTransfers = this.transfers.filter(t =>
      t.beneficiaryName?.toLowerCase().includes(query) ||
      t.sourceRib.toLowerCase().includes(query) ||
      t.description.toLowerCase().includes(query) ||
      t.amount.toString().includes(query)
    );
  }

  openAddModal(): void {
    this.isEditMode = false;
    this.currentTransfer = this.getEmptyTransfer();
    this.showModal = true;
  }

  openEditModal(transfer: Transfer): void {
    this.isEditMode = true;
    this.currentTransfer = { ...transfer };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.currentTransfer = this.getEmptyTransfer();
  }

  saveTransfer(): void {
    // Find beneficiary name
    const beneficiary = this.beneficiaries.find(b => b.id === this.currentTransfer.beneficiaryId);
    if (beneficiary) {
      this.currentTransfer.beneficiaryName = `${beneficiary.firstName} ${beneficiary.lastName}`;
    }

    if (this.isEditMode && this.currentTransfer.id) {
      this.transferService.updateTransfer(
        this.currentTransfer.id,
        this.currentTransfer
      ).subscribe({
        next: () => {
          this.loadTransfers();
          this.closeModal();
        },
        error: (err) => {
          console.error('Error updating transfer:', err);
          this.error = 'Failed to update transfer';
        }
      });
    } else {
      this.transferService.createTransfer(this.currentTransfer).subscribe({
        next: () => {
          this.loadTransfers();
          this.closeModal();
        },
        error: (err) => {
          console.error('Error creating transfer:', err);
          this.error = 'Failed to create transfer';
        }
      });
    }
  }

  deleteTransfer(id: number): void {
    if (confirm('Are you sure you want to delete this transfer?')) {
      this.transferService.deleteTransfer(id).subscribe({
        next: () => {
          this.loadTransfers();
        },
        error: (err) => {
          console.error('Error deleting transfer:', err);
          this.error = 'Failed to delete transfer';
        }
      });
    }
  }

  private getEmptyTransfer(): Transfer {
    return {
      beneficiaryId: 0,
      sourceRib: '',
      amount: 0,
      description: '',
      date: new Date().toISOString().split('T')[0],
      type: 'NORMAL'
    };
  }

  maskRib(rib: string): string {
    if (rib.length <= 8) return rib;
    return rib.substring(0, 4) + '...' + rib.substring(rib.length - 4);
  }

  formatAmount(amount: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR'
    }).format(amount);
  }

  getTypeClass(type: string): string {
    return type === 'INSTANTANE'
      ? 'bg-green-100 dark:bg-green-900/50 text-green-800 dark:text-green-300'
      : 'bg-primary/20 text-primary';
  }

  goToPage(page: number): void {
    this.currentPage = page;
    // Implement pagination logic here
  }
}
