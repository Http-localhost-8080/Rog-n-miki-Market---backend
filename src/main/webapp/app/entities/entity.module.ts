import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.ShoppingCategoryModule)
      },
      {
        path: 'note',
        loadChildren: () => import('./note/note.module').then(m => m.ShoppingNoteModule)
      },
      {
        path: 'picture',
        loadChildren: () => import('./picture/picture.module').then(m => m.ShoppingPictureModule)
      },
      {
        path: 'city',
        loadChildren: () => import('./city/city.module').then(m => m.ShoppingCityModule)
      },
      {
        path: 'etat',
        loadChildren: () => import('./etat/etat.module').then(m => m.ShoppingEtatModule)
      },
      {
        path: 'article',
        loadChildren: () => import('./article/article.module').then(m => m.ShoppingArticleModule)
      },
      {
        path: 'pannier',
        loadChildren: () => import('./pannier/pannier.module').then(m => m.ShoppingPannierModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ShoppingEntityModule {}
