import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeConge } from '../validation-demande-conge.model';
import { DemandeCongeService } from '../service/validation-demande-conge.service';

@Component({
  templateUrl: './validation-demande-conge-delete-dialog.component.html',
})
export class DemandeCongeDeleteDialogComponent {
  demandeConge?: IDemandeConge;

  constructor(protected demandeCongeService: DemandeCongeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demandeCongeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
