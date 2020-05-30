import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ShoppingTestModule } from '../../../test.module';
import { EtatComponent } from 'app/entities/etat/etat.component';
import { EtatService } from 'app/entities/etat/etat.service';
import { Etat } from 'app/shared/model/etat.model';

describe('Component Tests', () => {
  describe('Etat Management Component', () => {
    let comp: EtatComponent;
    let fixture: ComponentFixture<EtatComponent>;
    let service: EtatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingTestModule],
        declarations: [EtatComponent]
      })
        .overrideTemplate(EtatComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EtatComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EtatService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Etat(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.etats && comp.etats[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
