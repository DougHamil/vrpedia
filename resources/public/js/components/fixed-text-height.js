AFRAME.registerComponent('fixed-text-height', {
    dependencies: ['text'],
    schema:{
        height: {type:'number'}
    },
    init:function() {
        this.el.addEventListener('componentchanged', (event) => {
            console.log(event);
            if(event.detail.name === 'text') {
                this.updateWidth();
            }
        });
        this.el.addEventListener('textfontset', (evt) => {
            this.updateWidth();
        });
        this.updateWidth();
    },
    updateWidth:function() {
        return;
        let text = this.el.components.text;
        if(!text || !text.currentFont) return;
        let font = text.currentFont;
        // set text width based on height/width ratio

        // h = w / rw
        // w = h * rw
        let width = this.data.height * (0.5 * font.widthFactor);
        this.el.setAttribute('text', 'width', width);

        if(this.el.components.geometry) {
            console.log(text);
            let layout = text.geometry.layout;
            let metrics = layout._measure(text.data.value, 0, text.data.value.length, 100000.0);
            console.log(text.data.value);
            console.log(text.geometry.boundingSphere.radius);
            console.log(width);
            console.log(text.mesh.scale.x);
            console.log(metrics.width);
            this.el.setAttribute('geometry', 'width', metrics.width * text.mesh.scale.x);
        }
    }
});
