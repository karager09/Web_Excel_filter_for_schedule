// $(document).ready(function() {
//     $.ajax({
//         url: "http://localhost:8080/greeting"
//     }).then(function(data, status, jqxhr) {
//        //$('.greeting-id').append(data.id);
//        //$('.greeting-content').append(data.content);
//        console.log(jqxhr);
//     });
// });



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
            //content : 'Hi there',
            //id : 90
        })
        // success: alert("xd"),
        // failure: alert("xd2")

    }).then(function(data, status, jqxhr) {
        //document.write("Dostalem odpowiedz: "+data);
        if(data == true) document.write("Prowadzacy "+name+" istnieje!");
        else potrzebaJeszczeImie();
        //$('.greeting-id').append(data.id);
        //$('.greeting-content').append(data.content);
        //console.log(jqxhr);
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