import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SalarieComponent } from '../list/salarie.component';
import { SalarieDetailComponent } from '../detail/salarie-detail.component';
import { SalarieUpdateComponent } from '../update/salarie-update.component';
import { SalarieRoutingResolveService } from './salarie-routing-resolve.service';

const salarieRoute: Routes = [
  {
    path: '',
    component: SalarieComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SalarieDetailComponent,
    resolve: {
      salarie: SalarieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SalarieUpdateComponent,
    resolve: {
      salarie: SalarieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SalarieUpdateComponent,
    resolve: {
      salarie: SalarieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(salarieRoute)],
  exports: [RouterModule],
})
export class SalarieRoutingModule {}
