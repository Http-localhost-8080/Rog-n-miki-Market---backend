import { IArticle } from 'app/shared/model/article.model';

export interface ICategory {
  id?: number;
  icon?: string;
  title?: string;
  description?: string;
  articles?: IArticle[];
}

export class Category implements ICategory {
  constructor(public id?: number, public icon?: string, public title?: string, public description?: string, public articles?: IArticle[]) {}
}
