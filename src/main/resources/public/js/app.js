var gameModel;
var currentChosenRow = 0;
var currentChosenCol = 0;
var scanFlag = 0;           //So that the scan function only fires on the scan option

$( document ).ready(function() {
  // Handler for .ready() called.
  $.getJSON("model", function( json ) {
  gameModel = json;
    console.log( "JSON Data: " + json );
   });
});

function placeShip() {
   console.log($( "#shipSelec" ).val());
   console.log($( "#rowSelec" ).val());
   console.log($( "#colSelec" ).val());
   console.log($( "#orientationSelec" ).val());

   //var menuId = $( "ul.nav" ).first().attr( "id" );
   var request = $.ajax({
     url: "/placeShip/"+$( "#shipSelec" ).val()+"/"+$( "#rowSelec" ).val()+"/"+$( "#colSelec" ).val()+"/"+$( "#orientationSelec" ).val(),
     method: "post",
     data: JSON.stringify(gameModel),
     contentType: "application/json; charset=utf-8",
     dataType: "json"
   });

   request.done(function( currModel ) {
     displayGameState(currModel);
     gameModel = currModel;

   });

   request.fail(function( jqXHR, textStatus ) {
     alert( "Request failed: " + textStatus );
   });
}


function updateCoordinateStuff(row, col){

    //Update the view with the most recent coordinates
    document.getElementById("chosenP2").removeAttribute("hidden");
    document.getElementById("chosenRowSpan").innerHTML = row;
    currentChosenRow = row;
    document.getElementById("chosenColSpan").innerHTML = col;
    currentChosenCol = col;

    //Create correct strings for URLs
    var fireString = "chosenFireFunc('/fire/" + row + "/" + col + "')";
    var scanString = "chosenScanFunc('/scan/" + row + "/" + col + "')";

    //Update onclicks
    document.getElementById('chosenFire').setAttribute('onclick', fireString);
    document.getElementById('chosenScan').setAttribute('onclick', scanString);

}

function chosenFireFunc (reqURL) {
    //console.log("Yarr matey");

    //Send the request
    var request = $.ajax({
        url: reqURL,
        method: "post",
        data: JSON.stringify(gameModel),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    });

    //If it's good, display the game state
    request.done(function( currModel ) {
        displayGameState(currModel);
        gameModel = currModel;
    });

    //If it's not, error
    request.fail(function( jqXHR, textStatus ) {
        alert( "Request failed: " + textStatus );
    });

}

function chosenScanFunc (reqURL) {
    //console.log("Searchin the 7 seas");

    //Send the request
    var request = $.ajax({
        url: reqURL,
        method: "post",
        data: JSON.stringify(gameModel),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    });

    //If it went through, display the game state
    request.done(function( currModel ) {
        scanFlag = 1;
        displayGameState(currModel);
        gameModel = currModel;
    });

    //If it didn't, error
    request.fail(function( jqXHR, textStatus ) {
        alert( "Request failed: " + textStatus );
    });
}


function log(logContents){
    console.log(logContents);
}

function displayGameState(gameModel){
    $( '#MyBoard td'  ).css("background-color", "blue");
    $( '#TheirBoard td'  ).css("background-color", "blue");

    if (gameModel.overlapResult) {
        alert("Your ships can't overlap, try again.");
    }

    displayShip(gameModel.aircraftCarrier);
    displayShip(gameModel.battleship);
    displayShip(gameModel.cruiser);
    displayShip(gameModel.destroyer);
    displayShip(gameModel.submarine);

    for (var i = 0; i < gameModel.computerMisses.length; i++) {
       $( '#TheirBoard #' + gameModel.computerMisses[i].Across + '_' + gameModel.computerMisses[i].Down ).css("background-color", "green");
    }
    for (var i = 0; i < gameModel.computerHits.length; i++) {
       $( '#TheirBoard #' + gameModel.computerHits[i].Across + '_' + gameModel.computerHits[i].Down ).css("background-color", "red");
    }
    for (var i = 0; i < gameModel.playerMisses.length; i++) {
       $( '#MyBoard #' + gameModel.playerMisses[i].Across + '_' + gameModel.playerMisses[i].Down ).css("background-color", "green");
    }
    for (var i = 0; i < gameModel.playerHits.length; i++) {
       $( '#MyBoard #' + gameModel.playerHits[i].Across + '_' + gameModel.playerHits[i].Down ).css("background-color", "red");
    }

    // Added this flag check to make the scan alert only fire on scanning
    if (scanFlag) {
        //Check to see if the scan found anything and display
        if(gameModel.scanResult){
            alert("Scan found at least one Ship");
        }
        else{
            alert("Scan found no Ships");
        }
        scanFlag = 0;
    }


}



function displayShip(ship){

    startCoordAcross = ship.start.Across;
    startCoordDown = ship.start.Down;
    endCoordAcross = ship.end.Across;
    endCoordDown = ship.end.Down;
    // console.log(startCoordAcross);
    if(startCoordAcross > 0){
        if(startCoordAcross == endCoordAcross){
            for (i = startCoordDown; i <= endCoordDown; i++) {
                $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-color", "yellow");
            }
        } else {
            for (i = startCoordAcross; i <= endCoordAcross; i++) {
                $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-color", "yellow");
            }
        }
    }
}
