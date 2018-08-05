AFRAME.registerComponent('text-button', {
    schema: {
        text: {type:'string', default:'button'},
        width: {type:'number', default:1}
    },
    init: function() {
        // Frame
        let frame = document.createElement('a-plane');
        frame.setAttribute('width', this.data.width + 0.1);
        frame.setAttribute('height', '0.40');
        frame.setAttribute('position', '0 0 -0.001');
        frame.setAttribute('material', 'color:#333');
        this.el.appendChild(frame);

        // Background
        let bg = document.createElement('a-plane');
        bg.setAttribute('width', this.data.width);
        bg.setAttribute('height', '0.3');
        bg.setAttribute('material', 'color:#FFF');
        this.el.appendChild(bg);

        // Text
        let text = document.createElement('a-text');
        text.setAttribute('value', this.data.text);
        text.setAttribute('align', 'center');
        text.setAttribute('color', '#333');
        text.setAttribute('fit-text-width', {height:0.3});
        text.setAttribute('width', '3');
        this.el.appendChild(text);

        let self = this;
    }
});
