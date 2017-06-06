
//declarations
declare interface SEvent {
    from: HTMLElement
    to: HTMLElement
    item: HTMLElement
    oldIndex?: number
    newIndex?: number
}


interface SpaceI {
    id: string
    container?: string
    content?: Item[]
    level: number

}

interface Item {
    ctype?: string
    cid?: string
    wid?: string
    wtype?: string
    isNew?: boolean
}

declare function initWidget(item: Element): void

declare function createSpace(item: Element): void

declare function createSpaces():void

declare function initContainer(item: Element): void

declare function initWidget(item: Element): void

declare function loadContainer(id: string):void

declare function loadWidget(id: string):void

declare function save(containers: SpaceI[]):void

//global functions

;(<any> window).menuInit = function(id: string):void { 
    //alert(id)
}
;(<any> window).initWidget = function(item: Element):void {

    const containerStr: string = `
        <div class="panel-heading">
            <button class="btn btn-primary btn-sm edit-button">
               <span class="glyphicon glyphicon-cog"></span>
            </button>
            <button class="btn btn-primary btn-sm close-button">
               <span class="glyphicon glyphicon-remove"></span>
            </button>
        </div>
        <div class="panel-body">
            <div id="target"></div>
        </div>
        `

    const children = $(item).children().get(0)
    $(item)
    .removeAttr('id')
    .removeClass('init-widget')
    .addClass('panel panel-primary widget')
    .html(containerStr)
    .find('.close-button')
    .on('click', function(){
        item.remove()
    })

    if(children) $(item).find('.panel-body').append(children)

    $(item).find('.edit-button')
    .on('click', function(){
        const widget = $(item).find('[data-xx-wid]')
        let str = ''
        if(widget.hasClass('xx-new')) {
            str=`&new=1`
        }
        alert(`?id=${widget.attr('data-xx-wid')}${str}`)
        //window.location()
    })
}
;(<any> window).initContainer = function(item: Element):void {

    const containerStr: string = `
        <div class="panel-heading">
            <button class="btn btn-primary btn-sm edit-button">
               <span class="glyphicon glyphicon-cog"></span>
            </button>
            <button class="btn btn-primary btn-sm close-button">
               <span class="glyphicon glyphicon-remove"></span>
            </button>
        </div>
        <div class="panel-body">
            <div id="target"></div>
        </div>
        `

    const children = $(item).children().get(0)
    $(item)
    .removeAttr('id')
    .addClass('panel panel-primary widget')
    .html(containerStr)
    .find('.close-button')
    .on('click', function(){
        item.remove()
    })

    if(children) $(item).find('.panel-body').append(children)

}
;(<any> window).createSpaces = function():void {

    $('.space')
    
    .filter(function(){
        return !$(this).hasClass('space-init')
    })
    .addClass('space-init')
    .each(function(){
        createSpace(this)
    })

}

;(<any> window).createSpace = function(item:Element):void {
  
    Sortable.create(item, {
        group: {
            name: 'space',
            put: ['editor', 'space']
        } ,
        animation: 100,
        ghostClass: "ghost",
        // delay: 10,
        onAdd: function(event: SEvent) {
            if(event.from.id == 'editor-panel') {
                const item = event.item

                if ($(item).attr('data-xx-con')) {
                    initContainer(item)
                    loadContainer($(item).attr('data-xx-con'))

                } else if ($(item).attr('data-xx-w')) {
                    initWidget(item)
                    console.log($(item).attr('data-xx-w'))
                    loadWidget('id=' + $(item).attr('data-xx-w'))

                } else console.log('Error!');
                

                
            //  createSpaces()
            }
        },
        onUpdate: function(event: SEvent) {
            const ids: string[] = []
            $(event.to).children().each(
                function(){
                    ids.push(this.id)
                })
            // sort(event.to.id, ids)
        },
        onStart: function(event: SEvent) {
            $(event.item)
            .addClass('dragging')
        },
        onEnd: function(event: SEvent) {
            $(event.item)
            .removeClass('dragging')
        },
        draggable: '.widget'
    })
    }


//the "main" method
;(function(fn:()=>void) {
  if (document.readyState != 'loading') fn()
  else document.addEventListener('DOMContentLoaded', fn)
})(
    function() {
        Sortable.create(document.getElementById('editor-panel'), {
            group: {name:'editor', pull: 'clone'},
            animation: 100,
            ghostClass: "ghost"
        })

        createSpaces()

        $('#containers').on('change', function(){
            $('#container').attr('data-xx-con', this.value)
        })

        $('#widgets').on('change', function(){
            $('#widget').attr('data-xx-w', this.value)
        })

        let spaces:SpaceI[] = []

        $('#calculate').on('click', function(){
            calculate()
            console.log(JSON.stringify(spaces))
            // save(spaces)
            spaces = []
        })

        init()

        function calculate() {


            const top = $('.space-top')

            function setTop(t:JQuery) {
              const id = t.attr('data-xx-sid')
              const container = t.attr('data-xx-c')
              
              const content: Item[] = []
              const level = 0
              
              $('[data-xx-cid], [data-xx-wid]', t.get(0)).each(function(){
                  const element:Element = this

                  const cid = $(element).attr('data-xx-cid')
                  const ctype = $(element).attr('data-xx-container')
                  const wid = $(element).attr('data-xx-wid')
                  const wtype = $(element).attr('data-xx-widget')

                  content.push({
                    cid: cid,
                    ctype: ctype,
                    wid: wid,
                    wtype: wtype,
                    isNew: $(element).hasClass('xx-new')
                  })
              })

              spaces.push({id, container, content, level})
            }
            function findLevel(t: JQuery, l: number) {
              t.addClass('space-in-work')
              .find('.space:not(.space:not(.space-in-work) .space)')
              .each(function(){
                const id = $(this).attr('data-xx-sid')
                const container = $(this).attr('data-xx-c')
                
                const content: Item[] = []
                const level = l + 1
                
                $('[data-xx-cid], [data-xx-wid]', this).each(function(){
                    
                  const element:Element = this
                  const cid = $(element).attr('data-xx-cid')
                  const ctype = $(element).attr('data-xx-container')
                  const wid = $(element).attr('data-xx-wid')
                  const wtype = $(element).attr('data-xx-widget')

                  content.push({
                    cid: cid,
                    ctype: ctype,
                    wid: wid,
                    wtype: wtype,
                    isNew: $(element).hasClass('xx-new')
                  })
                })

                spaces.push({id, container, content, level})

                findLevel($(this), level)

              })
              t.removeClass('space-in-work')
            }

            setTop(top)
            findLevel(top, 0)

        }

        function init():void {
            $('[data-xx-container]').each(function(){

                initContainer(this)
                $('#target').remove()

            })

        }
        
        
        
    }
)

