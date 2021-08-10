import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypeCongeComponent } from './list/type-conge.component';
import { TypeCongeDetailComponent } from './detail/type-conge-detail.component';
import { TypeCongeUpdateComponent } from './update/type-conge-update.component';
import { TypeCongeDeleteDialogComponent } from './delete/type-conge-delete-dialog.component';
import { TypeCongeRoutingModule } from './route/type-conge-routing.module';

@NgModule({
  imports: [SharedModule, TypeCongeRoutingModule],
  declarations: [TypeCongeComponent, TypeCongeDetailComponent, TypeCongeUpdateComponent, TypeCongeDeleteDialogComponent],
  entryComponents: [TypeCongeDeleteDialogComponent],
})
export class TypeCongeModule {}
