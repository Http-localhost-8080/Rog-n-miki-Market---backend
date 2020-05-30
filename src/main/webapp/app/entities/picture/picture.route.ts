import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPicture, Picture } from 'app/shared/model/picture.model';
import { PictureService } from './picture.service';
import { PictureComponent } from './picture.component';
import { PictureDetailComponent } from './picture-detail.component';
import { PictureUpdateComponent } from './picture-update.component';

@Injectable({ providedIn: 'root' })
export class PictureResolve implements Resolve<IPicture> {
  constructor(private service: PictureService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPicture> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((picture: HttpResponse<Picture>) => {
          if (picture.body) {
            return of(picture.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Picture());
  }
}

export const pictureRoute: Routes = [
  {
    path: '',
    component: PictureComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'Pictures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PictureDetailComponent,
    resolve: {
      picture: PictureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Pictures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PictureUpdateComponent,
    resolve: {
      picture: PictureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Pictures'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PictureUpdateComponent,
    resolve: {
      picture: PictureResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Pictures'
    },
    canActivate: [UserRouteAccessService]
  }
];
