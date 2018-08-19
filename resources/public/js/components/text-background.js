AFRAME.registerComponent('text-background', {
    dependencies:['text', 'geometry'],
    schema:{
        paddingX:{type:'number', default:0},
        paddingY:{type:'number', default:null}
    },
    init:function() {
        this.update();
        this.el.addEventListener('textfontset', this.update.bind(this));
        this.el.addEventListener('componentchanged', this.update.bind(this));
    },
    update:function() {
        let geom = this.el.components['geometry'];
        let text = this.el.components['text'];
        let layout = text.geometry.layout;
        if(!layout) return;

        let textValue = text.data.value;
        let textScale = text.mesh.scale.x;
        let metrics = layout._measure(textValue, 0, textValue.length, 100000.0);
        let width = metrics.width * textScale + (this.data.paddingX * 2.0);
        this.el.setAttribute('geometry', 'width', width);
        if(this.data.paddingY !== null) {
            let height = layout._height * textScale + (this.data.paddingY * 2.0);
            this.el.setAttribute('geometry', 'height', height);
        }
        var geomX = 0.0;
        switch(text.data.align) {
        case 'left':
            geomX = 0.5;
            break;
        case 'right':
            geomX = -0.5;
            break;
        }

        geom.geometry.translate(geomX * width - (this.data.paddingX * geomX * 2.0), 0, -0.0001);
    }
});
