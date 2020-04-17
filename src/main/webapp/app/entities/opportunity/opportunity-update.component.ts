import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IOpportunity, Opportunity } from 'app/shared/model/opportunity.model';
import { OpportunityService } from './opportunity.service';
import { ITask } from 'app/shared/model/task.model';
import { TaskService } from 'app/entities/task/task.service';
import { IVolunteer } from 'app/shared/model/volunteer.model';
import { VolunteerService } from 'app/entities/volunteer/volunteer.service';

type SelectableEntity = ITask | IVolunteer;

@Component({
  selector: 'jhi-opportunity-update',
  templateUrl: './opportunity-update.component.html'
})
export class OpportunityUpdateComponent implements OnInit {
  isSaving = false;
  tasks: ITask[] = [];
  volunteers: IVolunteer[] = [];

  editForm = this.fb.group({
    id: [],
    opportunityTitle: [],
    opportunityDescription: [],
    weeklyTimeCommitment: [],
    duration: [],
    tasks: [],
    volunteerId: []
  });

  constructor(
    protected opportunityService: OpportunityService,
    protected taskService: TaskService,
    protected volunteerService: VolunteerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ opportunity }) => {
      this.updateForm(opportunity);

      this.taskService.query().subscribe((res: HttpResponse<ITask[]>) => (this.tasks = res.body || []));

      this.volunteerService.query().subscribe((res: HttpResponse<IVolunteer[]>) => (this.volunteers = res.body || []));
    });
  }

  updateForm(opportunity: IOpportunity): void {
    this.editForm.patchValue({
      id: opportunity.id,
      opportunityTitle: opportunity.opportunityTitle,
      opportunityDescription: opportunity.opportunityDescription,
      weeklyTimeCommitment: opportunity.weeklyTimeCommitment,
      duration: opportunity.duration,
      tasks: opportunity.tasks,
      volunteerId: opportunity.volunteerId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const opportunity = this.createFromForm();
    if (opportunity.id !== undefined) {
      this.subscribeToSaveResponse(this.opportunityService.update(opportunity));
    } else {
      this.subscribeToSaveResponse(this.opportunityService.create(opportunity));
    }
  }

  private createFromForm(): IOpportunity {
    return {
      ...new Opportunity(),
      id: this.editForm.get(['id'])!.value,
      opportunityTitle: this.editForm.get(['opportunityTitle'])!.value,
      opportunityDescription: this.editForm.get(['opportunityDescription'])!.value,
      weeklyTimeCommitment: this.editForm.get(['weeklyTimeCommitment'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      tasks: this.editForm.get(['tasks'])!.value,
      volunteerId: this.editForm.get(['volunteerId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOpportunity>>): void {
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

  getSelected(selectedVals: ITask[], option: ITask): ITask {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
