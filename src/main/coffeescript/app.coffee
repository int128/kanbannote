app = angular.module 'knApp', ['ngRoute', 'knControllers']

app.config ($routeProvider) ->
  $routeProvider
    .when '/notes',
      templateUrl: 'notes.html'
      controller: 'NotesController'
    .otherwise
      templateUrl: 'login.html'
      controller: 'LoginController'
