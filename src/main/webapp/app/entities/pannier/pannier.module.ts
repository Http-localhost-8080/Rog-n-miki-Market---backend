import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ShoppingSharedModule } from 'app/shared/shared.module';
import { PannierComponent } from './pannier.component';
import { PannierDetailComponent } from './pannier-detail.component';
import { PannierUpdateComponent } from './pannier-update.component';
import { PannierDeleteDialogComponent } from './pannier-delete-dialog.component';
import { pannierRoute } from './pannier.route';

@NgModule({
  imports: [ShoppingSharedModule, RouterModule.forChild(pannierRoute)],
  declarations: [PannierComponent, PannierDetailComponent, PannierUpdateComponent, PannierDeleteDialogComponent],
  entryComponents: [PannierDeleteDialogComponent]
})
export class ShoppingPannierModule {}
