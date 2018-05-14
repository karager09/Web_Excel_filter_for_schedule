/**
 * Dla /data
 * Dodajemy jeden wiersz do tabelki danych
 * @param where - gdzie wstawiamy (zwykle dane czy dla przedmiotow)
 * @param first - co w pierwszej kolumnie
 * @param second - co w drugiej kolumnie
 */
function add_row(where,first,second){
    $(where).append('<tr><td>'+first+'</td> <td>'+second+'</td> </tr>');

}

/**
 * Dla /data
 * Wstawiamy informacje o ilosci godzin itp. otrzymane od serwera
 */
function add_information(){
    var name = localStorage.getItem("name");
    var info = localStorage.getItem("data");

    $.ajax({
        url: "http://localhost:8080/api/"+localStorage.getItem("file")+"/lecturer/"+name,
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
                $("#thead_subjects").append('<tr>\n" +"<th scope=\"col\">Nazwa - Rodzaj - Stopień/Semestr</th>\n" +"<th scope=\"col\">Ilość godzin</th>\n"+"</tr>');

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
        //alert(aktualny);

    }).fail(function(jqXHR, textStatus) {
        alert('BLAD!!');
    });


}

/**
 * Drukujemy lub zapisujemy plik.
 */
function export_to_pdf(){
    window.print();
}


/**
 * Dla /reset
 * Ustawiamy nowe haslo.
 * Jesli dane sa poprawne to wysylamy na serwer zadanie zmiany hasla.
 */
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

            if(data === true) {
                alert("Hasło zostało zmienione.");
                window.location.href = "/";
            }
            else alert("Nie udało się zmienić hasła");
        });


    }else {alert("Podane hasła różnią się od siebie.");}
}

/**
 * Dla /login
 * Jesli nie udalo nam sie zalogowac to pokazujemy przycisk i resetujemy haslo
 */
function zresetuj_haslo(){

    var email = localStorage.getItem("email");
    $.ajax({
        url: "http://localhost:8080/api/reset/" + email,
        type : "get"
    }).then(function(data) {
        if(data === true ) alert("Link aktywacyjny został wysłany na email");
        else alert("Coś poszło nie tak!");
    });
}

/**
 * Dla /data
 * Wstawiamy nazwisko prowadzacego zeby ladnie wygladalo.
 */
function wstaw_nazwisko(){

    var name = localStorage.getItem("name");

    var imie = name.split("/")[1];
    var nazwisko = name.split("/")[0];
    imie = imie.toLowerCase().charAt(0).toUpperCase() + imie.toLowerCase().substr(1);
    nazwisko = nazwisko.toLowerCase().charAt(0).toUpperCase() + nazwisko.toLowerCase().substr(1);


    $('#nazwa_prowadzacego').append(imie +" "+ nazwisko);
}


function zapisz_nazwe(){
    var email = $("#email").val();
    localStorage.setItem("email", email);
}
