import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DemandeCongeComponent } from './list/demande-conge.component';
import { DemandeCongeDetailComponent } from './detail/demande-conge-detail.component';
import { DemandeCongeUpdateComponent } from './update/demande-conge-update.component';
import { DemandeCongeDeleteDialogComponent } from './delete/demande-conge-delete-dialog.component';
import { DemandeCongeRoutingModule } from './route/demande-conge-routing.module';

@NgModule({
  imports: [SharedModule, DemandeCongeRoutingModule],
  declarations: [DemandeCongeComponent, DemandeCongeDetailComponent, DemandeCongeUpdateComponent, DemandeCongeDeleteDialogComponent],
  entryComponents: [DemandeCongeDeleteDialogComponent],
})
export class DemandeCongeModule {}
