import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShoppingTestModule } from '../../../test.module';
import { PannierDetailComponent } from 'app/entities/pannier/pannier-detail.component';
import { Pannier } from 'app/shared/model/pannier.model';

describe('Component Tests', () => {
  describe('Pannier Management Detail Component', () => {
    let comp: PannierDetailComponent;
    let fixture: ComponentFixture<PannierDetailComponent>;
    const route = ({ data: of({ pannier: new Pannier(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingTestModule],
        declarations: [PannierDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PannierDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PannierDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pannier on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pannier).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
