AFRAME.registerComponent('text-button-list', {
    schema:{
        width:{type:'number', default:1.0}
    },
    init:function(){
        this.entries = [];
        this.buttons = [];
    },
    reset:function() {
        for(let i in this.buttons){
            this.el.removeChild(this.buttons[i]);
        }
        this.buttons = [];
    },
    rebuild:function(entries){
        this.reset();
        let self = this;
        for(let i in entries) {
            let entry = entries[i];
            let btn = document.createElement('a-entity');
            btn.setAttribute('text-button', {width:this.data.width, text:entry.text});
            this.el.appendChild(btn);
            btn.addEventListener('click', function() {
                self.el.dispatchEvent(new CustomEvent('entry-selected', {detail: entry}));
            });
            this.buttons.push(btn);
        }
    }
});
