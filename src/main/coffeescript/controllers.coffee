controllers = angular.module 'knControllers', ['ngStorage']

controllers.controller 'LoginController', ($scope, $location, Note) ->
  $scope.$storage = Note.authStorage
  $scope.loginWithToken = (environment) ->
    $location.path 'notes'

controllers.controller 'NotesController', ($scope, Note) ->
  Note.list().success (notes) ->
    $scope.notes = notes

controllers.controller 'NoteController', ($scope, $routeParams, Note) ->
  Note.get($routeParams.guid).success (note) ->
    $scope.note = note
  Note.list().success (notes) ->
    $scope.notes = notes

controllers.controller 'NoteEditController', ($scope, $routeParams, Note) ->
  Note.get($routeParams.guid).success (note) ->
    $scope.note = note
  Note.list().success (notes) ->
    $scope.notes = notes
