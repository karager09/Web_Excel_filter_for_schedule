$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/greeting"
    }).then(function(data, status, jqxhr) {
       $('.greeting-id').append(data.id);
       $('.greeting-content').append(data.content);
       console.log(jqxhr);
    });
});



function wyslij()
{
    $.ajax({
        url: "http://localhost:8080/greeting",
        datatype : 'json',
        type : "post",
        contentType : "application/json",
        data : JSON.stringify({
            content : 'Hi there',
            id : 90
        }),
        // success: alert("xd"),
        // failure: alert("xd2")

    }).then(function(data, status, jqxhr) {
        $('.greeting-id').append(data.id);
        $('.greeting-content').append(data.content);
        console.log(jqxhr);
    });

}
