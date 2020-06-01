import { IArticle } from 'app/shared/model/article.model';

export interface IEtat {
  id?: number;
  available?: boolean;
  modeAcquisition?: string;
  etatArticle?: string;
  fraisLivraison?: number;
  articles?: IArticle[];
}

export class Etat implements IEtat {
  constructor(
    public id?: number,
    public available?: boolean,
    public modeAcquisition?: string,
    public etatArticle?: string,
    public fraisLivraison?: number,
    public articles?: IArticle[]
  ) {
    this.available = this.available || false;
  }
}
