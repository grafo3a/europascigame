//====================================================

var stompClient = null;
var gameSignature = "#x0j=R7h2@W1lFb4Sp9C+i1T*3mK6d4V$8z_N5g3Q%#";


function startGame(){
	enableRopasButtons();
	$("#startGame").prop("disabled", "true");
	$("#stopGame").prop("disabled", !"true");
}


function playRock(){
	let pseudonyme = $("#labelUserName").text();
	let playerLanguage = $("#labelLanguage").text();
	stompClient.send("/app/hello", {}, JSON.stringify(
		{'playerMessage': '#---' + pseudonyme + '---played' + gameSignature + '---Rock---' + playerLanguage}));
		
	disableRopasButtons();
	$("#startGame").prop("disabled", !"true");
}


function playPaper(){
	let pseudonyme = $("#labelUserName").text();
	let playerLanguage = $("#labelLanguage").text();
	
	stompClient.send("/app/hello", {}, JSON.stringify(
		{'playerMessage': '#---' + pseudonyme + '---played' + gameSignature + '---Paper---' + playerLanguage}));
		
	disableRopasButtons();
	$("#startGame").prop("disabled", !"true");
}


function playScissors(){
	let pseudonyme = $("#labelUserName").text();
	let playerLanguage = $("#labelLanguage").text();
	
	stompClient.send("/app/hello", {}, JSON.stringify(
		{'playerMessage': '#---' + pseudonyme + '---played' + gameSignature + '---Scissors---' + playerLanguage}));
		
	disableRopasButtons();
	$("#startGame").prop("disabled", !"true");
}


function disableRopasButtons(){
	$("#playRock").prop("disabled", "true");
	$("#playPaper").prop("disabled", "true");
	$("#playScissors").prop("disabled", "true");
}


function enableRopasButtons(){
	$("#playRock").prop("disabled", !"true");
	$("#playPaper").prop("disabled", !"true");
	$("#playScissors").prop("disabled", !"true");
}


// Envoi d'un message vers le serveur Java

function sendOpinion() {
	
	if(($( "#opinion").val() ) === ""){
		stompClient.send("/app/hello", {}, JSON.stringify({'playerMessage': $("#labelUserName").text() + ": [Empty]"}));
		$("#opinion").val('');
		
	} else {
		stompClient.send("/app/hello", {}, JSON.stringify({'playerMessage': $("#labelUserName").text() + ": " + $("#opinion").val()}));
		$("#opinion").val('');
    }
}


function setUserName(){
	
	if(($( "#newPlayerNameInput").val() ) === ""){
		$("#labelUserName").html("[Visitor]");

	} else if (($( "#newPlayerNameInput").val().toLowerCase() ) === "computer"){
		$("#labelUserName").html("[komputer]");
		
	} else {
		$("#labelUserName").html( $( "#newPlayerNameInput").val() );
		$("#newPlayerNameInput").hide();
		$("#userNameButton").hide();
	}
}


function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#conversation").show();
    $("#games").html("");
}


function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/games', function (serverMessage) {
            showGame(serverMessage);
        });
    });
    
    disableRopasButtons();
    $("#stopGame").prop("disabled", "true");
}


// Affichage des infos reçues du serveur Java

function showGame(serverMessage) {
	
	let message = JSON.parse(serverMessage.body).serverMessage;
	let messageArray = message.split('*****');
	let message01 = messageArray[0];		// Main message
	let message02 = messageArray[1];		// Results / Stats
	let message03 = messageArray[2];		// Persistence Error
	
	//let messageArray01 = message01.split('---');
	
	let presentationPL = "Zwycięzca jest:";
	let presentationEN = "The winner is:";
	let presentationFR = "Le/La gagnant[e] est:";
	
	if ((message01.includes(presentationPL) ||
		message01.includes(presentationEN) ||
		message01.includes(presentationFR)) &&
		message.includes(gameSignature)) {
		
		let messageArray01 = message01.split('---');
		
		let winnerInfo = messageArray01[6];
		let winnerInfoColor = 'black';
		
		if ( winnerInfo.includes('Rock')){
			winnerInfoColor = "red";
			
		} else if (winnerInfo.includes('Paper')){
			winnerInfoColor = "blue";
		
		} else if (winnerInfo.includes('Scissors')){
			winnerInfoColor = "green";
		}
		
		let winnerInfoWithColor = "<font color='" + winnerInfoColor + "'>" + winnerInfo + "</font>";
		message01 = message01.replace(winnerInfo, winnerInfoWithColor);
		message01 = "<i>" + message01 + "</i>";
    }
	
	$("#games").append("<tr><td>" + message01 + "</td></tr>");
    $("#labelPlayersList").html(message02);
    $("#labelResultsError").html(message03);
}


function setLanguagePL(){
	
	$("#labelWelcome").html("Witamy do EuropasciGame");
	$("#labelYouAre").html("Jesteś:");
	$("#userNameButton").html("Zaloguj się");
	$("#labelObserve").html("Obserwuj tutaj");
	$("#labelChat").html("Czatuj tutaj");
	$("#btnSend").html("Wyślij");
	$("#labelControl").html("Zaczynaj tutaj (1)");
	$("#labelPlay").html("Graj tutaj (2)");
	$("#labelLanguage").html("PL");
	$("#btnPL").prop("disabled", "true");
	$("#btnEN").prop("disabled", !"true");
	$("#btnFR").prop("disabled", !"true");
}


function setLanguageEN(){
	
	$("#labelWelcome").html("Welcome to EuropasciGame!");
	$("#labelYouAre").html("You are:");
	$("#userNameButton").html("Sign in");
	$("#labelObserve").html("Observe here");
	$("#labelChat").html("Chat here");
	$("#btnSend").html("Send");
	$("#labelControl").html("Start here (1)");
	$("#labelPlay").html("Play here (2)");
	$("#labelLanguage").html("EN");
	$("#btnEN").prop("disabled", "true");
	$("#btnFR").prop("disabled", !"true");
	$("#btnPL").prop("disabled", !"true");
}


function setLanguageFR(){
	
	$("#labelWelcome").html("Bienvenue à EuropasciGame");
	$("#labelYouAre").html("Tu es:");
	$("#userNameButton").html("Se connecter");
	$("#labelObserve").html("Observe ici");
	$("#labelChat").html("Tchate ici");
	$("#btnSend").html("Envoyer");
	$("#labelControl").html("Commencer ici (1)");
	$("#labelPlay").html("Jouer ici (2)");
	$("#labelLanguage").html("FR");
	$("#btnFR").prop("disabled", "true");
	$("#btnPL").prop("disabled", !"true");
	$("#btnEN").prop("disabled", !"true");
}


//Point de départ quand le DOM est pret.

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    
    $( "#startGame" ).click(function() { startGame(); });
    $( "#btnSend" ).click(function() { sendOpinion(); });
    $( "#userNameButton" ).click(function() { setUserName(); });
    
    // Changement de langue
    
    $( "#btnPL" ).click(function() { setLanguagePL(); });
    $( "#btnEN" ).click(function() { setLanguageEN(); });
    $( "#btnFR" ).click(function() { setLanguageFR(); });
    $("#btnEN").prop("disabled", "true");
    
    // Boutons de jeu
    
    $( "#playRock" ).click(function() { playRock(); });
    $( "#playPaper" ).click(function() { playPaper(); });
    $( "#playScissors" ).click(function() { playScissors(); });
    
    connect("true");
    console.log("--- Debug mode is disabled in production.");
    
    //Pour activer le mode debug, commentez la ligne suivante.
    console.log = function() {}
});
