import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ResourceroutingSharedModule } from 'app/shared/shared.module';
import { VolunteerComponent } from './volunteer.component';
import { VolunteerDetailComponent } from './volunteer-detail.component';
import { VolunteerUpdateComponent } from './volunteer-update.component';
import { VolunteerDeleteDialogComponent } from './volunteer-delete-dialog.component';
import { volunteerRoute } from './volunteer.route';

@NgModule({
  imports: [ResourceroutingSharedModule, RouterModule.forChild(volunteerRoute)],
  declarations: [VolunteerComponent, VolunteerDetailComponent, VolunteerUpdateComponent, VolunteerDeleteDialogComponent],
  entryComponents: [VolunteerDeleteDialogComponent]
})
export class ResourceroutingVolunteerModule {}
