import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SalarieComponent } from './list/salarie.component';
import { SalarieDetailComponent } from './detail/salarie-detail.component';
import { SalarieUpdateComponent } from './update/salarie-update.component';
import { SalarieDeleteDialogComponent } from './delete/salarie-delete-dialog.component';
import { SalarieRoutingModule } from './route/salarie-routing.module';

@NgModule({
  imports: [SharedModule, SalarieRoutingModule],
  declarations: [SalarieComponent, SalarieDetailComponent, SalarieUpdateComponent, SalarieDeleteDialogComponent],
  entryComponents: [SalarieDeleteDialogComponent],
})
export class SalarieModule {}
