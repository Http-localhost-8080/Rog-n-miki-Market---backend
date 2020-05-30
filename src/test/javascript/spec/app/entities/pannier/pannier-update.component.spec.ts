import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ShoppingTestModule } from '../../../test.module';
import { PannierUpdateComponent } from 'app/entities/pannier/pannier-update.component';
import { PannierService } from 'app/entities/pannier/pannier.service';
import { Pannier } from 'app/shared/model/pannier.model';

describe('Component Tests', () => {
  describe('Pannier Management Update Component', () => {
    let comp: PannierUpdateComponent;
    let fixture: ComponentFixture<PannierUpdateComponent>;
    let service: PannierService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingTestModule],
        declarations: [PannierUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PannierUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PannierUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PannierService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Pannier(123);
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
        const entity = new Pannier();
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
