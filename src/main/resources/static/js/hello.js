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
        url: "http://localhost:8080/api/search/specific",
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
