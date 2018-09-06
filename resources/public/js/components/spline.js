AFRAME.registerComponent('spline', {
    schema: {
        color: {type:'number'},
        width: {type:'number', default:0.01},
        target: {type:'vec3'},
        offset: {type:'vec3'},
        mid: {type:'vec3'}
    },
    init:function() {
        this.isReady = false;
    },
    tock:function() {
        if(this.isReady) return;

        var target3 = new THREE.Vector3(this.data.target.x, this.data.target.y, this.data.target.z);
        var offset3 = new THREE.Vector3(this.data.offset.x, this.data.offset.y, this.data.offset.z);
        var mid3 = new THREE.Vector3(this.data.mid.x, this.data.mid.y, this.data.mid.z);
        target3 = this.el.object3D.worldToLocal(target3);

        var curve = new THREE.QuadraticBezierCurve3(
            offset3,
            mid3,
            target3
        );

        var points = curve.getPoints(10);

        var geometry = new THREE.Geometry();
        points.forEach(p => geometry.vertices.push(p));

        var line = new MeshLine();
        line.setGeometry(geometry);

        var material = new MeshLineMaterial({
            color:new THREE.Color(this.data.color),
            sizeAttenuation:1,
            lineWidth:this.data.width});

        var mesh = new THREE.Mesh(line.geometry, material);

        this.isReady = true;
        this.el.setObject3D('curve', mesh);
    },
    remove: function() {
        this.el.removeObject3D('curve');
    },
    update: function() {
        this.el.removeObject3D('curve');
        this.isReady = false;
    }
});
