import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEtat, Etat } from 'app/shared/model/etat.model';
import { EtatService } from './etat.service';
import { EtatComponent } from './etat.component';
import { EtatDetailComponent } from './etat-detail.component';
import { EtatUpdateComponent } from './etat-update.component';

@Injectable({ providedIn: 'root' })
export class EtatResolve implements Resolve<IEtat> {
  constructor(private service: EtatService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEtat> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((etat: HttpResponse<Etat>) => {
          if (etat.body) {
            return of(etat.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Etat());
  }
}

export const etatRoute: Routes = [
  {
    path: '',
    component: EtatComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Etats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EtatDetailComponent,
    resolve: {
      etat: EtatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Etats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EtatUpdateComponent,
    resolve: {
      etat: EtatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Etats'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EtatUpdateComponent,
    resolve: {
      etat: EtatResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Etats'
    },
    canActivate: [UserRouteAccessService]
  }
];
