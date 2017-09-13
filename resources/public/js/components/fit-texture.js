AFRAME.registerComponent('fit-texture', {
    dependencies:['geometry', 'material'],
    schema: {
        pixelsPerUnit: {
            type:'number',
            default:300.0
        },
        maxWidth: {
            type:'number',
            default:0
        },
        useFactor: {
            type:'boolean',
            default:false
        }
    },
    init:function(){},
    update:function(){
        if(this.data === false) return;

        var el = this.el;
        var self = this;

        if(self.dimensions) {
            self.applyTransformation()
        } else {
            var textureLoaded = function(e) {
                var w = e.detail.texture.image.videoWidth || e.detail.texture.image.width;
                var h = e.detail.texture.image.videoHeight || e.detail.texture.image.height;

                if(h === 0 || w === 0) return;

                self.dimensions = {w:w, h:h};
                self.applyTransformation();
            }

            el.addEventListener('materialvideoloadeddata', textureLoaded);
            el.addEventListener('materialtextureloaded', textureLoaded);
        }
    },
    applyTransformation: function() {
        var el = this.el;
        var geometry = el.getAttribute('geometry');

        var widthHeightRatio = this.dimensions.h / this.dimensions.w;
        var calculatedWidth = 1.0;

        if(this.data.useFactor) {
            calculatedWidth = this.dimensions.w / this.data.pixelsPerUnit;
        }
        else {
            if(geometry.width) {
                calculatedWidth = geometry.width;
            } else if (geometry.height) {
                calculatedWidth = geometry.height / widthHeightRatio;
            } else {
                calculatedWidth = 1.0;
            }
        }
        if(this.data.maxWidth > 0 && calculatedWidth > this.data.maxWidth)
            calculatedWidth = this.data.maxWidth;

        console.log(this.data);
        //geometry.width = calculatedWidth;
        //geometry.height = calculatedHeight;
        var calculatedHeight = calculatedWidth * widthHeightRatio;
        el.setAttribute('geometry', {width:calculatedWidth, height:calculatedHeight});
    },
    remove: function() {},
    pause: function() {},
    play: function() {}
});
