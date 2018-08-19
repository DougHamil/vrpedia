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
        this.el.addEventListener('componentchanged', (event) => {
            if(event.detail.name === 'text') {
                this.calculateWidth();
            }
        });
        this.calculateWidth();
    },
    calculateWidth: function() {
        var text = this.el.components.text;
        if(text.currentFont == null) return;
        var geom = text.geometry;

        // Measure line length w/o wrap
        var factor = this.data.height / geom.layout._height;
        var pixelWidth = geom.layout._measure(text.data.value, 0, text.data.value.length, 10000000000.0).width;
        var newWidth = pixelWidth * factor;
        console.log(newWidth);
        console.log(pixelWidth);
        geom = this.el.getAttribute('geometry');
        if(geom) {
            this.el.setAttribute('geometry', {width:newWidth+this.data.paddingX, height:this.data.height+this.data.paddingY});
            console.log(this.el.components.material);
            text.updateLayout(text.data);
        }
    },
});
