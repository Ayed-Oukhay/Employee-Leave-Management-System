import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemandeCongeComponent } from '../list/validation-demande-conge.component';
import { DemandeCongeDetailComponent } from '../detail/validation-demande-conge-detail.component';
import { DemandeCongeUpdateComponent } from '../update/validation-demande-conge-update.component';
import { DemandeCongeRoutingResolveService } from './validation-demande-conge-routing-resolve.service';

const demandeCongeRoute: Routes = [
  {
    path: '',
    component: DemandeCongeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemandeCongeDetailComponent,
    resolve: {
      demandeConge: DemandeCongeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemandeCongeUpdateComponent,
    resolve: {
      demandeConge: DemandeCongeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemandeCongeUpdateComponent,
    resolve: {
      demandeConge: DemandeCongeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demandeCongeRoute)],
  exports: [RouterModule],
})
export class DemandeCongeRoutingModule {}
