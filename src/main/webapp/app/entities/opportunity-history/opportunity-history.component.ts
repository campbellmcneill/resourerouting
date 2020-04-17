import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOpportunityHistory } from 'app/shared/model/opportunity-history.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { OpportunityHistoryService } from './opportunity-history.service';
import { OpportunityHistoryDeleteDialogComponent } from './opportunity-history-delete-dialog.component';

@Component({
  selector: 'jhi-opportunity-history',
  templateUrl: './opportunity-history.component.html'
})
export class OpportunityHistoryComponent implements OnInit, OnDestroy {
  opportunityHistories: IOpportunityHistory[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected opportunityHistoryService: OpportunityHistoryService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.opportunityHistories = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.opportunityHistoryService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe((res: HttpResponse<IOpportunityHistory[]>) => this.paginateOpportunityHistories(res.body, res.headers));
      return;
    }

    this.opportunityHistoryService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IOpportunityHistory[]>) => this.paginateOpportunityHistories(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.opportunityHistories = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.opportunityHistories = [];
    this.links = {
      last: 0
    };
    this.page = 0;
    if (query) {
      this.predicate = '_score';
      this.ascending = false;
    } else {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInOpportunityHistories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOpportunityHistory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInOpportunityHistories(): void {
    this.eventSubscriber = this.eventManager.subscribe('opportunityHistoryListModification', () => this.reset());
  }

  delete(opportunityHistory: IOpportunityHistory): void {
    const modalRef = this.modalService.open(OpportunityHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.opportunityHistory = opportunityHistory;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateOpportunityHistories(data: IOpportunityHistory[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.opportunityHistories.push(data[i]);
      }
    }
  }
}
