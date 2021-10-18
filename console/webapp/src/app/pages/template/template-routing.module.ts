import { Routes, RouterModule } from '@angular/router';
import { TemplateComponent } from './template.component';
import { AuthGuard } from 'src/app/auth/auth.guard';
import { NgModule } from '@angular/core';
import { ItemTextComponent } from './item.text.component';

const routes: Routes = [
  { path: ':id/text', component: ItemTextComponent, canActivate: [AuthGuard] },
  { path: ':id', component : TemplateComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/templates', pathMatch: 'full'},
  { path: '**', redirectTo: '/templates', pathMatch: 'full'}
 ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TemplateRoutingModule {
}
