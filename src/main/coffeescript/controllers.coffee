controllers = angular.module 'knControllers', ['ngStorage']

controllers.controller 'LoginController', ($scope, $location, authStorage) ->
  $scope.$storage = authStorage
  $scope.loginWithToken = (environment) ->
    $location.path 'notes'

controllers.controller 'NotesController', ($scope, Note) ->
  Note.list().success (notes) ->
    $scope.notes = notes

controllers.controller 'NoteController', ($scope, $routeParams, Note) ->
  $scope.onFileSelect = ($files) ->
    Note.addResources $routeParams.guid, $files
  Note.get($routeParams.guid).success (note) ->
    $scope.note = note

controllers.controller 'NoteEditController', ($scope, $routeParams, Note) ->
  Note.get($routeParams.guid).success (note) ->
    $scope.note = note
