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
        if(data === true) //document.write("Prowadzacy "+name+" istnieje!");
        {
            localStorage.setItem("nazwisko", name);
            window.location.href = "/info";

        }
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
        if(data === true) //document.write("Prowadzacy "+name+", "+ imie+" istnieje!");
        {
            localStorage.setItem("nazwisko", name);
            localStorage.setItem("imie", imie)
            window.location.href = "/info";

        }
        else nieMaTakiegoUzytkownika();
    });

}

function nieMaTakiegoUzytkownika(){
    $("#blad").html("<h2>Podałeś złe dane!</h2>");

    $("#nazwisko").val('');
    $('#imie').val('');
}



function dodaj_info(id){
    //var id = "cos";
    //id = localStorage.getItem("nazwisko");
    $('#jakie_info').append('<div><input type="checkbox" class="filled-in form-check-input" id='+id+' checked="checked"> <label class="form-check-label" for='+id+'>'+id+'</label> </div><br>');

}

function dodaj_jakie_info(){

    $.ajax({
        url: "http://localhost:8080/api/what_to_show",
        type : "get"
    }).then(function(data) {
        for(var i in data)
        {
            dodaj_info(data[i]);
        }

    });
}


function przeslij_jakie_info(){







}