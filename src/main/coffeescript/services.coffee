services = angular.module 'knServices', ['ngStorage', 'angularFileUpload']

services.factory 'AuthStorage', ($localStorage) ->
  $localStorage

services.factory 'AuthService', (AuthStorage) ->
  login: (credential) ->
    AuthStorage.token = credential.token
    AuthStorage.environment = credential.environment
  headers: ->
    'X-EN-Token': AuthStorage.token
    'X-EN-Environment': AuthStorage.environment
  isLoggedIn: ->
    if AuthStorage.token and AuthStorage.environment then true else false
  logout: ->
    delete AuthStorage.token
    delete AuthStorage.environment

services.factory 'Note', ($http, $upload, AuthService) ->
  list: ->
    $http.get '/notes', headers: AuthService.headers()
  get: (guid) ->
    $http.get "/note/#{guid}", headers: AuthService.headers()
  save: (guid, data) ->
    $http.post "/note/#{guid}", data, headers: AuthService.headers()
  addResources: (guid, files, onSuccess) ->
    files.forEach (file) ->
      $upload.upload
        url: "/note/#{guid}/resource"
        headers: AuthService.headers()
        file: file
      .success onSuccess
  removeResource: (noteId, resourceId, onSuccess) ->
    $http.delete "/note/#{noteId}/resource/#{resourceId}", headers: AuthService.headers()
      .success onSuccess
