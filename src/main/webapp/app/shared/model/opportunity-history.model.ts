import { Moment } from 'moment';
import { Language } from 'app/shared/model/enumerations/language.model';

export interface IOpportunityHistory {
  id?: number;
  startDate?: Moment;
  endDate?: Moment;
  rating?: number;
  comments?: string;
  language?: Language;
  opportunityId?: number;
  organizationId?: number;
  volunteerId?: number;
}

export class OpportunityHistory implements IOpportunityHistory {
  constructor(
    public id?: number,
    public startDate?: Moment,
    public endDate?: Moment,
    public rating?: number,
    public comments?: string,
    public language?: Language,
    public opportunityId?: number,
    public organizationId?: number,
    public volunteerId?: number
  ) {}
}
