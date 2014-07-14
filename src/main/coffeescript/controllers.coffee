controllers = angular.module 'knControllers', []

controllers.controller 'NoteListController', ($scope) ->
  $scope.notebooks = 'hoge'
