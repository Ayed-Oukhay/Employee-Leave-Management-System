import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'salarie',
        data: { pageTitle: 'stageApp.salarie.home.title' },
        loadChildren: () => import('./salarie/salarie.module').then(m => m.SalarieModule),
      },
      {
        path: 'departement',
        data: { pageTitle: 'stageApp.departement.home.title' },
        loadChildren: () => import('./departement/departement.module').then(m => m.DepartementModule),
      },
      {
        path: 'poste',
        data: { pageTitle: 'stageApp.poste.home.title' },
        loadChildren: () => import('./poste/poste.module').then(m => m.PosteModule),
      },
      {
        path: 'contrat',
        data: { pageTitle: 'stageApp.contrat.home.title' },
        loadChildren: () => import('./contrat/contrat.module').then(m => m.ContratModule),
      },
      {
        path: 'type-contrat',
        data: { pageTitle: 'stageApp.typeContrat.home.title' },
        loadChildren: () => import('./type-contrat/type-contrat.module').then(m => m.TypeContratModule),
      },
      {
        path: 'demande-conge',
        data: { pageTitle: 'stageApp.demandeConge.home.title' },
        loadChildren: () => import('./demande-conge/demande-conge.module').then(m => m.DemandeCongeModule),
      },
      {
        path: 'type-conge',
        data: { pageTitle: 'stageApp.typeConge.home.title' },
        loadChildren: () => import('./type-conge/type-conge.module').then(m => m.TypeCongeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
