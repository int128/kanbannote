controllers = angular.module 'knControllers', []

controllers.controller 'NoteListController', ($scope, $http) ->
  $scope.query = ->
    $http
      .get('/notes', headers: ('X-EN-Token': $scope.token))
      .success (notes) ->
        $scope.notes = JSON.stringify notes, ' '
