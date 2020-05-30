import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPannier } from 'app/shared/model/pannier.model';

@Component({
  selector: 'jhi-pannier-detail',
  templateUrl: './pannier-detail.component.html'
})
export class PannierDetailComponent implements OnInit {
  pannier: IPannier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pannier }) => (this.pannier = pannier));
  }

  previousState(): void {
    window.history.back();
  }
}
