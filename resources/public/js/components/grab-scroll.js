AFRAME.registerComponent('grab-scroll', {
    schema: {
    },
    init:function() {
        this.initialPosition = null;
        this.isScrolling = false;
        this.el.addEventListener('mousedown', this.startScroll.bind(this));
        this.el.addEventListener('mouseup', this.endScroll.bind(this));
    },
    tick:function() {
        if(this.isScrolling) this.updateScroll();
    },
    resetPosition:function(){
        if(this.initialPosition) this.el.setAttribute('position', this.initialPosition);
    },
    startScroll:function(evt) {
        if(this.initialPosition === null) {
            this.initialPosition = this.el.components.position.data;
        }

        this.raycaster = evt.detail.cursorEl.components.raycaster;
        let intersections = this.raycaster.raycaster.intersectObject(this.el.object3D, true);
        if(intersections !== null && intersections.length > 0) {
            this.scrollStartIntersectionPoint = intersections[0].point;
            this.scrollStartPosition = this.el.components.position.data;
            this.isScrolling = true;
        }
    },
    endScroll:function(evt) {
        this.isScrolling = false;
    },
    updateScroll:function() {
        let intersections = this.raycaster.raycaster.intersectObject(this.el.object3D, true);
        if(intersections !== null && intersections.length > 0) {
            let scrollIntersectionPoint = intersections[0].point;
            let deltaY = scrollIntersectionPoint.y - this.scrollStartIntersectionPoint.y;
            let newPosition = {x: this.scrollStartPosition.x, y:this.scrollStartPosition.y + deltaY, z:this.scrollStartPosition.z};
            this.el.setAttribute('position', newPosition);
        }
    }
});
