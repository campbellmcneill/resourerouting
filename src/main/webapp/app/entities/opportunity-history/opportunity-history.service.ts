import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IOpportunityHistory } from 'app/shared/model/opportunity-history.model';

type EntityResponseType = HttpResponse<IOpportunityHistory>;
type EntityArrayResponseType = HttpResponse<IOpportunityHistory[]>;

@Injectable({ providedIn: 'root' })
export class OpportunityHistoryService {
  public resourceUrl = SERVER_API_URL + 'api/opportunity-histories';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/opportunity-histories';

  constructor(protected http: HttpClient) {}

  create(opportunityHistory: IOpportunityHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(opportunityHistory);
    return this.http
      .post<IOpportunityHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(opportunityHistory: IOpportunityHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(opportunityHistory);
    return this.http
      .put<IOpportunityHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOpportunityHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOpportunityHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOpportunityHistory[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(opportunityHistory: IOpportunityHistory): IOpportunityHistory {
    const copy: IOpportunityHistory = Object.assign({}, opportunityHistory, {
      startDate: opportunityHistory.startDate && opportunityHistory.startDate.isValid() ? opportunityHistory.startDate.toJSON() : undefined,
      endDate: opportunityHistory.endDate && opportunityHistory.endDate.isValid() ? opportunityHistory.endDate.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? moment(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((opportunityHistory: IOpportunityHistory) => {
        opportunityHistory.startDate = opportunityHistory.startDate ? moment(opportunityHistory.startDate) : undefined;
        opportunityHistory.endDate = opportunityHistory.endDate ? moment(opportunityHistory.endDate) : undefined;
      });
    }
    return res;
  }
}
