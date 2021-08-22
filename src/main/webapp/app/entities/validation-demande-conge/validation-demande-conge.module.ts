import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DemandeCongeComponent } from './list/validation-demande-conge.component';
import { DemandeCongeDetailComponent } from './detail/validation-demande-conge-detail.component';
import { DemandeCongeUpdateComponent } from './update/validation-demande-conge-update.component';
import { DemandeCongeDeleteDialogComponent } from './delete/validation-demande-conge-delete-dialog.component';
import { DemandeCongeRoutingModule } from './route/validation-demande-conge-routing.module';

@NgModule({
  imports: [SharedModule, DemandeCongeRoutingModule],
  declarations: [DemandeCongeComponent, DemandeCongeDetailComponent, DemandeCongeUpdateComponent, DemandeCongeDeleteDialogComponent],
  entryComponents: [DemandeCongeDeleteDialogComponent],
})
export class DemandeCongeModule {}
