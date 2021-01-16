import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {ActorListComponent} from './components/actor-list/actor-list.component';
import {HttpClientModule} from '@angular/common/http';
import {ActorService} from './services/actor.service';
import {RouterModule, Routes} from '@angular/router';
import {MovieListComponent} from './components/movie-list/movie-list.component';
import {MovieCategoryMenuComponent} from './components/movie-category-menu/movie-category-menu.component';

const routes: Routes = [
  {path: 'category/:id', component: MovieListComponent},
  {path: 'category', component: MovieListComponent},
  {path: 'movies', component: MovieListComponent},
  {path: 'category', component: MovieListComponent},
  {path: '', redirectTo: '/movies', pathMatch: 'full'},
  {path: '**', redirectTo: '/movies', pathMatch: 'full'}
];

@NgModule({
  declarations: [
    AppComponent,
    ActorListComponent,
    MovieListComponent,
    MovieCategoryMenuComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    RouterModule
  ],
  providers: [ActorService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
