(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
'use strict';

(function (fn) {
    if (document.readyState != 'loading') fn();else document.addEventListener('DOMContentLoaded', fn);
})(function () {
    Sortable.create(document.getElementById('editor-panel'), {
        group: { name: 'editor', pull: 'clone' },
        animation: 100
    });
    var items = document.getElementsByClassName('space');
    var groups = ['editor'];
    for (var i = 0; i < items.length; i++) {
        groups.push('space' + i);
    }
    groups.forEach(function (name, index) {
        Sortable.create(items.item(index), {
            group: {
                name: 'space',
                put: ['editor', 'space']
            },
            animation: 100
        });
    });
});

},{}]},{},[1])

//# sourceMappingURL=bundle.js.map
