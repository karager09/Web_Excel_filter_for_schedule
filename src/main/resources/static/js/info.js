/**
 * Dodajemy pojedynczy wiersz w "do wyboru"
 * @param id
 */
function dodaj_info(id){
    //var id = "cos";
    //id = localStorage.getItem("nazwisko");
    var identity = id.replace(/\"/g,' ').replace(/ /g,'_');
    $('#jakie_info').append('<div><input type="checkbox" class="check_class filled-in form-check-input" id=\"'+identity+'\" checked="checked"> <label class="form-check-label" for='+id+'>'+id+'</label> </div><br>');

}

/**
 * Dodajemy wszystkie informacje, z których użytkownik może sobie później wybrac
 */
function dodaj_jakie_info(){

    $.ajax({
        url: "http://localhost:8080/api/"+localStorage.getItem("file")+"/what_to_show",
        type : "get"
    }).then(function(data) {
        for(var i in data)
        {
            dodaj_info(data[i]);
        }

    });
}

/**
 * Zbieramy informacje, ktore uzytkownik chce otrzymac, zapamietujemy je i przechodzimy do nastepnej strony
 */
function send_request_lecturer(){

    var tab = new Array();

    $(".check_class").each(function () {
        if(this.checked)
            tab.push(this.id.replace(/_/g," "));

    });

    //alert(tab);
    localStorage.setItem("data", JSON.stringify(tab));

    window.location.href = "/aktualny";
}

/**
 * Zmiana zaznaczonych pozycji - przycisk do zmiany zostal nacisniety
 * Wysylamy zapytanie do serwera i zmieniamy zaznaczenia zgodnie z odpowiedzia
 * @param number - ktory przycisk zostal wcisniety
 */
function change_check(number){

    $.ajax({
        url: "http://localhost:8080/api/"+localStorage.getItem("file")+"/profile/"+number,
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

/**
 * Ustawiamy nazwy przyciskow
 */
function get_all_alias(){

    for(var number=1; number<5; ++number){
        get_alias(number);
    }
}

/**
 * Ustawiamy nazwe jednego przycisku
 * @param number - ktory przycisk
 */
function get_alias(number){
    $.ajax({
        url: "http://localhost:8080/api/"+localStorage.getItem("file")+"/profile/name/"+number,
        type : "get"
    }).then(function(data) {
        $("#change" + number).html(data);
    });
}


/**
 * Zapisujemy nazwe pliku i zaznaczenia w tym momencie.
 * Serwer to zapamietuje, tak zeby pozniej mozna bylo z tego skorzystac.
 * @param number - ktory przycisk
 */
function save_as(number){

    if($("#change_name_"+number).val() != ""){
        var tab = new Array();
        $(".check_class").each(function () {
            if(this.checked)
                tab.push(this.id.replace(/_/g," "));

        });

        $.ajax({
            url: "http://localhost:8080/api/"+localStorage.getItem("file")+"/profile/" + number + "/name/" + $("#change_name_"+number).val(),
            datatype : 'json',
            type : "post",
            contentType : "application/json",
            data : JSON.stringify(tab)
            // aktualny : $("#change_name_"+number).val()
        }).then(function() {
            $("#change" + number).html($("#change_name_"+number).val());
            $("#change_name_"+number).val('');
        });

    }

}