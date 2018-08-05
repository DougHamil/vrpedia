AFRAME.registerComponent("vrpedia-abstract", {
    schema: {
        title: {type: 'string', default: 'Loading...'},
        abstract: {type: 'string', default: 'Please wait'}
    },
    init: function() {
        console.log("hello, world!");
    }
});
