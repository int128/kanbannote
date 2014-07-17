services = angular.module 'knServices', ['ngStorage']

services.factory 'Note', ($http, $localStorage) ->
  authStorage = $localStorage
  auth = ->
    headers:
      'X-EN-Token': authStorage.token
      'X-EN-Environment': authStorage.environment
  authStorage: authStorage
  list: ->
    $http.get('/notes', auth())
  get: (guid) ->
    $http.get("/note/#{guid}", auth())
