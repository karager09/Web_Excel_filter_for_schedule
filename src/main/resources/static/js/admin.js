//plik includowany w admin_panel

/**
 * Przesyla informacje wykorzsytywane w DataPlace,
 * dzięki temu definiujemy gdzie znajdują się informacje w konkretnych plikach
 */
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
            url: "http://localhost:8080/api/"+$( "#wybierz_plik option:selected" ).text()+"/calc/info",
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

/**
 * Pobieramy dane z DataPlace, czyli informacje o strukturze zapisanych plików
 * @param fileName plik nas interesujący
 */
function pobierz_dane(fileName){

    $.ajax({
        url: "http://localhost:8080/api/"+fileName+"/calc/info",
        type : "get",
    }).then(function(data, status, jqxhr) {
        
            $("#nazwisko").val(data.nazwisko);
            $("#imie").val(data.imie);
            $("#przedmioty_od").val(data.przedmioty_od);
            $("#przedmioty_do").val(data.przedmioty_do);
            $("#podsumowanie_od").val(data.podsumowanie_od);
            $("#podsumowanie_do").val(data.podsumowanie_do);
            $("#przedmioty").val(data.przedmioty);


    })
}

/**
 * Przesył pliku wybranego w formularzu do tego przygotowanym
 */
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
        enctype: 'multipart/form-aktualny',
        processData: false,
        contentType: false
    }).done(function(data) {
        location.reload();
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

/**
 * Wstawiamy nazwy wszystkich dostępnych plików w admin_panel,
 * tak żeby można było usunąć niepotrzebne za pomocą odpowiedniego przycisku.
 */
function wstaw_pliki(){
    $.ajax({
        url: "http://localhost:8080/api/files",
        type : "get"
    }).then(function(data) {

        for(var i in data) {
            $('#pliki').append('<div class="form-group row"> <label for="' + data[i] + '" class="col-sm-6 col-form-label">' + data[i] + '</label> <div class="col-sm-3"> <button type="button" class="btn" id="' + data[i] + '" onclick="usun_plik(\'' + data[i] + '\');">Usuń</button> </div> </div>');

            if (i == 0)
            {
                $('#wybierz_plik').append('<option value="' + data[i] + '" selected>' + data[i] + '</option>');
                pobierz_dane(data[i]);
            }
            else $('#wybierz_plik').append('<option value="' + data[i] + '">' + data[i] + '</option>');
        }
    });
}

/**
 * Usuwa plik z serwera.
 * @param fileName nazwa pliku do usuniecia
 */
function usun_plik(fileName){
    $.ajax({
        url: "http://localhost:8080/api/files/"+fileName,
        type : "delete"
    }).then(function(data) {
        location.reload();
    });

}

/**
 * W zależności od tego jaki plik jest aktualnie wybrany pokazujemy
 * strukture odnoszaca sie do niego.
 */
function update_info(){
    pobierz_dane($( "#wybierz_plik option:selected" ).text());
}