import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ShoppingSharedModule } from 'app/shared/shared.module';
import { ShoppingCoreModule } from 'app/core/core.module';
import { ShoppingAppRoutingModule } from './app-routing.module';
import { ShoppingHomeModule } from './home/home.module';
import { ShoppingEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { AProposComponent } from './components/a-propos/a-propos.component';
import { ContactComponent } from './components/contact/contact.component';
import { ArticleComponent } from './components/article/article.component';
import { AddArticleComponent } from './components/add-article/add-article.component';

@NgModule({
  imports: [
    BrowserModule,
    ShoppingSharedModule,
    ShoppingCoreModule,
    ShoppingHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ShoppingEntityModule,
    ShoppingAppRoutingModule
  ],
  declarations: [
    MainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    FooterComponent,
    AProposComponent,
    ContactComponent,
    ArticleComponent,
    AddArticleComponent
  ],
  bootstrap: [MainComponent]
})
export class ShoppingAppModule {}
