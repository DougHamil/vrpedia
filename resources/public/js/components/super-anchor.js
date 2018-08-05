AFRAME.registerComponent('super-anchor', {
    dependencies:['geometry'],
    schema: {
        type:'string',
        default:'0 0 0'
    },
    init:function(){
        var el = this.el;
        var self = this;
        self.emittedEvent = false;
        self.rawPosition = {};
        AFRAME.utils.extend(self.rawPosition, el.getAttribute('position'));
        self.lastPosition = self.rawPosition;
        this.el.addEventListener('componentchanged', function(event){
            if(event.detail.name === 'position' && event.detail.newData) {
                var newPosition = event.target.getAttribute('position');
                if(!AFRAME.utils.deepEqual(self.assignedPosition, newPosition))
                {
                    AFRAME.utils.extend(self.rawPosition, event.detail.newData);
                    self.applyTransformation();
                }
            }
            else if(event.detail.name === 'geometry') {
                var width = event.target.getAttribute('width');
                var height = event.target.getAttribute('height');
                self.applyTransformation(width, height);
            }
        });
        this.applyTransformation();
    },
    applyTransformation: function(width, height) {
        var el = this.el;
        var geometry = el.getAttribute('geometry');
        width = width || geometry.width || el.getAttribute('width') || 1.0;
        height = height || geometry.height || el.getAttribute('height') || 1.0;
        var anchor = AFRAME.utils.coordinates.parse(this.data);
        var position = {
            x:this.rawPosition.x + (anchor.x * width),
            y:this.rawPosition.y + (anchor.y * height),
            z:this.rawPosition.z
        };
        if(!AFRAME.utils.deepEqual(position, self.lastPosition))
        {
            this.lastPosition = position;
            this.assignedPosition = position;
            el.setAttribute('position', position);
        }
    }
});
