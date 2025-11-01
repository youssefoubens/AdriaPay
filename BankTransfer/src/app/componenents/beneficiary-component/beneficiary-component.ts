// src/app/componenents/beneficiary-component/beneficiary-component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { BeneficiaryService, Beneficiary } from '../../services/BeneficiaryService/beneficiary-service';
import { NavbarComponent } from '../navbar-component/navbar-component';

@Component({
  selector: 'app-beneficiary',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, NavbarComponent],
  templateUrl: './beneficiary-component.html',
  styleUrls: ['./beneficiary-component.css']
})
export class BeneficiaryComponent implements OnInit {
  beneficiaries: Beneficiary[] = [];
  filteredBeneficiaries: Beneficiary[] = [];
  searchQuery = '';
  loading = false;
  error: string | null = null;

  showModal = false;
  isEditMode = false;
  currentBeneficiary: Beneficiary = this.getEmptyBeneficiary();

  constructor(private beneficiaryService: BeneficiaryService) { }

  ngOnInit(): void {
    this.loadBeneficiaries();
  }

  loadBeneficiaries(): void {
    this.loading = true;
    this.error = null;

    this.beneficiaryService.getAllBeneficiaries().subscribe({
      next: (data) => {
        this.beneficiaries = data;
        this.filteredBeneficiaries = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading beneficiaries:', err);
        this.error = 'Failed to load beneficiaries';
        this.loading = false;
      }
    });
  }

  searchBeneficiaries(): void {
    if (!this.searchQuery.trim()) {
      this.filteredBeneficiaries = this.beneficiaries;
      return;
    }

    const query = this.searchQuery.toLowerCase();
    this.filteredBeneficiaries = this.beneficiaries.filter(b =>
      b.firstName?.toLowerCase().includes(query) ||
      b.lastName?.toLowerCase().includes(query) ||
      b.rib?.toLowerCase().includes(query) ||
      b.type?.toLowerCase().includes(query)
    );
  }

  openAddModal(): void {
    this.isEditMode = false;
    this.currentBeneficiary = this.getEmptyBeneficiary();
    this.showModal = true;
  }

  openEditModal(beneficiary: Beneficiary): void {
    this.isEditMode = true;
    this.currentBeneficiary = { ...beneficiary };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.currentBeneficiary = this.getEmptyBeneficiary();
  }

  saveBeneficiary(): void {
    if (this.isEditMode && this.currentBeneficiary.id) {
      this.beneficiaryService.updateBeneficiary(
        this.currentBeneficiary.id,
        this.currentBeneficiary
      ).subscribe({
        next: () => {
          this.loadBeneficiaries();
          this.closeModal();
        },
        error: (err) => {
          console.error('Error updating beneficiary:', err);
          this.error = 'Failed to update beneficiary';
        }
      });
    } else {
      this.beneficiaryService.createBeneficiary(this.currentBeneficiary).subscribe({
        next: () => {
          this.loadBeneficiaries();
          this.closeModal();
        },
        error: (err) => {
          console.error('Error creating beneficiary:', err);
          this.error = 'Failed to create beneficiary';
        }
      });
    }
  }

  deleteBeneficiary(id: number): void {
    if (confirm('Are you sure you want to delete this beneficiary?')) {
      this.beneficiaryService.deleteBeneficiary(id).subscribe({
        next: () => {
          this.loadBeneficiaries();
        },
        error: (err) => {
          console.error('Error deleting beneficiary:', err);
          this.error = 'Failed to delete beneficiary';
        }
      });
    }
  }

  private getEmptyBeneficiary(): Beneficiary {
    return {
      firstName: '',
      lastName: '',
      rib: '',
      type: 'PHYSIQUE'
    };
  }

  maskRib(rib: string): string {
    if (!rib || rib.length <= 8) return rib;
    return rib.substring(0, 4) + '****' + rib.substring(rib.length - 4);
  }

  getInitial(name?: string): string {
    return name?.charAt(0).toUpperCase() || '?';
  }

  getTypeClass(type: string): string {
    return type === 'PHYSIQUE' ? 'type-personal' : 'type-business';
  }
}
