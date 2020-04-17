import { IOpportunity } from 'app/shared/model/opportunity.model';

export interface ITask {
  id?: number;
  title?: string;
  description?: string;
  jobs?: IOpportunity[];
}

export class Task implements ITask {
  constructor(public id?: number, public title?: string, public description?: string, public jobs?: IOpportunity[]) {}
}
