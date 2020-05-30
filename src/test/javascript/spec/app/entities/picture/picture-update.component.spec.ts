import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ShoppingTestModule } from '../../../test.module';
import { PictureUpdateComponent } from 'app/entities/picture/picture-update.component';
import { PictureService } from 'app/entities/picture/picture.service';
import { Picture } from 'app/shared/model/picture.model';

describe('Component Tests', () => {
  describe('Picture Management Update Component', () => {
    let comp: PictureUpdateComponent;
    let fixture: ComponentFixture<PictureUpdateComponent>;
    let service: PictureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingTestModule],
        declarations: [PictureUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(PictureUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PictureUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PictureService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Picture(123);
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
        const entity = new Picture();
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
