AFRAME.registerComponent('tree-menu-node', {
    schema:{
        id: {type:'number'},
        label: {type:'string'}
    },
    init: function() {
        const el = document.createElement('a-text');
        el.setAttribute('value', this.data.label);
        el.setAttribute('color', '#444');
        console.log(this);
        this.el.appendChild(el);
    }
});
