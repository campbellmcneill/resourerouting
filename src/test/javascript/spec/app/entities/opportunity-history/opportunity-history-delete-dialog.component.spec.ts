import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ResourceroutingTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { OpportunityHistoryDeleteDialogComponent } from 'app/entities/opportunity-history/opportunity-history-delete-dialog.component';
import { OpportunityHistoryService } from 'app/entities/opportunity-history/opportunity-history.service';

describe('Component Tests', () => {
  describe('OpportunityHistory Management Delete Component', () => {
    let comp: OpportunityHistoryDeleteDialogComponent;
    let fixture: ComponentFixture<OpportunityHistoryDeleteDialogComponent>;
    let service: OpportunityHistoryService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ResourceroutingTestModule],
        declarations: [OpportunityHistoryDeleteDialogComponent]
      })
        .overrideTemplate(OpportunityHistoryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OpportunityHistoryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(OpportunityHistoryService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
