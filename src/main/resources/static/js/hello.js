function wyslij()
{
    var name = $("#nazwisko").val();

    $.ajax({
        url: "http://localhost:8080/api/search/lastname",
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
        url: "http://localhost:8080/api/search/fullname",
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
    var identity = id.replace(/\"/g,' ').replace(/ /g,'_');
    $('#jakie_info').append('<div><input type="checkbox" class="check_class filled-in form-check-input" id=\"'+identity+'\" checked="checked"> <label class="form-check-label" for='+id+'>'+id+'</label> </div><br>');

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


function send_request_lecturer(){

    var tab = new Array();

    $(".check_class").each(function () {
        if(this.checked)
        tab.push(this.id.replace(/_/g," "));

    });

    //alert(tab);
    localStorage.setItem("data", JSON.stringify(tab));

    window.location.href = "/data";
}


function add_row(where,first,second){
    $(where).append('<tr><td>'+first+'</td> <td>'+second+'</td> </tr>');

}

function add_information(){
    var nazwisko = localStorage.getItem("nazwisko");
    var imie = localStorage.getItem("imie");

    var info = localStorage.getItem("data");
    var where_to_send = nazwisko;
    if(imie !== null) where_to_send=nazwisko+"/"+imie;
    $.ajax({
        url: "http://localhost:8080/api/lecturer/"+where_to_send,
        datatype : 'json',
        type : "post",
        contentType : "application/json",
        data : info
    }).done(function(data) {

        var parsed = JSON.parse(data);
        var keys = Object.keys(parsed);

        for(var i in keys){
            if(keys[i] !== "przedmioty")
            add_row('#data_body', keys[i],parsed[keys[i]]);
            else {
                $("#subjects_div").innerText = "Przedmioty";
                $("#thead_subjects").append('<tr>\n" +"<th scope=\"col\">Nazwa</th>\n" +"<th scope=\"col\">Ilość godzin</th>\n"+"</tr>');

                przedmioty = parsed[keys[i]];
                keys_przedmioty = Object.keys(przedmioty);

                for (var i in keys_przedmioty) {
                    add_row("#subjects", keys_przedmioty[i], przedmioty[keys_przedmioty[i]]);
                }
            }
        }
        //alert(keys[0] + ", "+keys[1]);
        //alert(parsed.PENSUM);
        //alert(parsed[keys[0]]);
        //alert(data);

    }).fail(function(jqXHR, textStatus) {
        alert('BLAD!!');
    });


}


