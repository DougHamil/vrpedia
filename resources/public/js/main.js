document.body.onload = function() {
    let query = getParameterByName('subject') || 'Boise';
    let scene = document.querySelector('a-scene');

    $.get('/api/entity/search?query='+query).done(response => {
        console.log("Subject:");
        console.log(response);
        scene.systems.vrpedia.rebuild(response);
    });
};
