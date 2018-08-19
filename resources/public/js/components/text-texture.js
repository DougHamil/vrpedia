AFRAME.registerComponent('text-texture', {
    schema: {
        text: {type:'string'},
        font: {type:'string', default:'roboto'},
        fontSize: {type:'number', default:32},
        width: {type:'number', default:Infinity},
        textAlign:{type:'string', default:'left'},
        pixelsPerUnit: {type:'number', default:200}
    },
    init:function() {
        this.texture = new THREE.TextTexture({
            text:this.data.text,
            fontFamily: this.data.font,
            fontSize:this.data.fontSize,
            textAlign:this.data.textAlign,
            fillStyle:'white',
            strokeStyle:'white'
        });

        this.geometry = new THREE.PlaneGeometry(1, 1, 1 );
        let material = new THREE.MeshBasicMaterial({
            color:0xFFFFFF,
            side:THREE.DoubleSide,
            map:this.texture,
            transparent:true,
        });
        this.mesh = new THREE.Mesh(this.geometry, material);

        this.el.setObject3D('mesh', this.mesh);
        this.resize();
    },
    splitLines:function(text, maxWidth) {
        if(this.data.width === Infinity)
            return text;

        let ctx = this.texture.image.getContext('2d');
        let words = text.split(' ');
        var line = '';
        var lines = '';
        for(let i = 0; i < words.length; i++) {
            let lastLine = line;
            if(i === 0)
                line = words[i];
            else
                line += ' ' + words[i];
            let width = ctx.measureText(line).width;
            if(width > maxWidth) {
                lines += '\n'+lastLine;
                line = words[i];
            }
        }
        lines += '\n' + line;

        return lines;
    },
    resize:function() {
        let width = this.data.width;
        let height = width * ( 1.0 / this.texture.imageAspect);
        this.mesh.scale.set(width, height, 1);
        this.mesh.position.set(width/2.0, -height/2.0, 0);
        window._VRPedia.forceRaycasterRefresh();
    },
    trimText:function(text) {
        return text.replace(/\u0000/gm, '').trim();
    },
    update:function() {
        let pixelWidth = this.data.width * this.data.pixelsPerUnit;
        let splitText =  this.splitLines(this.trimText(this.data.text), pixelWidth);
        this.texture.text = splitText;
        this.texture.redraw();
        this.resize();
    }
});
