(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
'use strict';

(function (fn) {
    if (document.readyState != 'loading') fn();else document.addEventListener('DOMContentLoaded', fn);
})(function () {
    var containerStr = '\n        <div class="panel-heading">\n            <h3 class="panel-title">Widget</h3>\n        </div>\n        <div class="panel-body">\n            <div class="space"></div>\n        </div>\n        ';
    Sortable.create(document.getElementById('editor-panel'), {
        group: { name: 'editor', pull: 'clone' },
        animation: 100
    });
    var items = document.getElementsByClassName('space');
    var groups = ['editor'];
    for (var i = 0; i < items.length; i++) {
        createSpace(items.item(i));
    }
    function createSpace(item) {
        Sortable.create(item, {
            group: {
                name: 'space',
                put: ['editor', 'space']
            },
            animation: 100,
            // handle: ".my-handle",
            onAdd: function onAdd(event) {
                if (event.from.id == 'editor-panel') {
                    var _item = event.item;
                    $(_item).removeAttr('id').addClass('panel panel-primary widget').html(containerStr);
                    var space = $(_item).find('.space').get(0);
                    if (space) createSpace(space);
                }
            },
            onStart: function onStart(event) {
                $(event.item).addClass('dragging');
            },
            onEnd: function onEnd(event) {
                $(event.item).removeClass('dragging');
            },
            draggable: '.widget'
        });
    }
});

},{}]},{},[1])

//# sourceMappingURL=bundle.js.map
