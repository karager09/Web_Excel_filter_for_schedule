function przeslij_informacje(){

    var nazwisko = $("#nazwisko").val();
    var imie = $("#imie").val();
    var przedmioty_od = $("#przedmioty_od").val();
    var przedmioty_do = $("#przedmioty_do").val();
    var podsumowanie_od = $("#podsumowanie_od").val();
    var podsumowanie_do = $("#podsumowanie_do").val();
    var przedmioty = $("#przedmioty").val();

    if(nazwisko.match("^[1-9][0-9]*$") && imie.match("^[1-9][0-9]*$") && przedmioty_od.match("^[1-9][0-9]*$") && przedmioty_do.match("^[1-9][0-9]*$") && podsumowanie_od.match("^[1-9][0-9]*$") && podsumowanie_do.match("^[1-9][0-9]*$") && przedmioty.match("^[1-9][0-9]*$")){


        $.ajax({
            url: "http://localhost:8080/api/calc/info",
            datatype : 'json',
            type : "post",
            contentType : "application/json",
            data : JSON.stringify({
                nazwisko: parseInt(nazwisko),
                imie: parseInt(imie),
                przedmioty_od: parseInt(przedmioty_od),
                przedmioty_do: parseInt(przedmioty_do),
                podsumowanie_od: parseInt(podsumowanie_od),
                podsumowanie_do: parseInt(podsumowanie_do),
                przedmioty: parseInt(przedmioty)
            })


        }).done(function(data) {
            if(data == true) alert("Udało się poprawnie zapisać dane.")
            else alert("Nie udało się zapisać danych :(");
        }).fail(function(jqXHR, textStatus) {
            alert("Nie udało się zapisać danych :(");
        });


    } else {
        alert("Upewnij się, że wszystkie podane informacje są liczbami naturalnymi!");
        //alert(nazwisko + ", " + imie + ", " + przedmioty_od + ", " + przedmioty_do + ", " + podsumowanie_od + ", " + podsumowanie_do + ", " + przedmioty);
    }
}

function pobierz_dane(){

    $.ajax({
        url: "http://localhost:8080/api/calc/info",
        type : "get",
    }).then(function(data) {
        $("#nazwisko").val(data.nazwisko);
        $("#imie").val(data.imie);
        $("#przedmioty_od").val(data.przedmioty_od);
        $("#przedmioty_do").val(data.przedmioty_do);
        $("#podsumowanie_od").val(data.podsumowanie_od);
        $("#podsumowanie_do").val(data.podsumowanie_do);
        $("#przedmioty").val(data.przedmioty);

    })
}


function przeslij_plik(){

    var plik = $('#plik');
    var filename = $.trim(plik.val());
    if (!(filename.match(/xlsx$/i))) {
        alert('Podaj plik z rozszerzeniem .xlsx!');
        return;
    }

    $.ajax({
        url: 'http://localhost:8080/api/calc/file',
        type: "POST",
        data: new FormData(document.getElementById("plikForm")),
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false
    }).done(function(data) {
        alert("Plik przesłany!");
    }).fail(function(jqXHR, textStatus) {
        alert('File upload failed ...');
    });
}




// function wyslij_haslo(){
//     dobre_haslo();
//     //zle_haslo();
// }
//
// function dobre_haslo(){
//     window.location.href = "/src/main/resources/templates/admin_panel.html";
//
//
// }
//
// function zle_haslo(){
//     $("#blad").html("<h2>Podałeś złe hasło!</h2>");
//     $("#haslo").val('');
//
//     $("#przypomnienie").html('<button class="btn btn-danger" type="button" onclick="przypomnij_haslo();">Zresetuj hasło</button>');
//
// }

function przypomnij_haslo(){
    alert("Nowe hasło zostanie wysłane");
}
