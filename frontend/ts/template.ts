
declare interface SEvent {
    from: HTMLElement
    to: HTMLElement
    item: HTMLElement
    oldIndex?: number
    newIndex?: number
}

declare function ajaxRemove(param:JSON):void

declare function createSpace(item: Element): void

declare function createSpaces():void

declare function loadHtml(id: string):void


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
            onAdd: function(event: SEvent) {
                if(event.from.id == 'editor-panel') {
                    const item = event.item
                    const id = $(item).parent().attr('id')
                    $(item)
                    .removeAttr('id')
                    .addClass('panel panel-primary widget')
                    .html(containerStr)
                    .find('.close-button')
                    .on('click', function(){
                        item.remove()
                    })

                loadHtml(id)
                 createSpaces()
                }
            },
            onUpdate: function(event: SEvent) {
                const ids: string[] = []
                $(event.to).children().each(
                    function(){
                        ids.push(this.id)
                    })
                sort(event.to.id, ids)
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






// sorting within one space
function sort(space:string, ids: string[]) {
    console.log(space, " ", ids)
}


//adding from editor
function add (space: string, item_id: string, ids: string[]) {

}

//removing from template
function remove (space: string, item_id: string, ids: string[]) {

}

   
(<any> window).ajaxSuccess = function(data: any){
    console.log(data)

};


(<any> window).ajaxError = function(){
    
};


//moving from one space to another
function move (from: string, to: string, item_id: string, ids: string[]) {

}

(function(fn:()=>void) {
  if (document.readyState != 'loading') fn()
  else document.addEventListener('DOMContentLoaded', fn)
})(
    function() {
        
        Sortable.create(document.getElementById('editor-panel'), {
            group: {name:'editor', pull: 'clone'},
            animation: 100
        })

        createSpaces()

        
        
    }
)

