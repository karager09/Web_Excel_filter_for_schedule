function wyslij()
{
    var name = $("#nazwisko").val();

    $.ajax({
        url: "http://localhost:8080/api/search",
        datatype : 'json',
        type : "post",
        contentType : "application/json",
        data : JSON.stringify({
            lastName: name
        })
    }).then(function(data, status, jqxhr) {
        //document.write("Dostalem odpowiedz: "+data);
        if(data == true) document.write("Prowadzacy "+name+" istnieje!");
        else potrzebaJeszczeImie();
    });


}


function potrzebaJeszczeImie(){
    $('#imie_div').append("<label for=\"imie\">Podaj imię:</label>\n" +
        "<input type=\"text\" class=\"form-control\" id=\"imie\">" +
    "<br>");

    $('#send').attr("onclick", "wyslijZImieniem()");
}

function wyslijZImieniem(){

    var name = $("#nazwisko").val();
    var imie = $("#imie").val();

    $.ajax({
        url: "http://localhost:8080/api/search",
        datatype : 'json',
        type : "post",
        contentType : "application/json",
        data : JSON.stringify({
            lastName: name,
            firstName: imie
        })


    }).then(function(data, status, jqxhr) {
        if(data == true) document.write("Prowadzacy "+name+", "+ imie+" istnieje!");
        else nieMaTakiegoUzytkownika();
    });

}

function nieMaTakiegoUzytkownika(){
    $("#blad").html("<h2>Podałeś złe dane!</h2>");

    $("#nazwisko").val('');
    $('#imie').val('');
}

function wyslij_haslo(){
    zle_haslo();
}

function zle_haslo(){
    $("#blad").html("<h2>Podałeś złe hasło!</h2>");
    $("#haslo").val('');

    $("#przypomnienie").html('<button class="btn btn-danger" type="button" onclick="przypomnij_haslo();">Zresetuj hasło</button>');

}

function przypomnij_haslo(){

    $("#przypomnienie").html("Nowe hasło zostało wysłane");

}



function przeslij_plik(){

    var plik = $('#plik');

    var filename = $.trim(plik.val());

    if (!(filename.match(/xlsx$/i))) {
        alert('Podaj plik z rozszerzeniem .xlsx!');
        return;
    }


    $.ajax({
        url: 'http://localhost:8080/api/plik',
        type: "POST",
        data: new FormData(document.getElementById("plikForm")),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false
    }).done(function(data) {
        document.write("Plik przesłany!");
    }).fail(function(jqXHR, textStatus) {
        alert('File upload failed ...');
    });

}