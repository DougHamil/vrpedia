AFRAME.registerSystem('vrpedia',{
    init:function(){
        // Elements
        this.relationshipsList = document.querySelector('#relationshipsList');
        this.relatedSubjectsList = document.querySelector('#relatedSubjectsList');
        this.subjectAbstract = document.querySelector('#subjectAbstract');
        this.subjectTitle = document.querySelector('#subjectTitle');
        this.subjectImage = document.querySelector('#subjectImage');

        // Events
        this.relationshipsList.addEventListener('entry-selected', this.onRelationshipSelected.bind(this));
        this.relatedSubjectsList.addEventListener('entry-selected', this.onRelatedSubjectSelected.bind(this));
    },
    loadSubject: function(subjectUri) {
        console.log(encodeURI(subjectUri));
        $.get('/api/entity?uri='+encodeURI(subjectUri)).done(this.rebuild.bind(this));
    },
    rebuild:function(subject) {
        this.subject = subject;
        this.subjectTitle.setAttribute('text', {value:subject.label});
        this.subjectAbstract.setAttribute('value', subject.abstract);
        this.subjectImage.setAttribute('material', {src: 'url('+subject.image+')'});
        this.subjectTitle.components['super-anchor'].applyTransformation();

        let references = this.extractReferences(subject);
        this.relationshipsList.components['text-button-list'].rebuild(references);
        this.rebuildRelatedSubjectsList([]);
    },
    extractReferences:function(subject) {
        let entries = [];
        let references = subject['references'];
        Object.keys(references).forEach(function(key){
            let reference = references[key];
            entries.push({
                id:key,
                text:key
            });
        });
        return entries;
    },
    onRelationshipSelected:function(event) {
        let subjects = this.subject.references[event.detail.id];
        this.rebuildRelatedSubjectsList(subjects);
    },
    onRelatedSubjectSelected:function(event) {
        console.log(event.detail);
        this.loadSubject(event.detail.id);
    },
    rebuildRelatedSubjectsList:function(subjects) {
        let entries = [];
        for(let i in subjects) {
            let subject = subjects[i];
            entries.push({
                id:subject.Concept,
                text:subject.conceptLabel
            });
        }
        this.relatedSubjectsList.components['text-button-list'].rebuild(entries);
        console.log(subjects);
    }
});
