import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResourceroutingTestModule } from '../../../test.module';
import { OpportunityDetailComponent } from 'app/entities/opportunity/opportunity-detail.component';
import { Opportunity } from 'app/shared/model/opportunity.model';

describe('Component Tests', () => {
  describe('Opportunity Management Detail Component', () => {
    let comp: OpportunityDetailComponent;
    let fixture: ComponentFixture<OpportunityDetailComponent>;
    const route = ({ data: of({ opportunity: new Opportunity(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ResourceroutingTestModule],
        declarations: [OpportunityDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(OpportunityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OpportunityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load opportunity on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.opportunity).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
