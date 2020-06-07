import { INote } from 'app/shared/model/note.model';
import { IPicture } from 'app/shared/model/picture.model';
import { ICity } from 'app/shared/model/city.model';
import { IEtat } from 'app/shared/model/etat.model';
import { IUser } from 'app/core/user/user.model';
import { IPannier } from 'app/shared/model/pannier.model';
import { ICategory } from 'app/shared/model/category.model';

export class ArticleDto {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string,
    public price?: number,
    public createAt?: string,
    public notes?: INote[],
    public pictures?: IPicture[],
    public city?: ICity,
    public etat?: IEtat,
    public user?: IUser,
    public panniers?: IPannier[],
    public category?: ICategory
  ) {}
}
