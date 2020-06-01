import { IArticle } from 'app/shared/model/article.model';
import { IUser } from 'app/core/user/user.model';

export interface INote {
  id?: number;
  note?: number;
  article?: IArticle;
  user?: IUser;
}

export class Note implements INote {
  constructor(public id?: number, public note?: number, public article?: IArticle, public user?: IUser) {}
}
