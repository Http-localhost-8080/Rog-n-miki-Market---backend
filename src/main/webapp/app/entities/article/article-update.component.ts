import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IArticle, Article } from 'app/shared/model/article.model';
import { ArticleService } from './article.service';
import { ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city/city.service';
import { IEtat } from 'app/shared/model/etat.model';
import { EtatService } from 'app/entities/etat/etat.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IPannier } from 'app/shared/model/pannier.model';
import { PannierService } from 'app/entities/pannier/pannier.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { JhiDataUtils, JhiEventManager, JhiEventWithContent, JhiFileLoadError } from 'ng-jhipster';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IPicture, Picture } from 'app/shared/model/picture.model';

type SelectableEntity = ICity | IEtat | IUser | IPannier | ICategory;

type SelectableManyToManyEntity = ICity | IEtat | IUser | IPannier;

@Component({
  selector: 'jhi-article-update',
  templateUrl: './article-update.component.html'
})
export class ArticleUpdateComponent implements OnInit {
  isSaving = false;
  cities: ICity[] = [];
  etats: IEtat[] = [];
  users: IUser[] = [];
  panniers: IPannier[] = [];
  categories: ICategory[] = [];
  pictures: IPicture[];
  picture: Picture;
  createAtDp: any;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    description: [],
    price: [null, [Validators.required]],
    createAt: [],
    cities: [],
    etats: [],
    users: [],
    panniers: [],
    category: [],
    name: [],
    nameContentType: []
  });

  constructor(
    protected articleService: ArticleService,
    protected cityService: CityService,
    protected etatService: EtatService,
    protected userService: UserService,
    protected pannierService: PannierService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    private fb: FormBuilder
  ) {
    this.pictures = new Array<IPicture>();
    this.picture = new Picture();
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ article }) => {
      this.updateForm(article);

      this.cityService.query().subscribe((res: HttpResponse<ICity[]>) => (this.cities = res.body || []));

      this.etatService.query().subscribe((res: HttpResponse<IEtat[]>) => (this.etats = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.pannierService.query().subscribe((res: HttpResponse<IPannier[]>) => (this.panniers = res.body || []));

      this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
    });
  }

  updateForm(article: IArticle): void {
    this.editForm.patchValue({
      id: article.id,
      title: article.title,
      description: article.description,
      price: article.price,
      createAt: article.createAt,
      cities: article.cities,
      etats: article.etats,
      users: article.users,
      panniers: article.panniers,
      category: article.category
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const article = this.createFromForm();
    if (article.id !== undefined) {
      this.subscribeToSaveResponse(this.articleService.update(article));
    } else {
      this.subscribeToSaveResponse(this.articleService.create(article));
    }
  }

  private createFromForm(): IArticle {
    return {
      ...new Article(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      price: this.editForm.get(['price'])!.value,
      createAt: this.editForm.get(['createAt'])!.value,
      cities: this.editForm.get(['cities'])!.value,
      etats: this.editForm.get(['etats'])!.value,
      users: this.editForm.get(['users'])!.value,
      panniers: this.editForm.get(['panniers'])!.value,
      category: this.editForm.get(['category'])!.value,
      pictures: this.pictures
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: SelectableManyToManyEntity[], option: SelectableManyToManyEntity): SelectableManyToManyEntity {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(
      () => {
        this.picture.name = this.editForm.get('name')!.value;
        this.picture.nameContentType = this.editForm.get('nameContentType')!.value;
        this.pictures.push(this.picture);
        console.log(this.pictures);
      },
      (err: JhiFileLoadError) => {
        this.eventManager.broadcast(
          new JhiEventWithContent<AlertError>('shoppingApp.error', { message: err.message })
        );
      }
    );
  }
}
