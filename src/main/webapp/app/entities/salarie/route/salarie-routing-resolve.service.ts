import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISalarie, Salarie } from '../salarie.model';
import { SalarieService } from '../service/salarie.service';

@Injectable({ providedIn: 'root' })
export class SalarieRoutingResolveService implements Resolve<ISalarie> {
  constructor(protected service: SalarieService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISalarie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((salarie: HttpResponse<Salarie>) => {
          if (salarie.body) {
            return of(salarie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Salarie());
  }
}
