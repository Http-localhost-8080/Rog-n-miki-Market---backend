import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEtat } from 'app/shared/model/etat.model';

@Component({
  selector: 'jhi-etat-detail',
  templateUrl: './etat-detail.component.html'
})
export class EtatDetailComponent implements OnInit {
  etat: IEtat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etat }) => (this.etat = etat));
  }

  previousState(): void {
    window.history.back();
  }
}
