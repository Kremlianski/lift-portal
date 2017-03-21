const gulp = require('gulp'),
  less = require('gulp-less'),
  csso = require('gulp-csso')


  gulp.task('css', function () {
  return gulp.src('frontend/less/*')
    .pipe(less())
    // .pipe(csso())
    .pipe(gulp.dest('src/main/resources/toserve/css/'))

})

gulp.task('default', ['css'])