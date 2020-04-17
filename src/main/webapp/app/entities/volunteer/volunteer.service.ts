import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IVolunteer } from 'app/shared/model/volunteer.model';

type EntityResponseType = HttpResponse<IVolunteer>;
type EntityArrayResponseType = HttpResponse<IVolunteer[]>;

@Injectable({ providedIn: 'root' })
export class VolunteerService {
  public resourceUrl = SERVER_API_URL + 'api/volunteers';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/volunteers';

  constructor(protected http: HttpClient) {}

  create(volunteer: IVolunteer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(volunteer);
    return this.http
      .post<IVolunteer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(volunteer: IVolunteer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(volunteer);
    return this.http
      .put<IVolunteer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVolunteer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVolunteer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVolunteer[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(volunteer: IVolunteer): IVolunteer {
    const copy: IVolunteer = Object.assign({}, volunteer, {
      hireDate: volunteer.hireDate && volunteer.hireDate.isValid() ? volunteer.hireDate.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.hireDate = res.body.hireDate ? moment(res.body.hireDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((volunteer: IVolunteer) => {
        volunteer.hireDate = volunteer.hireDate ? moment(volunteer.hireDate) : undefined;
      });
    }
    return res;
  }
}
