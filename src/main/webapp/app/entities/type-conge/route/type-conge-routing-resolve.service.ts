import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeConge, TypeConge } from '../type-conge.model';
import { TypeCongeService } from '../service/type-conge.service';

@Injectable({ providedIn: 'root' })
export class TypeCongeRoutingResolveService implements Resolve<ITypeConge> {
  constructor(protected service: TypeCongeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeConge> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typeConge: HttpResponse<TypeConge>) => {
          if (typeConge.body) {
            return of(typeConge.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeConge());
  }
}
