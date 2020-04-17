import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOpportunityHistory } from 'app/shared/model/opportunity-history.model';

@Component({
  selector: 'jhi-opportunity-history-detail',
  templateUrl: './opportunity-history-detail.component.html'
})
export class OpportunityHistoryDetailComponent implements OnInit {
  opportunityHistory: IOpportunityHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ opportunityHistory }) => (this.opportunityHistory = opportunityHistory));
  }

  previousState(): void {
    window.history.back();
  }
}
