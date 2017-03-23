


declare interface SEvent {
    from: HTMLElement
    to: HTMLElement
    item: HTMLElement
    oldIndex?: number
    newIndex?: number
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


//moving from one space to another
function move (from: string, to: string, item_id: string, ids: string[]) {

}



(function(fn:()=>void) {
  if (document.readyState != 'loading') fn()
  else document.addEventListener('DOMContentLoaded', fn)
})(
    function() {

        const containerStr: string = `
        <div class="panel-heading">
            <h3 class="panel-title">Widget</h3>
        </div>
        <div class="panel-body">
            <div class="space"></div>
        </div>
        `

        Sortable.create(document.getElementById('editor-panel'), {
            group: {name:'editor', pull: 'clone'},
            animation: 100
        })

        const items = document.getElementsByClassName('space')

        let groups: string[] = ['editor']

        for(let i = 0; i < items.length; i++) {
            createSpace(items.item(i))
        }

        function createSpace(item:Element):void {
            Sortable.create(item, {
                group: {
                    name: 'space',
                    put: ['editor', 'space']
                } ,
                animation: 100,
                // handle: ".my-handle",
                onAdd: function(event: SEvent) {
                    if(event.from.id == 'editor-panel') {
                        const item = event.item
                        $(item)
                        .removeAttr('id')
                        .addClass('panel panel-primary widget')
                        .html(containerStr)

                        const space = $(item).find('.space').get(0)
                        if(space) createSpace(space)
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
        
    }
)

