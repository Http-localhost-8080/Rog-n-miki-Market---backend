import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEtat, Etat } from 'app/shared/model/etat.model';
import { EtatService } from './etat.service';

@Component({
  selector: 'jhi-etat-update',
  templateUrl: './etat-update.component.html'
})
export class EtatUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    available: [null, [Validators.required]],
    type: [null, [Validators.required]],
    frais: []
  });

  constructor(protected etatService: EtatService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etat }) => {
      this.updateForm(etat);
    });
  }

  updateForm(etat: IEtat): void {
    this.editForm.patchValue({
      id: etat.id,
      available: etat.available
      // type: etat.type,
      // frais: etat.frais
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etat = this.createFromForm();
    if (etat.id !== undefined) {
      this.subscribeToSaveResponse(this.etatService.update(etat));
    } else {
      this.subscribeToSaveResponse(this.etatService.create(etat));
    }
  }

  private createFromForm(): IEtat {
    return {
      ...new Etat(),
      id: this.editForm.get(['id'])!.value,
      available: this.editForm.get(['available'])!.value
      // type: this.editForm.get(['type'])!.value,
      // frais: this.editForm.get(['frais'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtat>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
