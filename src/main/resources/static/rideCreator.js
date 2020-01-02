

$("#rideForm").submit(function(e) {

    e.preventDefault(); // avoid to execute the actual submit of the form.

    var form = $(this);
    var url = form.attr('action');

    $.ajax({
           type: "POST",
           url: url,
           data: form.serialize(), // serializes the form's elements.
           success: function(data)
           {
               alert(data); // show response from the php script.
           }
         });


});

function createRide(data, suc) {
    $.ajax({
        type: "POST",
        url: "/rides",
        data: data,
        headers:
            {
                'Content-Type': 'application/json;charset=UTF-8'
            },
        success: suc,
        dataType: "json"
    });
}

function confirmRide() {
    var dataObj = {};
    dataObj.start = getObjStart($("#pac-input"));
    dataObj.dest = getObjDest($("#pac-dest"));
    dataObj.carType = getObjCarType($("#carType"));
    dataObj.date = getObjDate($("#date"));
    var data = JSON.stringify(dataObj);

    createRide(data,suc);


}

function getObjStart(element) {
    var start = element.val();
    return start;
}

function getObjDest(element) {
    var dest = element.val();
    return dest;
}

function getObjCarType(element) {
    var carType = element.val();
    return carType;
}

function getObjDate(element) {
    var date = element.val();
    return date;
}