import { Injectable } from '@angular/core';
import { Account } from 'app/core/user/account.model';

@Injectable({
  providedIn: 'root'
})
export class ParameterService {
  private logerUser: Account | null | undefined;
  constructor(public loger?: Account) {
    this.logerUser = loger;
  }

  public setLogerUser(user: Account | null): void {
    this.logerUser = user;
  }

  public getLogerUser(): Account | null | undefined {
    return this.logerUser;
  }
}
