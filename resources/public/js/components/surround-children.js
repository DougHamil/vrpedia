AFRAME.registerComponent('surround-children', {
    dependencies:['geometry'],
    schema: {
        type:'boolean',
        default:true
    },
    init:function(){
        console.log(this.el.children);
    },
    update:function(){
    },
    applyTransformation: function() {
        var position = this.el.getAttribute('position');
        var minX = position.x;
        var maxX = position.x;
        var minY = position.y;
        var maxY = position.y;
    },
    remove: function() {},
    pause: function() {},
    play: function() {}
});
