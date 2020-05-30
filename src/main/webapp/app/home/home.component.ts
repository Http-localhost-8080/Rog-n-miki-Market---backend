import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { IPicture } from 'app/shared/model/picture.model';
import { PictureService } from 'app/entities/picture/picture.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  pictures: any;
  authSubscription?: Subscription;

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private pictureService: PictureService
  ) {}

  ngOnInit(): void {
    this.pictures = new Array<IPicture>();
    this.pictureService.query().subscribe(
      response => {
        console.log(response);
        this.pictures = response.body;
        console.log(this.pictures);
      },
      error => {
        console.log(error);
      }
    );
    console.log(this.pictures);
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.loginModalService.open();
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
