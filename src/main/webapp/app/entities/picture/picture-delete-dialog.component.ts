import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPicture } from 'app/shared/model/picture.model';
import { PictureService } from './picture.service';

@Component({
  templateUrl: './picture-delete-dialog.component.html'
})
export class PictureDeleteDialogComponent {
  picture?: IPicture;

  constructor(protected pictureService: PictureService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pictureService.delete(id).subscribe(() => {
      this.eventManager.broadcast('pictureListModification');
      this.activeModal.close();
    });
  }
}
