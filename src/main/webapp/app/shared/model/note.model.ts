import { IArticle } from 'app/shared/model/article.model';

export interface INote {
  id?: number;
  note?: number;
  article?: IArticle;
}

export class Note implements INote {
  constructor(public id?: number, public note?: number, public article?: IArticle) {}
}
