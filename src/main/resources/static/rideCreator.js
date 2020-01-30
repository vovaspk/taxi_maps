var globalStart;
var globalDest;



$(document).ready(
    function($) {

    $('#estimatebtn').click(function(event){
            var data = {};
            data["origin"] = $("#pac-input").val();
            data["destination"] = $("#pac-dest").val();
            data["carType"] = $("#carType").val();
            data["date"] = $("#date").val();

            globalStart = $("#pac-input").val();
            globalDest = $("#pac-dest").val();

            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/rides/info",
                data: JSON.stringify(data),
                dataType: 'json',
                success: function (res) {
                    hideRideCreatorForm();
                    showResultInfo(res);
                    drawRoute(directionsService, directionsRenderer)
                    },
                    error: function (e) {
                        console.log("ERROR: ", e);
                        //display(e);
                        $("#submitbtn").prop("disabled", false);
                    }
                });
    });

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
                    showResultDivs();
                    showRideResult(res);
                    drawRoute(directionsService, directionsRenderer)



                },
                error: function (e) {
                    console.log("ERROR: ", e);
                    //display(e);
                    $("#submitbtn").prop("disabled", false);
                }
            });


        });

    });

    function showResultInfo(resultInfo){
         $('#startPointInfo').append(resultInfo.startPoint);
         $('#destinationInfo').append(resultInfo.destination);
         $('#priceInfo').append(resultInfo.price);
         $('#timeOfRideInfo').append(resultInfo.timeOfRide);
    }

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

         $('#rideTime').text("" + data.rideTime);
         $('#rideData').text(data.rideDate);
         $('#car').text(data.car);
         $('#price').text(data.price);
    }

    function showResultDivs(){
        showRideTime();
        showFoundCar();
        showPrice();
    }

    function hideResultDivs(){
        hideRideTime();
        hideFoundCar();
        hidePrice();
    }

    function showRideTime(){
        $("#rideTime").addClass("d-none");
    }

    function hideRideTime(){
        $("#rideTime").removeClass("d-none");
    }

    function showFoundCar(){
        $("#car").addClass("d-none");
    }

    function hideFoundCar(){
        $("#car").removeClass("d-none");
    }

    function showPrice(){
        $("#price").addClass("d-none");
    }

    function hidePrice(){
        $("#price").removeClass("d-none");
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
