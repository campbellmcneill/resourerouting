import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVolunteer } from 'app/shared/model/volunteer.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { VolunteerService } from './volunteer.service';
import { VolunteerDeleteDialogComponent } from './volunteer-delete-dialog.component';

@Component({
  selector: 'jhi-volunteer',
  templateUrl: './volunteer.component.html'
})
export class VolunteerComponent implements OnInit, OnDestroy {
  volunteers: IVolunteer[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;

  constructor(
    protected volunteerService: VolunteerService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks,
    protected activatedRoute: ActivatedRoute
  ) {
    this.volunteers = [];
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
      this.volunteerService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe((res: HttpResponse<IVolunteer[]>) => this.paginateVolunteers(res.body, res.headers));
      return;
    }

    this.volunteerService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IVolunteer[]>) => this.paginateVolunteers(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.volunteers = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.volunteers = [];
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
    this.registerChangeInVolunteers();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVolunteer): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVolunteers(): void {
    this.eventSubscriber = this.eventManager.subscribe('volunteerListModification', () => this.reset());
  }

  delete(volunteer: IVolunteer): void {
    const modalRef = this.modalService.open(VolunteerDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.volunteer = volunteer;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateVolunteers(data: IVolunteer[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.volunteers.push(data[i]);
      }
    }
  }
}
