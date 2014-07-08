gulp = require('gulp')
ngmin = require('gulp-ngmin')
uglify = require('gulp-uglify')
csso = require('gulp-csso')
del = require('del')
bower = require('bower')

sources =
  bower:  'bower.json'
  js:     'src/main/assets/**/*.js'
  css:    'src/main/assets/**/*.css'
  static: 'src/main/assets/**/*.!(js|css)'

target = 'src/main/webapp/'

gulp.task 'bower', ->
  bower.commands.install().on 'end', (installed) ->
    gulp.src([
      'bower_components/jquery/dist/jquery.min.js'
      'bower_components/knockout/dist/knockout.js'
      'bower_components/angular/angular.min.js'
      'bower_components/google-diff-match-patch-js/diff_match_patch.js'
      'bower_components/bootstrap/dist/js/bootstrap.min.js'
      'bower_components/bootstrap/dist/css/bootstrap.min.css'
      'bower_components/bootstrap/dist/css/bootstrap-theme.min.css'
      'bower_components/bootstrap/dist/fonts/'
    ]).pipe gulp.dest("#{target}/vendors")

gulp.task 'js', ->
  gulp.src(sources.js)
    .pipe(ngmin())
    .pipe(uglify())
    .pipe gulp.dest(target)

gulp.task 'css', ->
  gulp.src(sources.css)
    .pipe(csso())
    .pipe gulp.dest(target)

gulp.task 'static', ->
  gulp.src(sources.static)
    .pipe gulp.dest(target)

gulp.task 'default', ['clean'], ->
  gulp.start 'bower', 'js', 'css', 'static'

gulp.task 'watch', ->
  target = 'target/webapp/'
  gulp.watch sources.bower,  ['bower']
  gulp.watch sources.js,     ['js']
  gulp.watch sources.css,    ['css']
  gulp.watch sources.static, ['static']

gulp.task 'clean', (cb) -> del target, cb
