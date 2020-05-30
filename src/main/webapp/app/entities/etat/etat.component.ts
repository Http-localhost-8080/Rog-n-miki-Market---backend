import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEtat } from 'app/shared/model/etat.model';
import { EtatService } from './etat.service';
import { EtatDeleteDialogComponent } from './etat-delete-dialog.component';

@Component({
  selector: 'jhi-etat',
  templateUrl: './etat.component.html'
})
export class EtatComponent implements OnInit, OnDestroy {
  etats?: IEtat[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected etatService: EtatService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.etatService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IEtat[]>) => (this.etats = res.body || []));
      return;
    }

    this.etatService.query().subscribe((res: HttpResponse<IEtat[]>) => (this.etats = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEtats();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEtat): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEtats(): void {
    this.eventSubscriber = this.eventManager.subscribe('etatListModification', () => this.loadAll());
  }

  delete(etat: IEtat): void {
    const modalRef = this.modalService.open(EtatDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.etat = etat;
  }
}
