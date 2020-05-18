function updateUserGeo(data) {
   geoloc = {
       lat : data.lat,
       lng : data.lng
   }
    var dataJson = JSON.stringify(geoloc);

    $.ajax({
        type: "POST",
        url: "/updateUserGeo",
        data: dataJson,
        headers:
            {
                'Content-Type': 'application/json;charset=UTF-8'
            },
        success: function(data){
            console.log('user geolocation updated successfully');
        },
        dataType: "json"
    });
}