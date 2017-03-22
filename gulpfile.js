const gulp = require('gulp'),
  less = require('gulp-less'),
  csso = require('gulp-csso')


const browserify = require("browserify");
const source = require('vinyl-source-stream');
const watchify = require("watchify");
const tsify = require("tsify");
const gutil = require("gulp-util");
const dist = "src/main/resources/toserve/";
const uglify = require('gulp-uglify');
const sourcemaps = require('gulp-sourcemaps');
const buffer = require('vinyl-buffer');



const watchedBrowserify = watchify(browserify({
    basedir: '.',
    debug: true,
    entries: ['frontend/ts/template.ts'],
    cache: {},
    packageCache: {}
}).plugin(tsify)
.transform('babelify', {
    presets: ['es2015'],
    extensions: ['.ts', '.tsx']
})
);


function bundle() {
    // process.env.NODE_ENV = 'production';
    return watchedBrowserify
        .bundle()
        .pipe(source('bundle.js'))
        .pipe(buffer())
        .pipe(sourcemaps.init({loadMaps: true}))
        // .pipe(uglify())
        .pipe(sourcemaps.write('./'))
        .pipe(gulp.dest(dist + 'js/'));
}

gulp.task("js", bundle);


watchedBrowserify.on("update", bundle);
watchedBrowserify.on("log", gutil.log);




  gulp.task('css', function () {
  return gulp.src('frontend/less/*')
    .pipe(less())
    // .pipe(csso())
    .pipe(gulp.dest('src/main/resources/toserve/css/'))

})

gulp.task('default', ['css'])