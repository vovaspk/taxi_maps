var usersGlobal = [];

function getUsers() {
    $.ajax({
        type: "GET",
        url: "/getUsersList",
        headers:
            {
                'Content-Type': 'application/json;charset=UTF-8'
            },
        success: function(data){
            data.forEach(user => {
                var marker = new google.maps.Marker({
                    icon: {
                        url: "https://fonts.googleapis.com/icon?family=Material+Icons"
                    },
                    animation: google.maps.Animation.DROP,
                    position: new google.maps.LatLng(user.location.lat, user.location.lng),
                    map:map
                });

                var infoCarWindow = new google.maps.InfoWindow({
                    content: user.location.address + ", " + user.userName,
                    options: {maxWidth: 200}
                });

                marker.addListener('click', function() {

                    infoCarWindow.open(map, marker);
                });
                markerGlobal[user.id]=marker;
            })


        },
        dataType: "json"
    });
}

function updateUsersPosition() {
    $.ajax({
        type: "GET",
        url: "/getUserList",
        headers:
            {
                'Content-Type': 'application/json;charset=UTF-8'
            },
        success: function(data){
            for(var i in data){
                var user = data[i];
                if(typeof markerGlobal[user.id] === "undefined"){

                }else{
                    markerGlobal[user.id].setPosition(new google.maps.LatLng(user.location.lat, user.location.lng));
                }

            }

        },
        dataType: "json"
    });
}