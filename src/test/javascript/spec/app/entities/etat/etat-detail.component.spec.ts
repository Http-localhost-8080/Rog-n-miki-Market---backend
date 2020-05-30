import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShoppingTestModule } from '../../../test.module';
import { EtatDetailComponent } from 'app/entities/etat/etat-detail.component';
import { Etat } from 'app/shared/model/etat.model';

describe('Component Tests', () => {
  describe('Etat Management Detail Component', () => {
    let comp: EtatDetailComponent;
    let fixture: ComponentFixture<EtatDetailComponent>;
    const route = ({ data: of({ etat: new Etat(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingTestModule],
        declarations: [EtatDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EtatDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EtatDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load etat on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.etat).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
