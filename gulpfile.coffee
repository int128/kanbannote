gulp        = require 'gulp'
concat      = require 'gulp-concat'
coffee      = require 'gulp-coffee'
ngAnnotate  = require 'gulp-ng-annotate'
uglify      = require 'gulp-uglify'
less        = require 'gulp-less'
nodemon     = require 'gulp-nodemon'
del         = require 'del'
bower       = require 'bower'

sources =
  bower:  'bower.json'
  coffee: 'src/main/coffeescript/**/*'
  less:   'src/main/less/**/*'
  static: 'src/main/static/**/*'
  mockserverCoffee: 'src/mockserver/coffeescript/**/*'

targets =
  gulp: 'src/main/webapp/'
  appengineDevServer: 'target/webapp/'
  mockserver: 'target/mockserver/'

gulp.task 'default', ['build']

gulp.task 'bower', ->
  bower.commands.install().on 'end', (installed) ->
    gulp.src([
        'bower_components/angular/angular.min.js'
        'bower_components/angular-route/angular-route.min.js'
        'bower_components/angular-loading-bar/build/loading-bar.min.js'
        'bower_components/ngstorage/ngStorage.min.js'
        'bower_components/ng-file-upload/angular-file-upload.min.js'
      ])
      .pipe concat 'lib.js'
      .pipe gulp.dest targets.gulp
      .pipe gulp.dest targets.appengineDevServer
    gulp.src([
        'bower_components/bootstrap/dist/**/*'
        'bower_components/angular-loading-bar/build/loading-bar.min.css'
      ])
      .pipe gulp.dest targets.gulp
      .pipe gulp.dest targets.appengineDevServer

gulp.task 'coffee', ->
  gulp.src sources.coffee
    .pipe coffee()
    .pipe ngAnnotate()
    .pipe uglify()
    .pipe concat 'app.js'
    .pipe gulp.dest targets.gulp
    .pipe gulp.dest targets.appengineDevServer

gulp.task 'less', ->
  gulp.src sources.less
    .pipe less()
    .pipe concat 'app.css'
    .pipe gulp.dest targets.gulp
    .pipe gulp.dest targets.appengineDevServer

gulp.task 'static', ->
  gulp.src sources.static
    .pipe gulp.dest targets.gulp
    .pipe gulp.dest targets.appengineDevServer

gulp.task 'clean', (cb) -> del targets.gulp, cb

gulp.task 'build', ['clean'], ->
  gulp.start 'bower', 'coffee', 'less', 'static'

gulp.task 'watch', ['build'], ->
  gulp.watch sources.bower,  ['bower']
  gulp.watch sources.coffee, ['coffee']
  gulp.watch sources.less,   ['less']
  gulp.watch sources.static, ['static']

gulp.task 'coffee-server', ->
  gulp.src sources.mockserverCoffee
    .pipe coffee()
    .pipe gulp.dest targets.mockserver

gulp.task 'watch-coffee-server', ['coffee-server'], ->
  gulp.watch sources.mockserverCoffee, ['coffee-server']

gulp.task 'server', ['watch-coffee-server', 'watch'], ->
  nodemon
    script: "#{targets.mockserver}/server.js"
    watch:  targets.mockserver
    env:
      PORT: 8888
      DOCROOT: targets.gulp
