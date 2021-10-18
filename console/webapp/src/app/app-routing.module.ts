import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { AuthGuard } from './auth/auth.guard';

const routes: Routes = [
  { path: 'views', canActivate: [AuthGuard],
    data: {title: 'Views'},
    loadChildren: () => import('./pages/views/views.module')
    .then(m => m.ViewsModule),
  },
  { path: 'templates', canActivate: [AuthGuard],
    data: {title: 'Templates'},
    loadChildren: () => import('./pages/templates/templates.module')
    .then(m => m.TemplatesModule),
   },
  { path: 'template', canActivate: [AuthGuard],
    data: {title: 'Templates | Template', drawerLink: false},
    loadChildren: () => import('./pages/template/template.module')
      .then(m => m.TemplateModule),
  },
  { path: 'qrs', canActivate: [AuthGuard],
    data: {title: 'QR codes'},
    loadChildren: () => import('./pages/qrs/qrs.module')
      .then(m => m.QrsModule),
  },
  { path: 'login', component: LoginComponent,
    data: {headerVisible: false}  },
  //  { path: 'register', component: RegisterComponent },

    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
