var globalStart;
var globalDest;

// $("#rideForm").submit(function(e) {
//
//     e.preventDefault(); // avoid to execute the actual submit of the form.
//
//     var form = $(this);
//     var url = form.attr('action');
//
//     $.ajax({
//            type: "POST",
//            url: url,
//            data: form.serialize(), // serializes the form's elements.
//            success: function(data)
//            {
//                alert(data);
//            }
//          });
//
//
// });

$(document).ready(
    function($) {

        $("#submitbtn").click(function(event) {

            var data = {};
            data["origin"] = $("#pac-input").val();
            data["destination"] = $("#pac-dest").val();
            data["carType"] = $("#carType").val();
            data["date"] = $("#date").val();

            globalStart = $("#pac-input").val();
            globalDest = $("#pac-dest").val();

            $("#submitbtn").prop("disabled", true);

            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/rides",
                data: JSON.stringify(data),
                dataType: 'json',
                //timeout: 600000,
                success: function (res) {
                    hideRideCreatorForm();
                    showRideResult(res);
                    drawRoute(directionsService, directionsRenderer)
                    //setCookie('start', data.origin, 1);
                    //setCookie('dest', data.destination, 1);
                    //window.location.href = '/processInput';

                    //localStorage.setItem(data.start, "globalStart");
                    //localStorage.setItem(data.destination, "globalDest");


                },
                error: function (e) {
                    console.log("ERROR: ", e);
                    //display(e);
                    $("#submitbtn").prop("disabled", false);
                }
            });


        });

    });

    function drawRoute(directionsService, directionsRenderer){

            console.log('goes to here 2');

            directionsService.route(
                {
                    origin: {query: globalStart},
                    destination: {query: globalDest},
                    travelMode: 'DRIVING'
                },
                function(response, status) {
                    if (status === 'OK') {
                        console.log('goes to here 3');
                        directionsRenderer.setDirections(response);

                    } else {
                        console.log('goes to here fail');
                        window.alert('Directions request failed due to ' + status);
                    }
                });


    }

    function setCookie(cname, cvalue, exdays) {
      var d = new Date();
      d.setTime(d.getTime() + (exdays*24*60*60*1000));
      var expires = "expires="+ d.toUTCString();
      document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }

    function getCookie(cname) {
      var name = cname + "=";
      var decodedCookie = decodeURIComponent(document.cookie);
      var ca = decodedCookie.split(';');
      for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
          c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
          return c.substring(name.length, c.length);
        }
      }
      return "";
    }

    function showRideResult(data){
         console.log(data.rideTime);
         console.log(data.rideDate);
         console.log(data.car);
         console.log(data.price);
         $('#rideTime').text(data.rideTime);
         $('#rideData').text(data.rideDate);
         $('#car').text(data.car);
         $('#price').text(data.price);
    }


    function hideRideCreatorForm() {
        $("#input-form").addClass("d-none");
    }

    function showRideCreatorForm() {
        $("#input-form").removeClass("d-none");
    }

    function hideInfoRide() {
            $("infoRide").addClass("d-none");
        }

    function showInfoRide() {
        $("infoRide").removeClass("d-none");
    }

// function createRide(data) {
//     $.ajax({
//         type: "POST",
//         url: "/rides",
//         data: data,
//         headers:
//             {
//                 'Content-Type': 'application/json;charset=UTF-8'
//             },
//         success: function(data)
//         {
//             alert(data);
//         },
//         dataType: "json"
//     });
// }
//
// function suc() {
//
// }
//
// function confirmRide() {
//     var dataObj = {};
//     dataObj.start = getObjStart($("#pac-input"));
//     dataObj.dest = getObjDest($("#pac-dest"));
//     dataObj.carType = getObjCarType($("#carType"));
//     dataObj.date = getObjDate($("#date"));
//
//     var data = JSON.stringify(dataObj);
//
//     createRide(data,suc);
//
//
// }
//
// function getObjStart(element) {
//     var start = element.val();
//     return start;
// }
//
// function getObjDest(element) {
//     var dest = element.val();
//     return dest;
// }
//
// function getObjCarType(element) {
//     var carType = element.val();
//     return carType;
// }
//
// function getObjDate(element) {
//     var date = element.val();
//     return date;
// }