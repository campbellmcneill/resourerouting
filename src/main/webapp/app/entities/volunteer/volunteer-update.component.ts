import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IVolunteer, Volunteer } from 'app/shared/model/volunteer.model';
import { VolunteerService } from './volunteer.service';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from 'app/entities/organization/organization.service';

type SelectableEntity = IVolunteer | IOrganization;

@Component({
  selector: 'jhi-volunteer-update',
  templateUrl: './volunteer-update.component.html'
})
export class VolunteerUpdateComponent implements OnInit {
  isSaving = false;
  volunteers: IVolunteer[] = [];
  organizations: IOrganization[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    email: [],
    phoneNumber: [],
    hireDate: [],
    supervisorId: [],
    organizationId: []
  });

  constructor(
    protected volunteerService: VolunteerService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ volunteer }) => {
      if (!volunteer.id) {
        const today = moment().startOf('day');
        volunteer.hireDate = today;
      }

      this.updateForm(volunteer);

      this.volunteerService.query().subscribe((res: HttpResponse<IVolunteer[]>) => (this.volunteers = res.body || []));

      this.organizationService.query().subscribe((res: HttpResponse<IOrganization[]>) => (this.organizations = res.body || []));
    });
  }

  updateForm(volunteer: IVolunteer): void {
    this.editForm.patchValue({
      id: volunteer.id,
      firstName: volunteer.firstName,
      lastName: volunteer.lastName,
      email: volunteer.email,
      phoneNumber: volunteer.phoneNumber,
      hireDate: volunteer.hireDate ? volunteer.hireDate.format(DATE_TIME_FORMAT) : null,
      supervisorId: volunteer.supervisorId,
      organizationId: volunteer.organizationId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const volunteer = this.createFromForm();
    if (volunteer.id !== undefined) {
      this.subscribeToSaveResponse(this.volunteerService.update(volunteer));
    } else {
      this.subscribeToSaveResponse(this.volunteerService.create(volunteer));
    }
  }

  private createFromForm(): IVolunteer {
    return {
      ...new Volunteer(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      hireDate: this.editForm.get(['hireDate'])!.value ? moment(this.editForm.get(['hireDate'])!.value, DATE_TIME_FORMAT) : undefined,
      supervisorId: this.editForm.get(['supervisorId'])!.value,
      organizationId: this.editForm.get(['organizationId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVolunteer>>): void {
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
