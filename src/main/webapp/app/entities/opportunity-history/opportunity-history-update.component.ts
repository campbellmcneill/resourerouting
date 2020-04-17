import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IOpportunityHistory, OpportunityHistory } from 'app/shared/model/opportunity-history.model';
import { OpportunityHistoryService } from './opportunity-history.service';
import { IOpportunity } from 'app/shared/model/opportunity.model';
import { OpportunityService } from 'app/entities/opportunity/opportunity.service';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from 'app/entities/organization/organization.service';
import { IVolunteer } from 'app/shared/model/volunteer.model';
import { VolunteerService } from 'app/entities/volunteer/volunteer.service';

type SelectableEntity = IOpportunity | IOrganization | IVolunteer;

@Component({
  selector: 'jhi-opportunity-history-update',
  templateUrl: './opportunity-history-update.component.html'
})
export class OpportunityHistoryUpdateComponent implements OnInit {
  isSaving = false;
  opportunities: IOpportunity[] = [];
  organizations: IOrganization[] = [];
  volunteers: IVolunteer[] = [];

  editForm = this.fb.group({
    id: [],
    startDate: [],
    endDate: [],
    rating: [],
    comments: [],
    language: [],
    opportunityId: [],
    organizationId: [],
    volunteerId: []
  });

  constructor(
    protected opportunityHistoryService: OpportunityHistoryService,
    protected opportunityService: OpportunityService,
    protected organizationService: OrganizationService,
    protected volunteerService: VolunteerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ opportunityHistory }) => {
      if (!opportunityHistory.id) {
        const today = moment().startOf('day');
        opportunityHistory.startDate = today;
        opportunityHistory.endDate = today;
      }

      this.updateForm(opportunityHistory);

      this.opportunityService
        .query({ filter: 'opportunityhistory-is-null' })
        .pipe(
          map((res: HttpResponse<IOpportunity[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IOpportunity[]) => {
          if (!opportunityHistory.opportunityId) {
            this.opportunities = resBody;
          } else {
            this.opportunityService
              .find(opportunityHistory.opportunityId)
              .pipe(
                map((subRes: HttpResponse<IOpportunity>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IOpportunity[]) => (this.opportunities = concatRes));
          }
        });

      this.organizationService
        .query({ filter: 'opportunityhistory-is-null' })
        .pipe(
          map((res: HttpResponse<IOrganization[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IOrganization[]) => {
          if (!opportunityHistory.organizationId) {
            this.organizations = resBody;
          } else {
            this.organizationService
              .find(opportunityHistory.organizationId)
              .pipe(
                map((subRes: HttpResponse<IOrganization>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IOrganization[]) => (this.organizations = concatRes));
          }
        });

      this.volunteerService
        .query({ filter: 'opportunityhistory-is-null' })
        .pipe(
          map((res: HttpResponse<IVolunteer[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IVolunteer[]) => {
          if (!opportunityHistory.volunteerId) {
            this.volunteers = resBody;
          } else {
            this.volunteerService
              .find(opportunityHistory.volunteerId)
              .pipe(
                map((subRes: HttpResponse<IVolunteer>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IVolunteer[]) => (this.volunteers = concatRes));
          }
        });
    });
  }

  updateForm(opportunityHistory: IOpportunityHistory): void {
    this.editForm.patchValue({
      id: opportunityHistory.id,
      startDate: opportunityHistory.startDate ? opportunityHistory.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: opportunityHistory.endDate ? opportunityHistory.endDate.format(DATE_TIME_FORMAT) : null,
      rating: opportunityHistory.rating,
      comments: opportunityHistory.comments,
      language: opportunityHistory.language,
      opportunityId: opportunityHistory.opportunityId,
      organizationId: opportunityHistory.organizationId,
      volunteerId: opportunityHistory.volunteerId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const opportunityHistory = this.createFromForm();
    if (opportunityHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.opportunityHistoryService.update(opportunityHistory));
    } else {
      this.subscribeToSaveResponse(this.opportunityHistoryService.create(opportunityHistory));
    }
  }

  private createFromForm(): IOpportunityHistory {
    return {
      ...new OpportunityHistory(),
      id: this.editForm.get(['id'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? moment(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? moment(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      rating: this.editForm.get(['rating'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      language: this.editForm.get(['language'])!.value,
      opportunityId: this.editForm.get(['opportunityId'])!.value,
      organizationId: this.editForm.get(['organizationId'])!.value,
      volunteerId: this.editForm.get(['volunteerId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOpportunityHistory>>): void {
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
