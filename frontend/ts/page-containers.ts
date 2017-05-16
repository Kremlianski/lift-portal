
//declarations
declare interface SEvent {
    from: HTMLElement
    to: HTMLElement
    item: HTMLElement
    oldIndex?: number
    newIndex?: number
}


interface Space {
    id: string
    container?: string
    content?: string[]
    level: number

}

declare function createSpace(item: Element): void

declare function createSpaces():void

declare function loadHtml(id: string):void



//global functions

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

    const containerStr: string = `
        <div class="panel-heading">
            <button class="btn btn-primary btn-sm close-button">
               <span class="glyphicon glyphicon-remove"></span>
            </button>
        </div>
        <div class="panel-body">
            <div id="target"></div>
        </div>
        `
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
        
                    $(item)
                    .removeAttr('id')
                    .addClass('panel panel-primary widget')
                    .html(containerStr)
                    .find('.close-button')
                    .on('click', function(){
                        item.remove()
                    })

                loadHtml($('#widget').attr('data-xx-w'))
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
            $('#widget').attr('data-xx-w', this.value)
        })

        let spaces:Space[] = []

        $('#calculate').on('click', function(){
            calculate()

            console.log(spaces)
            spaces = []
        })

        function calculate() {


            const top = $('.space-top')

            function setTop(t:JQuery) {
              const id = t.attr('data-xx-sid')
              const container = t.attr('data-xx-c')
              
              const content: string[] = []
              const level = 0
              
              $('[data-xx-cid]', t.get(0)).each(function(){
                  
                  content.push($(this).attr('data-xx-cid'))
              })

              spaces.push({id, container, content, level})
            }
            function findLevel(t: JQuery) {
              t.addClass('space-in-work').find('.space:not(.space:not(.space-in-work) .space)').each(function(){
                const id = $(this).attr('data-xx-sid')
                const container = $(this).attr('data-xx-c')
                
                const content: string[] = []
                const level = 0
                
                $('[data-xx-cid]', this).each(function(){
                    
                    content.push($(this).attr('data-xx-cid'))
                })

                spaces.push({id, container, content, level})

                findLevel($(this))

              })
              t.removeClass('space-in-work')
            }
          // need level calculation!
            setTop(top)
            findLevel(top)

            // $('.space').each(function(){
            //     const id = $(this).attr('data-xx-sid')
            //     const container = $(this).attr('data-xx-c')
                
            //     const content: string[] = []
            //     const level = 0
                
            //     $('[data-xx-cid]', this).each(function(){
                   
            //        content.push($(this).attr('data-xx-cid'))
            //     })

            //     spaces.push({id, container, content, level})

            //    }
            // )
        }
        
        
        
    }
)

