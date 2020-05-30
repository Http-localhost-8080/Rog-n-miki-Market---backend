import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPannier, Pannier } from 'app/shared/model/pannier.model';
import { PannierService } from './pannier.service';

@Component({
  selector: 'jhi-pannier-update',
  templateUrl: './pannier-update.component.html'
})
export class PannierUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    quantite: [],
    priceTotal: []
  });

  constructor(protected pannierService: PannierService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pannier }) => {
      this.updateForm(pannier);
    });
  }

  updateForm(pannier: IPannier): void {
    this.editForm.patchValue({
      id: pannier.id,
      quantite: pannier.quantite,
      priceTotal: pannier.priceTotal
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pannier = this.createFromForm();
    if (pannier.id !== undefined) {
      this.subscribeToSaveResponse(this.pannierService.update(pannier));
    } else {
      this.subscribeToSaveResponse(this.pannierService.create(pannier));
    }
  }

  private createFromForm(): IPannier {
    return {
      ...new Pannier(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      priceTotal: this.editForm.get(['priceTotal'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPannier>>): void {
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
