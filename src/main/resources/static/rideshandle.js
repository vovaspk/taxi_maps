$(document).ready(function () {
       getAllRides();
});

function getAllRides(data, suc) {
    $.ajax({
        type: "GET",
        url: "/rides/all",
        data: data,
        headers:
        {
            'Content-Type': 'application/json;charset=UTF-8'
        },
        success: function(data){
        var ridesList = ('#ridesTable');
        for (var i = 0; i < data.length; i++) {
             $(ridesList).append(' <tr><td>' + data[i].startPoint.address + '</td><td>' + data[i].destination.address + '</td><td>' + data[i].price + '</td></tr>')
            }

        },
        dataType: "json"
    });
}