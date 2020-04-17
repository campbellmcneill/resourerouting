import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IOpportunity, Opportunity } from 'app/shared/model/opportunity.model';
import { OpportunityService } from './opportunity.service';
import { OpportunityComponent } from './opportunity.component';
import { OpportunityDetailComponent } from './opportunity-detail.component';
import { OpportunityUpdateComponent } from './opportunity-update.component';

@Injectable({ providedIn: 'root' })
export class OpportunityResolve implements Resolve<IOpportunity> {
  constructor(private service: OpportunityService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOpportunity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((opportunity: HttpResponse<Opportunity>) => {
          if (opportunity.body) {
            return of(opportunity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Opportunity());
  }
}

export const opportunityRoute: Routes = [
  {
    path: '',
    component: OpportunityComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'resourceroutingApp.opportunity.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: OpportunityDetailComponent,
    resolve: {
      opportunity: OpportunityResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'resourceroutingApp.opportunity.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: OpportunityUpdateComponent,
    resolve: {
      opportunity: OpportunityResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'resourceroutingApp.opportunity.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: OpportunityUpdateComponent,
    resolve: {
      opportunity: OpportunityResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'resourceroutingApp.opportunity.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
