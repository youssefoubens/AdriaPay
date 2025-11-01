import { Routes } from '@angular/router';
import { DashboardComponent } from './componenents/dashboard-component/dashboard-component';
import { BeneficiaryComponent } from './componenents/beneficiary-component/beneficiary-component';
import { TransferComponent } from './componenents/transfer-component/transfer-component';
import { ChatbotComponent } from './componenents/chatbot-component/chatbot-component';
import {NavbarComponent} from './componenents/navbar-component/navbar-component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'beneficiaries', component: BeneficiaryComponent },
  { path: 'transfer', component: TransferComponent },
  { path: 'chatbot', component: ChatbotComponent },
  { path: 'nav', component: NavbarComponent }
];
