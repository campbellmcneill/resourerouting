import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResourceroutingTestModule } from '../../../test.module';
import { OpportunityHistoryDetailComponent } from 'app/entities/opportunity-history/opportunity-history-detail.component';
import { OpportunityHistory } from 'app/shared/model/opportunity-history.model';

describe('Component Tests', () => {
  describe('OpportunityHistory Management Detail Component', () => {
    let comp: OpportunityHistoryDetailComponent;
    let fixture: ComponentFixture<OpportunityHistoryDetailComponent>;
    const route = ({ data: of({ opportunityHistory: new OpportunityHistory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ResourceroutingTestModule],
        declarations: [OpportunityHistoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(OpportunityHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OpportunityHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load opportunityHistory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.opportunityHistory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
