import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INote } from 'app/shared/model/note.model';
import { NoteService } from './note.service';
import { NoteDeleteDialogComponent } from './note-delete-dialog.component';

@Component({
  selector: 'jhi-note',
  templateUrl: './note.component.html'
})
export class NoteComponent implements OnInit, OnDestroy {
  notes?: INote[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected noteService: NoteService,
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
      this.noteService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<INote[]>) => (this.notes = res.body || []));
      return;
    }

    this.noteService.query().subscribe((res: HttpResponse<INote[]>) => (this.notes = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInNotes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: INote): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInNotes(): void {
    this.eventSubscriber = this.eventManager.subscribe('noteListModification', () => this.loadAll());
  }

  delete(note: INote): void {
    const modalRef = this.modalService.open(NoteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.note = note;
  }
}
