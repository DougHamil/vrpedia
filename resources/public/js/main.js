document.body.onload = function() {
    var query = getParameterByName('query') || 'Boise';

    $.get('/api/entity/search?query='+query).done(response => {
                console.log(response);
                var titleTextEl = document.querySelector("#titleText");
                titleTextEl.setAttribute('text', {value:response.label});
                $("#abstractText").attr('value', response.abstract);
                $("#thumbnail").attr("material", "src: url("+response.image+")");
            });
}