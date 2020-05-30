import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { INote } from 'app/shared/model/note.model';

type EntityResponseType = HttpResponse<INote>;
type EntityArrayResponseType = HttpResponse<INote[]>;

@Injectable({ providedIn: 'root' })
export class NoteService {
  public resourceUrl = SERVER_API_URL + 'api/notes';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/notes';

  constructor(protected http: HttpClient) {}

  create(note: INote): Observable<EntityResponseType> {
    return this.http.post<INote>(this.resourceUrl, note, { observe: 'response' });
  }

  update(note: INote): Observable<EntityResponseType> {
    return this.http.put<INote>(this.resourceUrl, note, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INote>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INote[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INote[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
