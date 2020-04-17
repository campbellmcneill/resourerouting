import { Moment } from 'moment';
import { IOpportunity } from 'app/shared/model/opportunity.model';

export interface IVolunteer {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  hireDate?: Moment;
  opportunities?: IOpportunity[];
  supervisorId?: number;
  organizationId?: number;
}

export class Volunteer implements IVolunteer {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public phoneNumber?: string,
    public hireDate?: Moment,
    public opportunities?: IOpportunity[],
    public supervisorId?: number,
    public organizationId?: number
  ) {}
}
