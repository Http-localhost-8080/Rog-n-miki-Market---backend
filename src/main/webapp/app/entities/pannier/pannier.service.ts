import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IPannier } from 'app/shared/model/pannier.model';

type EntityResponseType = HttpResponse<IPannier>;
type EntityArrayResponseType = HttpResponse<IPannier[]>;

@Injectable({ providedIn: 'root' })
export class PannierService {
  public resourceUrl = SERVER_API_URL + 'api/panniers';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/panniers';

  constructor(protected http: HttpClient) {}

  create(pannier: IPannier): Observable<EntityResponseType> {
    return this.http.post<IPannier>(this.resourceUrl, pannier, { observe: 'response' });
  }

  update(pannier: IPannier): Observable<EntityResponseType> {
    return this.http.put<IPannier>(this.resourceUrl, pannier, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPannier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPannier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPannier[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
