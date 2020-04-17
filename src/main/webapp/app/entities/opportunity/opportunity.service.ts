import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IOpportunity } from 'app/shared/model/opportunity.model';

type EntityResponseType = HttpResponse<IOpportunity>;
type EntityArrayResponseType = HttpResponse<IOpportunity[]>;

@Injectable({ providedIn: 'root' })
export class OpportunityService {
  public resourceUrl = SERVER_API_URL + 'api/opportunities';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/opportunities';

  constructor(protected http: HttpClient) {}

  create(opportunity: IOpportunity): Observable<EntityResponseType> {
    return this.http.post<IOpportunity>(this.resourceUrl, opportunity, { observe: 'response' });
  }

  update(opportunity: IOpportunity): Observable<EntityResponseType> {
    return this.http.put<IOpportunity>(this.resourceUrl, opportunity, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOpportunity>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOpportunity[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOpportunity[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
