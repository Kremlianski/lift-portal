

(function(fn:()=>void) {
  if (document.readyState != 'loading') fn()
  else document.addEventListener('DOMContentLoaded', fn)
})(
    function() {
        Sortable.create(document.getElementById('editor-panel'), {
            group: {name:'editor', pull: 'clone'},
            animation: 100
        })

        const items = document.getElementsByClassName('space')

        let groups: string[] = ['editor']

        for(let i = 0; i < items.length; i++) {
            Sortable.create(items.item(i), {
                group: {
                    name: 'space',
                    put: ['editor', 'space']
                } ,
                animation: 100
                
            })
        }
        
    }
)

