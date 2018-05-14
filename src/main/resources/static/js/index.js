/*function wyslij()
{
    var name = $("#nazwisko").val();

    $.ajax({
        url: "http://localhost:8080/api/search/lastname",
        datatype : 'json',
        type : "post",
        contentType : "application/json",
        aktualny : JSON.stringify({
            lastName: name
        })
    }).then(function(aktualny, status, jqxhr) {
        //document.write("Dostalem odpowiedz: "+aktualny);
        if(aktualny === true) //document.write("Prowadzacy "+name+" istnieje!");
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
*/

/**
 * Plik includowany przez /index
 */

/**
 * Wysyłamy dane do serwera jakie imię, nazwisko i plik wybraliśmy.
 * Dostajmy odpowiedź z polami jakie mamy do wyboru.
 */
function wyslijZImieniem(){

    var name = $("#nazwisko").val();
    var imie = $("#imie").val();
    var file = $("#wybierz_plik").val();

    $.ajax({
        url: "http://localhost:8080/api/"+file+"/search/fullname",
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
            localStorage.setItem("file", file);
            window.location.href = "/info";

        }
        else nieMaTakiegoUzytkownika();
    });

}

/**
 * Jesli podane przez nas nazwisko i imie prowadzacego nie figuruje w pliku wyswietlamy odpowiednia informacje.
 */
function nieMaTakiegoUzytkownika(){
    $("#blad").html("<h2>Podałeś złe dane!</h2>");

    $("#nazwisko").val('');
    $('#imie').val('');
}

/**
 * Wstawiamy pliki ktore sa dostepne na serwerze i z ktorych uzytkownik moze wybrac, z ktorego mamy zbierac informacje
 */
function wstaw_pliki(){
    $.ajax({
        url: "http://localhost:8080/api/files",
        type : "get"
    }).then(function(data) {

        for(var i in data)
            if(i==0) $('#wybierz_plik').append('<option value="'+data[i]+'" selected>'+data[i]+'</option>');
            else $('#wybierz_plik').append('<option value="'+data[i]+'">'+data[i]+'</option>');

    });
}