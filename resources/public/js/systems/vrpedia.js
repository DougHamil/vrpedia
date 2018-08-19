window._VRPedia = {
    init:function() {
        // Fields
        this.raycasters = [];
    },
    searchForSubject:function(term) {
        $.get('/api/entity/search?query='+term).done(this.setSubject.bind(this));
    },
    loadSubject:function(subjectUri) {
        $.get('/api/entity?uri='+encodeURI(subjectUri)).done(this.setSubject.bind(this));
    },
    setSubject:function(subject) {
        VRPedia.instance.setSubject(subject);
    },
    registerRaycaster:function(raycaster) {
        this.raycasters.push(raycaster);
    },
    forceRaycasterRefresh:function() {
        for(let i in this.raycasters) {
            let raycaster = this.raycasters[i];
            raycaster.refreshObjects();
        }
    },
};

AFRAME.registerSystem('vrpedia', {
    init:function(){
        window._VRPedia.init();
    }
});

