controllers = angular.module 'knControllers', ['ngStorage', 'ngCookies']

controllers.controller 'LoginController', ($scope, $location, $sessionStorage) ->
  $scope.$storage = $sessionStorage
  $scope.loginWithToken = (environment) ->
    $location.path 'notes'

controllers.controller 'NotesController', ($scope, Note) ->
  Note.list().success (notes) ->
    $scope.notes = notes

controllers.controller 'NoteController', ($scope, $routeParams, $cookies, Note) ->
  Note.get($routeParams.guid).success (note) ->
    $cookies.resourcesAccessKey = note.resourcesAccessKey
    $scope.note = note
