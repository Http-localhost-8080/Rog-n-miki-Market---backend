import { IArticle } from 'app/shared/model/article.model';

export interface IEtat {
  id?: number;
  available?: boolean;
  type?: string;
  frais?: number;
  articles?: IArticle[];
}

export class Etat implements IEtat {
  constructor(public id?: number, public available?: boolean, public type?: string, public frais?: number, public articles?: IArticle[]) {
    this.available = this.available || false;
  }
}
