import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPannier } from 'app/shared/model/pannier.model';
import { PannierService } from './pannier.service';

@Component({
  templateUrl: './pannier-delete-dialog.component.html'
})
export class PannierDeleteDialogComponent {
  pannier?: IPannier;

  constructor(protected pannierService: PannierService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pannierService.delete(id).subscribe(() => {
      this.eventManager.broadcast('pannierListModification');
      this.activeModal.close();
    });
  }
}
