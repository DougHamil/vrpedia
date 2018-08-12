AFRAME.registerComponent('text-button', {
    schema: {
        text: {type:'string', default:'button'},
        width: {type:'number', default:1}
    },
    init: function() {
        // Frame
        this.frame = document.createElement('a-plane');
        this.frame.setAttribute('width', this.data.width + 0.1);
        this.frame.setAttribute('height', '0.40');
        this.frame.setAttribute('position', '0 0 -0.001');
        this.frame.setAttribute('material', 'color:#333');
        this.el.appendChild(this.frame);
        this.frame.classList.add('collidable');

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

        this.el.addEventListener('mouseenter', this.highlight.bind(this));
        this.el.addEventListener('raycaster-intersected', this.highlight.bind(this));
        this.el.addEventListener('mouseleave', this.unhighlight.bind(this));
        this.el.addEventListener('raycaster-intersected-cleared', this.unhighlight.bind(this));
    },
    highlight: function() {
        this.frame.setAttribute('material', 'color:#888');
    },
    unhighlight:function() {
        this.frame.setAttribute('material', 'color:#333');
    }
});
