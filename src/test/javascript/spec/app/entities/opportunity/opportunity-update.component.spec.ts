import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ResourceroutingTestModule } from '../../../test.module';
import { OpportunityUpdateComponent } from 'app/entities/opportunity/opportunity-update.component';
import { OpportunityService } from 'app/entities/opportunity/opportunity.service';
import { Opportunity } from 'app/shared/model/opportunity.model';

describe('Component Tests', () => {
  describe('Opportunity Management Update Component', () => {
    let comp: OpportunityUpdateComponent;
    let fixture: ComponentFixture<OpportunityUpdateComponent>;
    let service: OpportunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ResourceroutingTestModule],
        declarations: [OpportunityUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(OpportunityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OpportunityUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OpportunityService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Opportunity(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Opportunity();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
