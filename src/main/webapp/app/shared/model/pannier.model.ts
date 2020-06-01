import { IArticle } from 'app/shared/model/article.model';
import { IUser } from 'app/core/user/user.model';

export interface IPannier {
  id?: number;
  quantite?: number;
  priceTotal?: number;
  articles?: IArticle[];
  user?: IUser;
}

export class Pannier implements IPannier {
  constructor(
    public id?: number,
    public quantite?: number,
    public priceTotal?: number,
    public articles?: IArticle[],
    public user?: IUser
  ) {}
}
