import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'region',
        loadChildren: () => import('./region/region.module').then(m => m.ResourceroutingRegionModule)
      },
      {
        path: 'country',
        loadChildren: () => import('./country/country.module').then(m => m.ResourceroutingCountryModule)
      },
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.ResourceroutingLocationModule)
      },
      {
        path: 'organization',
        loadChildren: () => import('./organization/organization.module').then(m => m.ResourceroutingOrganizationModule)
      },
      {
        path: 'task',
        loadChildren: () => import('./task/task.module').then(m => m.ResourceroutingTaskModule)
      },
      {
        path: 'volunteer',
        loadChildren: () => import('./volunteer/volunteer.module').then(m => m.ResourceroutingVolunteerModule)
      },
      {
        path: 'opportunity',
        loadChildren: () => import('./opportunity/opportunity.module').then(m => m.ResourceroutingOpportunityModule)
      },
      {
        path: 'opportunity-history',
        loadChildren: () => import('./opportunity-history/opportunity-history.module').then(m => m.ResourceroutingOpportunityHistoryModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ResourceroutingEntityModule {}
