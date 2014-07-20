app = angular.module 'knApp', ['ngRoute', 'angular-loading-bar', 'knControllers', 'knServices']

app.config ($routeProvider) ->
  $routeProvider
    .when '/notes',
      templateUrl: 'notes.html'
      controller: 'NotesController'
    .when '/note/:guid',
      templateUrl: 'note.html'
      controller: 'NoteController'
    .when '/note/:guid/edit',
      templateUrl: 'note-edit.html'
      controller: 'NoteEditController'
    .otherwise
      templateUrl: 'login.html'
      controller: 'LoginController'

app.filter 'renderNoteContent', ($sce) ->
  (note) -> $sce.trustAsHtml note?.content

app.filter 'orElse', ->
  (value, otherwise) -> value ? otherwise
