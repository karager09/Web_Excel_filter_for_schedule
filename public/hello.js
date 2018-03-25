$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/greeting"
    }).then(function(data, status, jqxhr) {
       //$('.greeting-id').append(data.id);
       //$('.greeting-content').append(data.content);
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

    potrzebaJeszczeImie();
}


function potrzebaJeszczeImie(){
    $('#imie_div').append("<label for=\"imie\">Podaj imię:</label>\n" +
        "<input type=\"text\" class=\"form-control\" id=\"imie\">" +
    "<br>");

    $('#send').attr("onclick", "wyslijZImieniem()");
}

function wyslijZImieniem(){
    //document.write("wysłano: " + $("#nazwisko").val() + ", "+ $("#imie").val());
nieMaTakiegoUzytkownika();

}

function nieMaTakiegoUzytkownika(){
    $("#main").prepend("<h2>Podałeś złe dane!</h2>");

    $("#nazwisko").val('');
    $('#imie').val('');

}
