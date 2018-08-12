AFRAME.registerComponent('user-controller', {
    schema:{},
    init:function(){
        this.el.sceneEl.systems['vrpedia'].registerRaycaster(this.el.components.raycaster);
        this.el.addEventListener('raycaster-intersection', function() {
        });
    }
});
