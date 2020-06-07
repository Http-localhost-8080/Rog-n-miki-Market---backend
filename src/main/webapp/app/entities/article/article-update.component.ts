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
import { Etat, IEtat } from 'app/shared/model/etat.model';
import { EtatService } from 'app/entities/etat/etat.service';
import { IUser, User } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IPannier } from 'app/shared/model/pannier.model';
import { PannierService } from 'app/entities/pannier/pannier.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';
import { JhiDataUtils, JhiEventManager, JhiEventWithContent, JhiFileLoadError } from 'ng-jhipster';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IPicture, Picture } from 'app/shared/model/picture.model';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { ArticleDto } from 'app/shared/dto/article-dto.model';
type SelectableEntity = ICity | IEtat | IUser | IPannier | ICategory;

type SelectableManyToManyEntity = ICity | IEtat | IUser | IPannier;

@Component({
  selector: 'jhi-article-update',
  templateUrl: './article-update.component.html',
  styleUrls: ['./article.scss']
})
export class ArticleUpdateComponent implements OnInit {
  isSaving = false;
  city: ICity[] = [];
  etat: IEtat[] = [];
  users: IUser[] = [];
  panniers: IPannier[] = [];
  categories: ICategory[] = [];
  pictures: IPicture[];
  picture: Picture;
  picture1: Picture;
  picture2: Picture;
  picture3: Picture;
  logerUser: Account;
  user: User;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    description: [],
    price: [null, [Validators.required]],
    city: [[Validators.required]],
    category: [[Validators.required]],
    etat: [[Validators.required]],
    modeAcquisition: [[Validators.required]],
    fraisLivraison: [null, [Validators.required]],
    name: [],
    nameContentType: []
  });

  etatProduit: Etat;

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
    private fb: FormBuilder,
    private accountService: AccountService
  ) {
    this.pictures = new Array<IPicture>();
    this.picture = new Picture();
    this.picture1 = new Picture();
    this.picture2 = new Picture();
    this.picture3 = new Picture();
    this.user = new User();
    this.logerUser = new Account();
    this.etatProduit = new Etat();
  }

  ngOnInit(): void {
    this.accountService.identity(true).subscribe(
      data => {
        console.log(data);
        this.logerUser = data;
        console.log(this.logerUser);
        this.user.id = this.logerUser.id;
        this.user.login = this.logerUser.login;
      },
      error => {
        console.log(error);
      }
    );
    this.activatedRoute.data.subscribe(({ article }) => {
      this.updateForm(article);

      this.cityService.query().subscribe((res: HttpResponse<ICity[]>) => (this.city = res.body || []));

      this.etatService.query().subscribe((res: HttpResponse<IEtat[]>) => (this.etat = res.body || []));

      // this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

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
      cities: article.city,
      etats: article.etat,
      user: article.user,
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
    console.log(article);
    if (article.id !== undefined) {
      this.subscribeToSaveResponse(this.articleService.update(article));
    } else {
      this.subscribeToSaveResponse(this.articleService.create(article));
    }
  }

  private createFromForm(): ArticleDto {
    this.etatProduit.etatArticle = this.editForm.get(['etat'])!.value;
    this.etatProduit.available = true;
    this.etatProduit.modeAcquisition = this.editForm.get(['modeAcquisition'])!.value;
    this.etatProduit.fraisLivraison = this.editForm.get(['fraisLivraison'])!.value;
    return {
      ...new ArticleDto(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      price: this.editForm.get(['price'])!.value,
      // createAt: new Date().toLocaleDateString(),
      city: this.editForm.get(['city'])!.value,
      pictures: this.pictures,
      etat: this.etatProduit,
      user: this.user,
      category: this.editForm.get(['category'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ArticleDto>>): void {
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
    console.log(event);
    console.log(field);
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(
      () => {
        // this.pictures[this.pictures.length] = this.picture;
        if (this.pictures.length === 0) {
          this.picture1.name = this.editForm.get('name')!.value;
          this.picture1.nameContentType = this.editForm.get('nameContentType')!.value;
          this.pictures.push(this.picture1);
        } else if (this.pictures.length === 1) {
          this.picture2.name = this.editForm.get('name')!.value;
          this.picture2.nameContentType = this.editForm.get('nameContentType')!.value;
          this.pictures.push(this.picture2);
        } else if (this.pictures.length === 2) {
          this.picture3.name = this.editForm.get('name')!.value;
          this.picture3.nameContentType = this.editForm.get('nameContentType')!.value;
          this.pictures.push(this.picture3);
        }

        // this.pictures.push(this.picture);
      },
      (err: JhiFileLoadError) => {
        this.eventManager.broadcast(
          new JhiEventWithContent<AlertError>('shoppingApp.error', { message: err.message })
        );
      }
    );
  }

  onDeletePicture(i: number): void {
    console.log('index', i);
    this.pictures.splice(i, 1);
    console.log(this.pictures);
  }
}
