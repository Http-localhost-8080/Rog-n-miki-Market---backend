import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEtat } from 'app/shared/model/etat.model';
import { EtatService } from './etat.service';

@Component({
  templateUrl: './etat-delete-dialog.component.html'
})
export class EtatDeleteDialogComponent {
  etat?: IEtat;

  constructor(protected etatService: EtatService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.etatService.delete(id).subscribe(() => {
      this.eventManager.broadcast('etatListModification');
      this.activeModal.close();
    });
  }
}
