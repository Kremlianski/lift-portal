
//declarations
declare interface SEvent {
    from: HTMLElement
    to: HTMLElement
    item: HTMLElement
    oldIndex?: number
    newIndex?: number
}


interface SpaceTemplate {
    id: string
    content?: Widget[]

}

interface Widget {
    wtype: string
    wid: string
    isNew?: boolean
}

declare function createSpace(item: Element): void

declare function createSpaces():void

declare function loadHtml(id: string):void
declare function menuInit(id: string):void


declare function save(widgets: Widget[]):void



//global functions

;(<any> window).menuInit = function(id: string):void { 
    //alert(id)
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
                loadHtml('id=' + $('#widget').attr('data-xx-w') + ';b=4')
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

        let spaces:SpaceTemplate[] = []

        $('#calculate').on('click', function(){
            calculate()
            console.log(spaces)
            spaces = []
        })

        function calculate() {
            $('.space').each(function() {
                const id = $(this).attr('data-xx-sid')
                const content: Widget[] = []
                
                $('[data-xx-wid]', this).each(function(){

                    const element:Element = this
                    content.push({ 
                        wtype: $(element).attr('data-xx-widget'),
                        wid: $(element).attr('data-xx-wid'),
                        isNew: $(element).hasClass('xx-new')
                    })
                })

                spaces.push({id,  content})

               }
            )
        }
    }
)

