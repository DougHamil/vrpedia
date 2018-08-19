AFRAME.registerComponent('user-controller', {
    schema:{},
    init:function(){
        window._VRPedia.registerRaycaster(this.el.components.raycaster);
    }
});
