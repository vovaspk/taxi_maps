<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Hello</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <style>
        /* Always set the map height explicitly to define the size of the div
         * element that contains the map. */
        #map {
            height: 80%;
            margin-top: 20px;
        }
        /* Optional: Makes the sample page fill the window. */
        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
        }
        #input-form{
            margin-top: 30px;
        }
        #description {
            font-family: Roboto;
            font-size: 15px;
            font-weight: 300;
        }

        #infowindow-content .title {
            font-weight: bold;
        }

        #infowindow-content {
            display: none;
        }

        #map #infowindow-content {
            display: inline;
        }

        .pac-card {
            margin: 10px 10px 0 0;
            border-radius: 2px 0 0 2px;
            box-sizing: border-box;
            -moz-box-sizing: border-box;
            outline: none;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
            background-color: #fff;
            font-family: Roboto;
        }

        #pac-container {
            padding-bottom: 12px;
            margin-right: 12px;
        }

        .pac-controls {
            display: inline-block;
            padding: 5px 11px;
        }

        .pac-controls label {
            font-family: Roboto;
            font-size: 13px;
            font-weight: 300;
        }

        #pac-input {
            background-color: #fff;
            font-family: Roboto;
            font-size: 15px;
            font-weight: 300;
            margin-left: 12px;
            padding: 0 11px 0 13px;
            text-overflow: ellipsis;
            width: 400px;
        }

        #pac-input:focus {
            border-color: #4d90fe;
        }

        #title {
            color: #fff;
            background-color: #4d90fe;
            font-size: 25px;
            font-weight: 500;
            padding: 6px 12px;
        }
    </style>
</head>
<body>
<#include "parts/navbar.ftl">
<div id="originPlaceId">
<#--<p th:text="${originPlaceId}"></p>-->
    ${originPlaceId}
</div>
<div id="destPlaceId">
<#--<p th:text="${destPlaceId}"></p>-->
    ${destPlaceId}
</div>
Response should be here:
To get from
<div id="origin">
<#--<p th:text="${origin}"></p>-->
    ${origin}
</div>
to
<div id="destination">
<#--<p th:text="${destination}"></p>-->
    ${destination}
</div>
    you need:
<div id="response">
<!--<p th:text="{response}"></p>-->
</div>
<#--    DISTANTION(meters), duration: <p th:text="${dist}"> </p>-->
<#--<div id="start" >start: <p th:text="${start}"></p> </div>-->
<#--<div id="end">end: <p th:text="${end}"></p> </div>-->
<#--<div id="direct"><p th:text="${direct}"></p></div>-->
<#--dist: ${dist}-->
<#--start: ${start}-->
<#--end: ${end}-->
<#--direct: ${direct}-->
MAP NEXT <button id="showDirection" type="submit">Show Direction and find nearest driver</button>
<div id="map"></div>
<script>
    var map;
    var start = document.getElementById("start");
    var end = document.getElementById("end");
    var origin = document.getElementById("origin").innerText;
    var destination = document.getElementById("destination").innerText;
    var originPlaceId = document.getElementById("originPlaceId").innerText;
    var destPlaceId = document.getElementById("destPlaceId").innerText;
    initMap();
    function initMap() {
        var directionsService = new google.maps.DirectionsService();
        var directionsRenderer = new google.maps.DirectionsRenderer();



        map = new google.maps.Map(document.getElementById('map'), {
            zoom: 12,
            center: {lat: 49.2331, lng: 28.4682},
        });
        directionsRenderer.setMap(map);

        var onChangeHandler = function() {
            console.log('goes to here 1');
            calculateAndDisplayRoute(directionsService, directionsRenderer);
        };
        document.getElementById('showDirection').addEventListener('click', onChangeHandler);
    }

    function calculateAndDisplayRoute(directionsService, directionsRenderer) {
        console.log('goes to here 2');
        directionsService.route(
            {
                origin: {query: document.getElementById("originPlaceId").innerText},
                destination: {query: document.getElementById("destPlaceId").innerText}, //var end doesnt work here and request not found, should pass ltn and lng of start and dest and works fine
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
</script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBR4RT24iLNmr74yE168KRWz5qdjr3NhVc&callback=initMap"
        async defer></script>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

</body>
</html>