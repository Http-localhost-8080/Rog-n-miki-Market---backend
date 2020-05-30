import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShoppingTestModule } from '../../../test.module';
import { NoteDetailComponent } from 'app/entities/note/note-detail.component';
import { Note } from 'app/shared/model/note.model';

describe('Component Tests', () => {
  describe('Note Management Detail Component', () => {
    let comp: NoteDetailComponent;
    let fixture: ComponentFixture<NoteDetailComponent>;
    const route = ({ data: of({ note: new Note(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ShoppingTestModule],
        declarations: [NoteDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(NoteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NoteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load note on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.note).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
