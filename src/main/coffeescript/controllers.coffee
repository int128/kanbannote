controllers = angular.module 'knControllers', []

controllers.controller 'LoginController', ($scope, $location) ->
  $scope.loginWithToken = (environment) ->
    $location.path 'notes'

controllers.controller 'NotesController', ($scope, $http) ->
  $scope.query = ->
    $http
      .get('/notes', headers: ('X-EN-Token': $scope.token))
      .success (data) ->
         $scope.notes = data.notes
