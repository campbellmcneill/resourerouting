import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IOrganization, Organization } from 'app/shared/model/organization.model';
import { OrganizationService } from './organization.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { IOpportunity } from 'app/shared/model/opportunity.model';
import { OpportunityService } from 'app/entities/opportunity/opportunity.service';

type SelectableEntity = ILocation | IOpportunity;

@Component({
  selector: 'jhi-organization-update',
  templateUrl: './organization-update.component.html'
})
export class OrganizationUpdateComponent implements OnInit {
  isSaving = false;
  locations: ILocation[] = [];
  opportunities: IOpportunity[] = [];

  editForm = this.fb.group({
    id: [],
    organizationName: [null, [Validators.required]],
    locationId: [],
    opportunityId: []
  });

  constructor(
    protected organizationService: OrganizationService,
    protected locationService: LocationService,
    protected opportunityService: OpportunityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ organization }) => {
      this.updateForm(organization);

      this.locationService
        .query({ filter: 'organization-is-null' })
        .pipe(
          map((res: HttpResponse<ILocation[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ILocation[]) => {
          if (!organization.locationId) {
            this.locations = resBody;
          } else {
            this.locationService
              .find(organization.locationId)
              .pipe(
                map((subRes: HttpResponse<ILocation>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ILocation[]) => (this.locations = concatRes));
          }
        });

      this.opportunityService
        .query({ filter: 'organization-is-null' })
        .pipe(
          map((res: HttpResponse<IOpportunity[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IOpportunity[]) => {
          if (!organization.opportunityId) {
            this.opportunities = resBody;
          } else {
            this.opportunityService
              .find(organization.opportunityId)
              .pipe(
                map((subRes: HttpResponse<IOpportunity>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IOpportunity[]) => (this.opportunities = concatRes));
          }
        });
    });
  }

  updateForm(organization: IOrganization): void {
    this.editForm.patchValue({
      id: organization.id,
      organizationName: organization.organizationName,
      locationId: organization.locationId,
      opportunityId: organization.opportunityId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const organization = this.createFromForm();
    if (organization.id !== undefined) {
      this.subscribeToSaveResponse(this.organizationService.update(organization));
    } else {
      this.subscribeToSaveResponse(this.organizationService.create(organization));
    }
  }

  private createFromForm(): IOrganization {
    return {
      ...new Organization(),
      id: this.editForm.get(['id'])!.value,
      organizationName: this.editForm.get(['organizationName'])!.value,
      locationId: this.editForm.get(['locationId'])!.value,
      opportunityId: this.editForm.get(['opportunityId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganization>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
