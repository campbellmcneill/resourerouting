import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IOpportunityHistory, OpportunityHistory } from 'app/shared/model/opportunity-history.model';
import { OpportunityHistoryService } from './opportunity-history.service';
import { OpportunityHistoryComponent } from './opportunity-history.component';
import { OpportunityHistoryDetailComponent } from './opportunity-history-detail.component';
import { OpportunityHistoryUpdateComponent } from './opportunity-history-update.component';

@Injectable({ providedIn: 'root' })
export class OpportunityHistoryResolve implements Resolve<IOpportunityHistory> {
  constructor(private service: OpportunityHistoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOpportunityHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((opportunityHistory: HttpResponse<OpportunityHistory>) => {
          if (opportunityHistory.body) {
            return of(opportunityHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OpportunityHistory());
  }
}

export const opportunityHistoryRoute: Routes = [
  {
    path: '',
    component: OpportunityHistoryComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'resourceroutingApp.opportunityHistory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: OpportunityHistoryDetailComponent,
    resolve: {
      opportunityHistory: OpportunityHistoryResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'resourceroutingApp.opportunityHistory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: OpportunityHistoryUpdateComponent,
    resolve: {
      opportunityHistory: OpportunityHistoryResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'resourceroutingApp.opportunityHistory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: OpportunityHistoryUpdateComponent,
    resolve: {
      opportunityHistory: OpportunityHistoryResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'resourceroutingApp.opportunityHistory.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
