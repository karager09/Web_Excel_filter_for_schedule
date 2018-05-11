function send(){
    var email = $("#email").val();
    var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if(regex.test(email)){
        $.ajax({
            url: "http://localhost:8080/api/register",
            datatype: 'json',
            type: "post",
            contentType: "application/json",
            data: email
        }).then(function (data, status, jqxhr) {

            alert('Żądanie zostało wysłane. Czekaj na potwierdzającego e-maila.');
        });
    }
    else
    {
       alert("Podaj poprawnego e-maila");
    }
}