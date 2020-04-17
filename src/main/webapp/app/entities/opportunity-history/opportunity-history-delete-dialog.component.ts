import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOpportunityHistory } from 'app/shared/model/opportunity-history.model';
import { OpportunityHistoryService } from './opportunity-history.service';

@Component({
  templateUrl: './opportunity-history-delete-dialog.component.html'
})
export class OpportunityHistoryDeleteDialogComponent {
  opportunityHistory?: IOpportunityHistory;

  constructor(
    protected opportunityHistoryService: OpportunityHistoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.opportunityHistoryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('opportunityHistoryListModification');
      this.activeModal.close();
    });
  }
}
