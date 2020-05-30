import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ShoppingTestModule } from '../../../test.module';
import { EtatUpdateComponent } from 'app/entities/etat/etat-update.component';
import { EtatService } from 'app/entities/etat/etat.service';
import { Etat } from 'app/shared/model/etat.model';

describe('Component Tests', () => {
  describe('Etat Management Update Component', () => {
    let comp: EtatUpdateComponent;
    let fixture: ComponentFixture<EtatUpdateComponent>;
    let service: EtatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingTestModule],
        declarations: [EtatUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EtatUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EtatUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EtatService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Etat(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Etat();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
