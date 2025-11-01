// src/app/services/transfer.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Transfer {
  id?: number;
  beneficiaryId: number;
  beneficiaryName?: string;
  sourceRib: string;
  amount: number;
  description: string;
  date: string;
  type: 'NORMAL' | 'INSTANTANE';
  status?: 'PENDING' | 'COMPLETED' | 'FAILED';
}

export interface DashboardStats {
  totalBeneficiaries: number;
  totalTransfers: number;
  recentTransactions: number;
}

@Injectable({
  providedIn: 'root'
})
export class TransferService {
  private apiUrl = `${environment.apiUrl}/VIREMENT-SERVICE/api/virements`;

  constructor(private http: HttpClient) { }

  getAllTransfers(): Observable<Transfer[]> {
    return this.http.get<Transfer[]>(this.apiUrl);
  }

  getTransferById(id: number): Observable<Transfer> {
    return this.http.get<Transfer>(`${this.apiUrl}/${id}`);
  }

  createTransfer(transfer: Transfer): Observable<Transfer> {
    return this.http.post<Transfer>(this.apiUrl, transfer);
  }

  updateTransfer(id: number, transfer: Transfer): Observable<Transfer> {
    return this.http.put<Transfer>(`${this.apiUrl}/${id}`, transfer);
  }

  deleteTransfer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  searchTransfers(query: string): Observable<Transfer[]> {
    return this.http.get<Transfer[]>(`${this.apiUrl}/search?q=${query}`);
  }

  getRecentTransfers(days: number = 7): Observable<Transfer[]> {
    return this.http.get<Transfer[]>(`${this.apiUrl}/recent?days=${days}`);
  }

  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.apiUrl}/dashboard/stats`);
  }
}
