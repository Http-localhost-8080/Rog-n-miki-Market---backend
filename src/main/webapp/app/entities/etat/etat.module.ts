import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ShoppingSharedModule } from 'app/shared/shared.module';
import { EtatComponent } from './etat.component';
import { EtatDetailComponent } from './etat-detail.component';
import { EtatUpdateComponent } from './etat-update.component';
import { EtatDeleteDialogComponent } from './etat-delete-dialog.component';
import { etatRoute } from './etat.route';

@NgModule({
  imports: [ShoppingSharedModule, RouterModule.forChild(etatRoute)],
  declarations: [EtatComponent, EtatDetailComponent, EtatUpdateComponent, EtatDeleteDialogComponent],
  entryComponents: [EtatDeleteDialogComponent]
})
export class ShoppingEtatModule {}
