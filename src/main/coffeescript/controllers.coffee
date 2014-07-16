controllers = angular.module 'knControllers', ['ngStorage']

controllers.controller 'LoginController', ($scope, $location, $sessionStorage) ->
  $scope.$storage = $sessionStorage
  $scope.loginWithToken = (environment) ->
    $location.path 'notes'

controllers.controller 'NotesController', ($scope, $http, $sessionStorage) ->
  auth = headers:
    'X-EN-Token': $sessionStorage.token
    'X-EN-Environment': $sessionStorage.environment
  $http.get('/notes', auth).success (data) ->
    $scope.notes = data.notes

controllers.controller 'NoteController', ($scope, $routeParams, $http, $sessionStorage) ->
  auth = headers:
    'X-EN-Token': $sessionStorage.token
    'X-EN-Environment': $sessionStorage.environment
  $http.get("/note/#{$routeParams.guid}", auth).success (data) ->
    $scope.note = data.note
