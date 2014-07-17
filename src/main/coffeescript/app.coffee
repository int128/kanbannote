app = angular.module 'knApp', ['ngRoute', 'knControllers', 'knServices']

app.config ($routeProvider) ->
  $routeProvider
    .when '/notes',
      templateUrl: 'notes.html'
      controller: 'NotesController'
    .when '/note/:guid',
      templateUrl: 'note.html'
      controller: 'NoteController'
    .otherwise
      templateUrl: 'login.html'
      controller: 'LoginController'

app.filter 'renderNoteContent', ($sce) ->
  (note) -> $sce.trustAsHtml note?.content
