(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
'use strict';

window.initWidget = function (item) {
    var containerStr = '\n        <div class="panel-heading">\n            <button class="btn btn-primary btn-sm edit-button">\n               <span class="glyphicon glyphicon-cog"></span>\n            </button>\n            <button class="btn btn-primary btn-sm close-button">\n               <span class="glyphicon glyphicon-remove"></span>\n            </button>\n        </div>\n        <div class="panel-body">\n            <div id="target"></div>\n        </div>\n        ';
    var children = $(item).children().get(0);
    $(item).removeAttr('id').addClass('panel panel-primary widget').html(containerStr).find('.close-button').on('click', function () {
        item.remove();
    });
    if (children) $(item).find('.panel-body').append(children);
};
window.createSpaces = function () {
    $('.space').filter(function () {
        return !$(this).hasClass('space-init');
    }).addClass('space-init').each(function () {
        createSpace(this);
    });
};
window.createSpace = function (item) {
    Sortable.create(item, {
        group: {
            name: 'space',
            put: ['editor', 'space']
        },
        animation: 100,
        ghostClass: "ghost",
        // delay: 10,
        onAdd: function onAdd(event) {
            if (event.from.id == 'editor-panel') {
                var _item = event.item;
                initWidget(_item);
                loadHtml($('#widget').attr('data-xx-w'));
                //  createSpaces()
            }
        },
        onUpdate: function onUpdate(event) {
            var ids = [];
            $(event.to).children().each(function () {
                ids.push(this.id);
            });
            // sort(event.to.id, ids)
        },
        onStart: function onStart(event) {
            $(event.item).addClass('dragging');
        },
        onEnd: function onEnd(event) {
            $(event.item).removeClass('dragging');
        },
        draggable: '.widget'
    });
};
(function (fn) {
    if (document.readyState != 'loading') fn();else document.addEventListener('DOMContentLoaded', fn);
})(function () {
    Sortable.create(document.getElementById('editor-panel'), {
        group: { name: 'editor', pull: 'clone' },
        animation: 100,
        ghostClass: "ghost"
    });
    createSpaces();
    $('#containers').on('change', function () {
        $('#widget').attr('data-xx-w', this.value);
    });
    var spaces = [];
    $('#calculate').on('click', function () {
        calculate();
        // console.log(JSON.stringify(spaces))
        save(spaces);
        spaces = [];
    });
    init();
    function calculate() {
        var top = $('.space-top');
        function setTop(t) {
            var id = t.attr('data-xx-sid');
            var container = t.attr('data-xx-c');
            var content = [];
            var level = 0;
            $('[data-xx-cid]', t.get(0)).each(function () {
                var element = this;
                content.push({
                    cid: $(element).attr('data-xx-cid'),
                    ctype: $(element).attr('data-xx-container'),
                    isNew: $(element).hasClass('xx-new')
                });
            });
            spaces.push({ id: id, container: container, content: content, level: level });
        }
        function findLevel(t, l) {
            t.addClass('space-in-work').find('.space:not(.space:not(.space-in-work) .space)').each(function () {
                var id = $(this).attr('data-xx-sid');
                var container = $(this).attr('data-xx-c');
                var content = [];
                var level = l + 1;
                $('[data-xx-cid]', this).each(function () {
                    var element = this;
                    content.push({
                        cid: $(element).attr('data-xx-cid'),
                        ctype: $(element).attr('data-xx-container'),
                        isNew: $(element).hasClass('xx-new')
                    });
                });
                spaces.push({ id: id, container: container, content: content, level: level });
                findLevel($(this), level);
            });
            t.removeClass('space-in-work');
        }
        setTop(top);
        findLevel(top, 0);
    }
    function init() {
        $('[data-xx-container]').each(function () {
            initWidget(this);
            $('#target').remove();
        });
    }
});

},{}]},{},[1])

//# sourceMappingURL=page-containers.js.map
