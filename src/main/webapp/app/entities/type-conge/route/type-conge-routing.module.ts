import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypeCongeComponent } from '../list/type-conge.component';
import { TypeCongeDetailComponent } from '../detail/type-conge-detail.component';
import { TypeCongeUpdateComponent } from '../update/type-conge-update.component';
import { TypeCongeRoutingResolveService } from './type-conge-routing-resolve.service';

const typeCongeRoute: Routes = [
  {
    path: '',
    component: TypeCongeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeCongeDetailComponent,
    resolve: {
      typeConge: TypeCongeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeCongeUpdateComponent,
    resolve: {
      typeConge: TypeCongeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeCongeUpdateComponent,
    resolve: {
      typeConge: TypeCongeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeCongeRoute)],
  exports: [RouterModule],
})
export class TypeCongeRoutingModule {}
