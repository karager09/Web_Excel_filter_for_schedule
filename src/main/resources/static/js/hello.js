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
            localStorage.setItem("name", name);
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
            localStorage.setItem("name", name+"/"+imie);
            //localStorage.setItem("imie", imie)
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
    var name = localStorage.getItem("name");
    var info = localStorage.getItem("data");

    $.ajax({
        url: "http://localhost:8080/api/lecturer/"+name,
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
                $("#subjects_h").append("Przedmioty");
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

function export_to_pdf(){
    window.print();
}


function change_check(number){

    $.ajax({
        url: "http://localhost:8080/api/what_to_show/"+number,
        type : "get"
    }).then(function(data) {
        //var example = ["pensum","zniżka pensum"];

        for (var i in data) {
            data[i] = data[i].replace(/\"/g, ' ').replace(/ /g, '_').toUpperCase();
        }


        $(".check_class").each(function () {
            var flag = false;

            for (var i in data) {
                if (data[i] == this.id) flag = true;
            }

            if (flag == true) this.checked = true;
            else this.checked = false;

        });

    });
}

function get_all_alias(){



    for(var number=1; number<5; ++number){
        get_alias(number);
    }
}

function get_alias(number){
    $.ajax({
        url: "http://localhost:8080/api/what_to_show/name/"+number,
        type : "get"
    }).then(function(data) {
        $("#change" + number).html(data);
    });
}

function save_as(number){

    if($("#change_name_"+number).val() != ""){
        var tab = new Array();
        $(".check_class").each(function () {
            if(this.checked)
                tab.push(this.id.replace(/_/g," "));

        });

        $.ajax({
            url: "http://localhost:8080/api/what_to_show/" + number + "/name/" + $("#change_name_"+number).val(),
            datatype : 'json',
            type : "post",
            contentType : "application/json",
            data : JSON.stringify(tab)
            // data : $("#change_name_"+number).val()
        }).then(function() {
            $("#change" + number).html($("#change_name_"+number).val());
            $("#change_name_"+number).val('');
        });

    }





    // $.ajax({
    //     url: "http://localhost:8080/api/what_to_show/" + number,
    //     datatype : 'json',
    //     type : "post",
    //     contentType : "application/json",
    //     data : JSON.stringify(tab)
    // });



}


function wyslij_nowe_haslo(){

    if($("#haslo").val() == $("#haslo2").val()) {

        var no = window.location.search.split('?')[1];
        //alert(no +", "+$("#haslo").val());

        $.ajax({
            url: "http://localhost:8080/api/reset/" + no,
            datatype: 'json',
            type: "post",
            contentType: "application/json",
            data: $("#haslo").val()
        }).then(function (data, status, jqxhr) {

            if(data === true) alert("Hasło zostało zmienione.");
            else alert("Nie udało się zmienić hasła");
        });


    }else {alert("Podane hasła różnią się od siebie.");}
}

function zresetuj_haslo(){
    $.ajax({
        url: "http://localhost:8080/api/reset",
        type : "get"
    }).then(function(data) {
        if(data === true ) alert("Link aktywacyjny został wysłany na email");
        else alert("Coś poszło nie tak!");
    });

}