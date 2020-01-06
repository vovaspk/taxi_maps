function flatten(obj) {
    var flattenedObj = {};
    Object.keys(obj).forEach(function (key) {
        if (obj[key] === null) {
            flattenedObj[key] = obj[key];
        } else if (typeof obj[key] === 'object') {
            $.extend(flattenedObj, flatten(obj[key]));
        } else {
            flattenedObj[key] = obj[key];
        }
    });
    return flattenedObj;
};

function addTodo(data, suc) {
    $.ajax({
        type: "POST",
        url: "/todo/",
        data: data,
        headers:
            {
                'Content-Type': 'application/json;charset=UTF-8',
                'X-CSRF-TOKEN': _csrf
            },
        success: suc,
        dataType: "json"
    });
}

var markerGlobal = [];
var foundCar = {};
function getCars() {
    $.ajax({
        type: "GET",
        url: "/cars/all",
        headers:
            {
                'Content-Type': 'application/json;charset=UTF-8'
            },
        success: function(data){
        data.forEach(car => {
        var marker = new google.maps.Marker({
                                icon: {
                                    url: "http://maps.google.com/mapfiles/kml/pal4/icon15.png"
                                },
                                animation: google.maps.Animation.DROP,
                                position: new google.maps.LatLng(car.location.lat, car.location.lng),
                                map:map
                            });

                            var infoCarWindow = new google.maps.InfoWindow({
                                content: car.location.address + ", " + car.carType,
                                options: {maxWidth: 200}
                            });

                            marker.addListener('click', function() {
                                    console.log(car);
                                    console.log(infoCarWindow);
                                    console.log(marker);
                                    infoCarWindow.open(map, marker);
                            });
                         markerGlobal[car.id]=marker;
        })


},
        dataType: "json"
    });
}

function updatePositionCars() {
    $.ajax({
        type: "GET",
        url: "/cars/all",
        headers:
            {
                'Content-Type': 'application/json;charset=UTF-8'
            },
        success: function(data){
        for(var i in data){
            var car = data[i];
            if(typeof markerGlobal[car.id] === "undefined"){

            }else{
                markerGlobal[car.id].setPosition(new google.maps.LatLng(car.location.lat, car.location.lng));
            }

        }

},
        dataType: "json"
    });
}

function findNearestCar(data, suc) {
    $.ajax({
        type: "GET",
        url: "/cars/find",
        data: data,
        headers:
            {
                'Content-Type': 'application/json;charset=UTF-8'
            },
        success: function(data){


        },
        dataType: "json"
    });
}

setTimeout(() => getCars(), 2000);

setInterval(() => updatePositionCars(), 1000);

$(document.getElementById('foundCarCoords')).ready( function(){

    var foundCarIndex = document.getElementById("foundCarCoords").innerText;
        if(markerGlobal[foundCarIndex] != undefined){
            markerGlobal[foundCarIndex].getAnimation();
            console.log('found right car');
        }else{
            console.log('cannot find right car');
        }
})


function getAllCars(data, suc) {
    $.ajax({
        type: "GET",
        url: "/cars/all",
        data: data,
        headers:
            {
                'Content-Type': 'application/json;charset=UTF-8'
            },
        success: suc,
        dataType: "json"
    });
}

function updateTodo(data, suc) {
    $.ajax({
        type: "PUT",
        url: "/todo/",
        data: data,
        headers:
            {
                'Content-Type': 'application/json;charset=UTF-8',
                'X-CSRF-TOKEN': _csrf
            },
        success: suc,
        dataType: "json"
    });
}

function deleteTodo(id, suc) {
    $.ajax({
        type: "DELETE",
        url: "/todo/",
        data: {
            "id": id,
            "_csrf": _csrf
        },
        success: suc,
        dataType: "json"
    });
}

function triggerTodo(id, suc) {
    $.ajax({
        type: "PATCH",
        url: "/todo/",
        data: {
            "id": id,
            "_csrf": _csrf
        },
        success: suc,
        dataType: "json"
    });
}

function getFoundCarCoords() {
    return $("#foundCarCoords").val();
}

function showEditor() {
    $("#todo-edit").removeClass("d-none");
}

function hideEditor() {
    $("#todo-edit").addClass("d-none");
}

function hideAddButton() {
    $("#todo-add-button").addClass("d-none");
}

function showAddButton() {
    $("#todo-add-button").removeClass("d-none");
}

function getTodoText() {
    return $("#todo-edit-area").val();
}

function getObjIsDone(element) {
    var done = element.attr("data-done");
    if (typeof done == "undefined") {
        return false;
    }
    return done;
}

function getObjId(element) {
    var id = element.attr("data-id");
    return id;
}

function cleanTodoText() {
    return $("#todo-edit-area").val("");
}

function resetTodoEditor() {
    cleanTodoText();
    $("#todo-edit-area").removeAttr("data-id");
    $("#todo-edit-area").removeAttr("data-done");
}

function setTodoEditor(text, id, done) {
    $("#todo-edit-area").val(text);
    $("#todo-edit-area").attr("data-id", id);
    $("#todo-edit-area").attr("data-done", done);
}

function confirmTodo() {
    var dataObj = {};
    dataObj.todo = getTodoText();
    dataObj.done = getObjIsDone($("#todo-edit-area"));
    dataObj.id = getObjId($("#todo-edit-area"));
    var data = JSON.stringify(dataObj);
    if (typeof dataObj.id == "undefined") {
        addTodo(data,todoAddCallback);
    } else {
        updateTodo(data,todoUpdateCallback);
    }

}

function edit(obj) {
    showEditor();
    hideAddButton();
    var todoElem = $(obj).parent().parent();
    var text = todoElem.find(".todo-text").text();
    var done = getObjIsDone(todoElem);
    var id = getObjId(todoElem);
    setTodoEditor(text, id, done);
}

function del(obj) {
    var todoElem = $(obj).parent().parent();
    var id = getObjId(todoElem);
    deleteTodo(id,todoDeleteCallback(id));
}

function done(obj) {
    var todoElem = $(obj).parent().parent();
    var id = getObjId(todoElem);
    triggerTodo(id,todoUpdateCallback);
}

function todoAddCallback(data){
    var html = formatTodo(data);
    $("div.todo-list").append(html);
    hideEditor();
    showAddButton();
    resetTodoEditor();
}

function todoUpdateCallback(data){
    var html = formatTodo(data);
    $("div.todo-list .card[data-id='"+data.id+"']").replaceWith(html);
    hideEditor();
    showAddButton();
    resetTodoEditor();
}

function todoDeleteCallback(id){
    $("div.todo-list .card[data-id='"+id+"']").remove();

}

function formatTodo(dataJson) {
    if(dataJson.done===true){
        var templateText = "    <div class=\"card-body\">\n" +
            "        <h5 class=\"card-title todo-text text-success\">%todo%</h5>\n" +
            "    </div>\n";
    }else{
        var templateText = "    <div class=\"card-body\">\n" +
            "        <h5 class=\"card-title todo-text text-danger\">%todo%</h5>\n" +
            "    </div>\n";
    }

    var template = "<div class=\"card border-dark mb-3\" data-id=\"%id%\" data-done=\"%done%\">\n" +
        "    <div class=\"form-inline\">\n" +
        "        <button type=\"button\" class=\"btn btn-success\" onclick=\"done(this)\">Done</button>\n" +
        "        <button type=\"button\" class=\"btn btn-danger\" onclick=\"del(this)\">Delete</button>\n" +
        "        <button type=\"button\" class=\"btn btn-primary\" onclick=\"edit(this)\">Edit</button>\n" +
        "    </div>\n" + templateText +
        "</div>";
    return formatTemplate(template,dataJson);
}

function formatTemplate(template, data) {
    for (var key in data) {
        template = template.replace(new RegExp('%' + key + '%', 'g'), data[key] === null ? "" : data[key]);
    }
    return template;


}

function bindConfirmTodoButton() {
    $("#todo-confirm-button").click(function () {
        confirmTodo();
    });
}

function bindCancelTodoButton() {
    $("#todo-cancel-button").click(function () {
        hideEditor();
        showAddButton();
        resetTodoEditor();
    });
}

function bindAddTodoButton() {
    $("#todo-add-button").click(function () {
        showEditor();
        hideAddButton();
    });
}

function bindTodoEditor() {
    bindAddTodoButton();
    bindCancelTodoButton();
    bindConfirmTodoButton();
}


window.onload = function () {
    console.log("Page loaded ");
};

$(document).ready(function () {
    bindTodoEditor();


    initForm();

    function initForm() {
        $("div.ajax-form").each(function () {
            var form = $(this);
            var url = form.attr("data-link");
            var inputs = form.find("input");
            var answer = form.find(".request-result");
            var confirmButton = form.find(".confirm");
            var updateContainerClass = form.attr("data-update-container");
            var containersToUpdate = $('.' + updateContainerClass);
            var selects = form.find("select");


            confirmButton.on("click", function () {
                var _data = {};
                inputs.each(function () {
                    var fieldName = $(this).attr("name");
                    var fieldVal = $(this).val();
                    Object.defineProperty(_data, fieldName, {
                        value: fieldVal,
                        writable: false,
                        configurable: false,
                        enumerable: true
                    });
                });
                selects.each(function () {
                    var fieldName = $(this).attr("name");
                    var mySelections = [];
                    $(this).find("option").each(function (i) {
                        if (this.selected == true) {
                            mySelections.push(this.value);
                        }
                    });

                    Object.defineProperty(_data, fieldName, {
                        value: mySelections,
                        writable: false,
                        configurable: false,
                        enumerable: true
                    });
                });
                _data._csrf = _csrf;
                console.log(_data);
                $.ajaxSetup({traditional: true});
                $.ajax({
                    type: "POST",
                    url: url,
                    data: _data,
                    success: function (resp) {
                        alertResponse(answer, resp);
                        updateContainers(containersToUpdate);
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        alertResponse(answer, xhr);
                        console.log(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                    },
                    dataType: "json"
                });
                $.ajaxSetup({traditional: false});
            });

        });
    };

    function alertResponse(object, response) {
        if (typeof response.statusText != 'undefined') {
            object.html('<div class="alert alert-danger" role="alert">' + response.statusText + "\r\n" + response.responseText + '</div>');
        } else if (response.status == "ok") {
            object.html('<div class="alert alert-success" role="alert">' + response.message + '</div>');
        } else if (response.status == "error") {
            object.html('<div class="alert alert-warning" role="alert">' + response.message + '</div>');
        }
    };

    function updateContainers(containers) {
        containers.each(function () {
            var container = $(this);
            var updateLink = container.attr("data-link");
            $.get(updateLink,
                {},
                function (data) {
                    container.html(data);
                });

        });

    }

    $("button.remove-user").each(function () {
        var updateContainerClass = $(this).attr("data-update-container");
        var containersToUpdate = $('.' + updateContainerClass);
        $(this).on("click", function () {
            var username = $(this).attr("data-id");
            $.ajax({
                type: "POST",
                url: "/userAdmin/delete",
                data: {
                    "username": username,
                    "_csrf": _csrf,
                },
                success: function (resp) {
                    //alertResponse(answer,resp);
                    updateContainers(containersToUpdate);
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    //alertResponse(answer,xhr);
                    console.log(thrownError + "\r\n" + xhr.statusText + "\r\n" + xhr.responseText);
                },
                dataType: "json"
            });
        });
    });


});
