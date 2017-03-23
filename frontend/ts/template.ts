


declare interface SEvent {
    from: HTMLElement
    to: HTMLElement
    item: HTMLElement
    oldIndex?: number
    newIndex?: number
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

