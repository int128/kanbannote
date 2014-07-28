controllers = angular.module 'knControllers', ['ngStorage']

controllers.controller 'AppController', ($scope, $location, AuthService) ->
  $scope.loggedIn = AuthService.isLoggedIn()
  $scope.logout = ->
    AuthService.logout()
    $location.path '/'

controllers.controller 'LoginController', ($scope, $location, AuthService) ->
  if AuthService.isLoggedIn()
    $location.path '/'
  $scope.loginWithToken = (token, environment) ->
    AuthService.login
      token: token
      environment: environment
    $location.path '/'

controllers.controller 'NotesController', ($scope, Note) ->
  Note.list().success (notes) ->
    $scope.notes = notes

controllers.controller 'NoteController', ($scope, $routeParams, Note) ->
  $scope.onFileSelect = ($files) ->
    Note.addResources $routeParams.guid, $files, (note) ->
      $scope.note.resources = note.resources
      $scope.note = $scope.note
  $scope.removeResource = (resource) ->
    resource.removing = true
    Note.removeResource $routeParams.guid, resource.guid, (note) ->
      resource.removing = false
      $scope.note.resources = note.resources
      $scope.note = $scope.note
  Note.get($routeParams.guid).success (note) ->
    $scope.note = note

controllers.controller 'NoteEditController', ($scope, $routeParams, Note) ->
  Note.get($routeParams.guid).success (note) ->
    $scope.note = note
  $scope.save = (note) ->
    Note.save note.guid,
      title: note.title
      content: note.content
