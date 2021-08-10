import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeConge } from '../type-conge.model';
import { TypeCongeService } from '../service/type-conge.service';

@Component({
  templateUrl: './type-conge-delete-dialog.component.html',
})
export class TypeCongeDeleteDialogComponent {
  typeConge?: ITypeConge;

  constructor(protected typeCongeService: TypeCongeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeCongeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
