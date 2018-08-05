AFRAME.registerComponent('fit-text-width', {
    dependencies:['text'],
    schema: {
        height:{
            type:'number',
            default:1.0
        },
        paddingX: {
            type:'number',
            default:0.2
        },
        paddingY: {
            type:'number',
            default:0.1
        }
    },
    init:function(){
        var self = this;
        this.el.addEventListener('componentchanged', function(event){
            if(event.detail.name === 'text') {
                self.calculateWidth();
            }
        });
        self.calculateWidth();
    },
    calculateWidth: function() {
        var text = this.el.components.text;
        if(text.currentFont == null) return;
        var geom = text.geometry;

        // Measure line length w/o wrap
        var factor = this.data.height / geom.layout._height;
        var pixelWidth = geom.layout._measure(text.data.value, 0, text.data.value.length, 10000000000.0).width;
        var newWidth = pixelWidth * factor;
        text.data.width = newWidth;
        this.el.setAttribute("text", {width:newWidth, wrapPixels:pixelWidth+5});
        geom = this.el.getAttribute('geometry');
        if(geom) {
            this.el.setAttribute('geometry', {"width":newWidth+this.data.paddingX, "height":this.data.height+this.data.paddingY});
        }
    },
});
