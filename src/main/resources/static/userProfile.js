$(document).ready(function () {
       getUserProfile();
});

function getUserProfile(data, suc) {
    $.ajax({
        type: "GET",
        url: "/getUser",
        data: data,
        headers:
        {
            'Content-Type': 'application/json;charset=UTF-8'
        },
        success: function(data){
            $("#userNameField").val(data.name);
            $("#EmailField").val(data.email)

        },
        dataType: "json"
    });
}