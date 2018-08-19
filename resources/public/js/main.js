document.body.onload = function() {
    let query = getParameterByName('subject') || 'Boise';
    let scene = document.querySelector('a-scene');

    window._VRPedia.searchForSubject(query);
};
