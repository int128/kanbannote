app = angular.module 'knApp', ['ngRoute', 'angular-loading-bar', 'knControllers', 'knServices']

app.config ($routeProvider) ->
  $routeProvider
    .when '/notes',
      templateUrl: 'notes.html'
      controller: 'NotesController'
    .when '/note/:guid',
      templateUrl: 'note.html'
      controller: 'NoteController'
    .when '/note/:guid/edit',
      templateUrl: 'note-edit.html'
      controller: 'NoteEditController'
    .otherwise
      templateUrl: 'login.html'
      controller: 'LoginController'

app.filter 'asHtml', ($sce) ->
  (value) -> $sce.trustAsHtml value

app.filter 'orElse', ->
  (value, otherwise) -> value ? otherwise

app.directive 'ngCkeditor', ($window) ->
  link: (scope, element, attrs) ->
    attrs.$set 'contenteditable', 'true'
    $window.CKEDITOR.disableAutoInline = true
    $window.CKEDITOR.inline element[0]

app.directive 'ngXmlUpdate', ($window) ->
  serializer = new XMLSerializer()
  link: (scope, element, attrs) ->
    element.on 'blur', ->
      scope.$xml = serializer
        .serializeToString element[0].firstChild
        .replace ///xmlns=".+?"///, ''
      scope.$apply ->
        scope.$eval attrs.ngXmlUpdate
