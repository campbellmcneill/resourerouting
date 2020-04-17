import { IVolunteer } from 'app/shared/model/volunteer.model';

export interface IOrganization {
  id?: number;
  organizationName?: string;
  locationId?: number;
  opportunityId?: number;
  volunteers?: IVolunteer[];
}

export class Organization implements IOrganization {
  constructor(
    public id?: number,
    public organizationName?: string,
    public locationId?: number,
    public opportunityId?: number,
    public volunteers?: IVolunteer[]
  ) {}
}
