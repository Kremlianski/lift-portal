
declare interface SEvent {
    from: HTMLElement
    to: HTMLElement
    item: HTMLElement
    oldIndex?: number
    newIndex?: number
}

declare function ajaxRemove(param:JSON):void

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
        
        const containerStr: string = `
        <div class="panel-heading">
            <button class="btn btn-primary btn-sm close-button">
               <span class="glyphicon glyphicon-remove"></span>
            </button>
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

