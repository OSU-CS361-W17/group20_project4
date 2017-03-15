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

function switchToEasyMode () {

    console.log("gg ez");

    //Send the request
    var request = $.ajax({
        url: "/easyMode",
        method: "post",
        data: JSON.stringify(gameModel),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    });

    //If it's good, display the game state
    request.done(function( currModel ) {
        console.log("switched successfully to easy mode");
        console.log(currModel);
        //Clear the enemy's board
        displayGameState(currModel);
        gameModel = currModel;
    });

    //If it's not, error
    request.fail(function( jqXHR, textStatus ) {
        alert( "Request failed: " + textStatus );
    });

}

function switchToHardMode () {

    console.log("gg no re");

    var request = $.ajax({
        url: "/hardMode",
        method: "post",
        data: JSON.stringify(gameModel),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    });

    //If it's good, display the game state
    request.done(function( currModel ) {
        console.log("switched successfully to hard mode");
        console.log(currModel);

        //clear the enemy's board
        for (var i = 1; i <= 10; i++) {
           for (var j = 1; j <= 10; j++) {
               $( '#TheirBoard #' + i + '_' + j ).css("background-image", "url('http://imgur.com/a/D5WtN')");
               $( '#TheirBoard #' + i + '_' + j ).css("background-size", "contain");
               $( '#TheirBoard #' + i + '_' + j ).css("background-repeat", "no-repeat");
            }
        }

        displayGameState(currModel);
        gameModel = currModel;
    });

    //If it's not, error
    request.fail(function( jqXHR, textStatus ) {
        alert( "Request failed: " + textStatus );
    });

}

function log(logContents){
    console.log(logContents);
}

function displayGameState(gameModel){
    // the following overwrites any residual ship icons from the previous game state with a lame screenshot of #0000ff
    //this is necessary due to the way we implemented the ship icons
    $( '#MyBoard td' ).css("background-image", "url('http://imgur.com/a/D5WtN')");

    if (gameModel.offBoard) {
        alert("Your ship will be off the board if you place it there! Please try again.")
    }

    if (gameModel.overlapResult) {
        alert("Your new ship will overlap another ship if you place it there! Please try again.");
    }

    displayShip(gameModel.aircraftCarrier);
    displayShip(gameModel.battleship);
    displayShip(gameModel.clipper);
    displayShip(gameModel.dinghy);
    displayShip(gameModel.submarine);

    displayHits(gameModel);

    for (var i = 0; i < gameModel.computerMisses.length; i++) {
       $( '#TheirBoard #' + gameModel.computerMisses[i].Across + '_' + gameModel.computerMisses[i].Down ).css("background-image", "url('http://i.imgur.com/R2hmmag.gif')");
       $( '#TheirBoard #' + gameModel.computerMisses[i].Across + '_' + gameModel.computerMisses[i].Down ).css("background-size", "contain");
       $( '#TheirBoard #' + gameModel.computerMisses[i].Across + '_' + gameModel.computerMisses[i].Down ).css("background-repeat", "no-repeat");

    }
    for (var i = 0; i < gameModel.computerHits.length; i++) {
       $( '#TheirBoard #' + gameModel.computerHits[i].Across + '_' + gameModel.computerHits[i].Down ).css("background-image", "url('http://i.imgur.com/yXH9huv.png')");
       $( '#TheirBoard #' + gameModel.computerHits[i].Across + '_' + gameModel.computerHits[i].Down ).css("background-size", "contain");
       $( '#TheirBoard #' + gameModel.computerHits[i].Across + '_' + gameModel.computerHits[i].Down ).css("background-repeat", "no-repeat");

    }
    for (var i = 0; i < gameModel.playerMisses.length; i++) {
       $( '#MyBoard #' + gameModel.playerMisses[i].Across + '_' + gameModel.playerMisses[i].Down ).css("background-image", "url('http://i.imgur.com/R2hmmag.gif')");
       $( '#MyBoard #' + gameModel.playerMisses[i].Across + '_' + gameModel.playerMisses[i].Down ).css("background-size", "contain");
       $( '#MyBoard #' + gameModel.playerMisses[i].Across + '_' + gameModel.playerMisses[i].Down ).css("background-repeat", "no-repeat");

    }
    for (var i = 0; i < gameModel.playerHits.length; i++) {
       $( '#MyBoard #' + gameModel.playerHits[i].Across + '_' + gameModel.playerHits[i].Down ).css("background-image", "url('http://i.imgur.com/yXH9huv.png')");
       $( '#MyBoard #' + gameModel.playerHits[i].Across + '_' + gameModel.playerHits[i].Down ).css("background-size", "contain");
       $( '#MyBoard #' + gameModel.playerHits[i].Across + '_' + gameModel.playerHits[i].Down ).css("background-repeat", "no-repeat");

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
    //console.log("in display");
    //console.log(startCoordAcross, endCoordAcross);
    if(startCoordAcross > 0){
        if(startCoordAcross == endCoordAcross){
            for (i = startCoordDown; i <= endCoordDown; i++) {
                $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-image", "url('http://clipartix.com/wp-content/uploads/2016/04/Blue-boat-clip-art-vector-clip-art-free-clipartcow.png')");
                $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-size", "contain");
                $( '#MyBoard #'+startCoordAcross+'_'+i  ).css("background-repeat", "no-repeat");

            }
        } else {
            for (i = startCoordAcross; i <= endCoordAcross; i++) {
                $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-image", "url('http://clipartix.com/wp-content/uploads/2016/04/Blue-boat-clip-art-vector-clip-art-free-clipartcow.png')");
                $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-size", "contain");
                $( '#MyBoard #'+i+'_'+startCoordDown  ).css("background-repeat", "no-repeat");

            }
        }
    }
}

function displayHits(gameObject){
 var computerHits = gameObject.computerHits.length;
 var playerHits= gameObject.playerHits.length;
 var computerMisses = gameObject.computerMisses.length;
 var playerMisses = gameObject.playerMisses.length;

 $("#computerHits").text(computerHits);
 $("#computerMisses").text(computerMisses);
 $("#playerHits").text(playerHits);
 $("#playerMisses").text(playerMisses);
}
