import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPannier, Pannier } from 'app/shared/model/pannier.model';
import { PannierService } from './pannier.service';
import { PannierComponent } from './pannier.component';
import { PannierDetailComponent } from './pannier-detail.component';
import { PannierUpdateComponent } from './pannier-update.component';

@Injectable({ providedIn: 'root' })
export class PannierResolve implements Resolve<IPannier> {
  constructor(private service: PannierService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPannier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((pannier: HttpResponse<Pannier>) => {
          if (pannier.body) {
            return of(pannier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pannier());
  }
}

export const pannierRoute: Routes = [
  {
    path: '',
    component: PannierComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Panniers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PannierDetailComponent,
    resolve: {
      pannier: PannierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Panniers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PannierUpdateComponent,
    resolve: {
      pannier: PannierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Panniers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PannierUpdateComponent,
    resolve: {
      pannier: PannierResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Panniers'
    },
    canActivate: [UserRouteAccessService]
  }
];
