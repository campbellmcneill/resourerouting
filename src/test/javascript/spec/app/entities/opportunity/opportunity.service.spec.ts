import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { OpportunityService } from 'app/entities/opportunity/opportunity.service';
import { IOpportunity, Opportunity } from 'app/shared/model/opportunity.model';

describe('Service Tests', () => {
  describe('Opportunity Service', () => {
    let injector: TestBed;
    let service: OpportunityService;
    let httpMock: HttpTestingController;
    let elemDefault: IOpportunity;
    let expectedResult: IOpportunity | IOpportunity[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(OpportunityService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Opportunity(0, 'AAAAAAA', 'AAAAAAA', 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Opportunity', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Opportunity()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Opportunity', () => {
        const returnedFromService = Object.assign(
          {
            opportunityTitle: 'BBBBBB',
            opportunityDescription: 'BBBBBB',
            weeklyTimeCommitment: 1,
            duration: 1
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Opportunity', () => {
        const returnedFromService = Object.assign(
          {
            opportunityTitle: 'BBBBBB',
            opportunityDescription: 'BBBBBB',
            weeklyTimeCommitment: 1,
            duration: 1
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Opportunity', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
