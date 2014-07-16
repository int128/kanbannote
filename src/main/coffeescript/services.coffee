services = angular.module 'knServices', ['ngStorage']

services.factory 'Note', ($http, $sessionStorage) ->
  auth = ->
    headers:
      'X-EN-Token': $sessionStorage.token
      'X-EN-Environment': $sessionStorage.environment
  list: ->
    $http.get('/notes', auth())
  get: (guid) ->
    $http.get("/note/#{guid}", auth())
