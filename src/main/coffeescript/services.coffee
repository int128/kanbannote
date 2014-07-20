services = angular.module 'knServices', ['ngStorage', 'angularFileUpload']

services.factory 'authStorage', ($localStorage) ->
  $localStorage

services.factory 'Note', ($http, $upload, authStorage) ->
  list: ->
    $http.get '/notes',
      headers:
        'X-EN-Token': authStorage.token
        'X-EN-Environment': authStorage.environment
  get: (guid) ->
    $http.get "/note/#{guid}",
      headers:
        'X-EN-Token': authStorage.token
        'X-EN-Environment': authStorage.environment
  addResources: (guid, files) ->
    files.forEach (file) ->
      $upload.upload
        url: "/note/#{guid}/resource"
        headers:
          'X-EN-Token': authStorage.token
          'X-EN-Environment': authStorage.environment
        file: file
