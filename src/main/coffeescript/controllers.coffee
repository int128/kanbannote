controllers = angular.module 'knControllers', ['ngStorage']

controllers.controller 'LoginController', ($scope, $location, $sessionStorage) ->
  $scope.$storage = $sessionStorage
  $scope.loginWithToken = (environment) ->
    $location.path 'notes'

controllers.controller 'NotesController', ($scope, Note) ->
  Note.list().success (data) ->
    $scope.notes = data.notes

controllers.controller 'NoteController', ($scope, $routeParams, Note) ->
  Note.get($routeParams.guid).success (data) ->
    $scope.note = data.note
