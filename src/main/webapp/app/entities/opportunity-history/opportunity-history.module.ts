import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ResourceroutingSharedModule } from 'app/shared/shared.module';
import { OpportunityHistoryComponent } from './opportunity-history.component';
import { OpportunityHistoryDetailComponent } from './opportunity-history-detail.component';
import { OpportunityHistoryUpdateComponent } from './opportunity-history-update.component';
import { OpportunityHistoryDeleteDialogComponent } from './opportunity-history-delete-dialog.component';
import { opportunityHistoryRoute } from './opportunity-history.route';

@NgModule({
  imports: [ResourceroutingSharedModule, RouterModule.forChild(opportunityHistoryRoute)],
  declarations: [
    OpportunityHistoryComponent,
    OpportunityHistoryDetailComponent,
    OpportunityHistoryUpdateComponent,
    OpportunityHistoryDeleteDialogComponent
  ],
  entryComponents: [OpportunityHistoryDeleteDialogComponent]
})
export class ResourceroutingOpportunityHistoryModule {}
