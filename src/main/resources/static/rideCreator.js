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

            globalStart = data["origin"];
            globalDest = data["destination"];

            $("#submitbtn").prop("disabled", true);

            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/rides",
                data: JSON.stringify(data),
                dataType: 'json',
                //timeout: 600000,
                success: function (data) {
                    console.log("DONE");
                    window.location.href("/processInput");
                },
                error: function (e) {
                    console.log("ERROR: ", e);
                    //display(e);
                    $("#submitbtn").prop("disabled", false);
                }
            });


        });

    });

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