import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IOpportunity } from 'app/shared/model/opportunity.model';
import { OpportunityService } from './opportunity.service';

@Component({
  templateUrl: './opportunity-delete-dialog.component.html'
})
export class OpportunityDeleteDialogComponent {
  opportunity?: IOpportunity;

  constructor(
    protected opportunityService: OpportunityService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.opportunityService.delete(id).subscribe(() => {
      this.eventManager.broadcast('opportunityListModification');
      this.activeModal.close();
    });
  }
}
