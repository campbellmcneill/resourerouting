import { ITask } from 'app/shared/model/task.model';

export interface IOpportunity {
  id?: number;
  opportunityTitle?: string;
  opportunityDescription?: string;
  weeklyTimeCommitment?: number;
  duration?: number;
  tasks?: ITask[];
  volunteerId?: number;
}

export class Opportunity implements IOpportunity {
  constructor(
    public id?: number,
    public opportunityTitle?: string,
    public opportunityDescription?: string,
    public weeklyTimeCommitment?: number,
    public duration?: number,
    public tasks?: ITask[],
    public volunteerId?: number
  ) {}
}
