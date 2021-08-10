import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISalarie } from '../salarie.model';
import { SalarieService } from '../service/salarie.service';

@Component({
  templateUrl: './salarie-delete-dialog.component.html',
})
export class SalarieDeleteDialogComponent {
  salarie?: ISalarie;

  constructor(protected salarieService: SalarieService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.salarieService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
