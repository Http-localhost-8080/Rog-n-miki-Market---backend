import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IEtat } from 'app/shared/model/etat.model';

type EntityResponseType = HttpResponse<IEtat>;
type EntityArrayResponseType = HttpResponse<IEtat[]>;

@Injectable({ providedIn: 'root' })
export class EtatService {
  public resourceUrl = SERVER_API_URL + 'api/etats';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/etats';

  constructor(protected http: HttpClient) {}

  create(etat: IEtat): Observable<EntityResponseType> {
    return this.http.post<IEtat>(this.resourceUrl, etat, { observe: 'response' });
  }

  update(etat: IEtat): Observable<EntityResponseType> {
    return this.http.put<IEtat>(this.resourceUrl, etat, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEtat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtat[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
