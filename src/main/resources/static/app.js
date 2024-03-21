//----------------------------------------------------------
var newMatchPL = "NOWY MECZ!";
var newMatchEN = "NEW MATCH!";
var newMatchFR = "NOUVEAU MATCH!";
var goodLuckPL = "POWODZENIA!";
var goodLuckEN = "GOOD LUCK!";
var goodLuckFR = "BONNE CHANCE!";
var messagesBackgroundColor = "#ccffdd";
var bravoChampPL = "Brawo! now[y/a] mistrz[yni]";
var bravoChampEN = "Bravo! new champion";
var bravoChampFR = "Bravo! nouve[au/lle] champion[ne]";
//-----------------------------------------------------------
var stompClient = null;
var gameSignature = "#x0j=R7h2@W1lFb4Sp9C+i1T*3mK6d4V$8z_N5g3Q%#";
var championPoints = 12;
//------------------------------------------------------------


function startGame() {
	
	enableRopasButtons();
	$("#startGame").prop("disabled", "true");
	$("#stopGame").prop("disabled", !"true");
}


function playRock() {
	
	let pseudonyme = $("#labelUserName").text();
	let playerLanguage = $("#labelLanguage").text();
	stompClient.send("/app/hello", {}, JSON.stringify(
		{'playerMessage': '#---' + pseudonyme + '---played' + gameSignature + '---Rock---' + playerLanguage}));
		
	disableRopasButtons();
	$("#startGame").prop("disabled", !"true");
}


function playPaper() {
	
	let pseudonyme = $("#labelUserName").text();
	let playerLanguage = $("#labelLanguage").text();
	
	stompClient.send("/app/hello", {}, JSON.stringify(
		{'playerMessage': '#---' + pseudonyme + '---played' + gameSignature + '---Paper---' + playerLanguage}));
		
	disableRopasButtons();
	$("#startGame").prop("disabled", !"true");
}


function playScissors() {
	
	let pseudonyme = $("#labelUserName").text();
	let playerLanguage = $("#labelLanguage").text();
	
	stompClient.send("/app/hello", {}, JSON.stringify(
		{'playerMessage': '#---' + pseudonyme + '---played' + gameSignature + '---Scissors---' + playerLanguage}));
		
	disableRopasButtons();
	$("#startGame").prop("disabled", !"true");
}


function disableRopasButtons() {
	
	$("#playRock").prop("disabled", "true");
	$("#playPaper").prop("disabled", "true");
	$("#playScissors").prop("disabled", "true");
}


function enableRopasButtons() {
	
	$("#playRock").prop("disabled", !"true");
	$("#playPaper").prop("disabled", !"true");
	$("#playScissors").prop("disabled", !"true");
}


// Envoi d'un message vers le serveur Java

function sendOpinion() {
	
	if(($( "#opinion").val() ) === "") {
		stompClient.send("/app/hello", {}, JSON.stringify({'playerMessage': $("#labelUserName").text() + ": [Empty]"}));
		$("#opinion").val('');
		
	} else {
		stompClient.send("/app/hello", {}, JSON.stringify({'playerMessage': $("#labelUserName").text() + ": " + $("#opinion").val()}));
		$("#opinion").val('');
    }
}


function setUserName() {
	
	if(($( "#newPlayerNameInput").val() ) === "") {
		$("#labelUserName").html("[Visitor]");

	} else if (($( "#newPlayerNameInput").val().toLowerCase() ) === "robot") {
		$("#labelUserName").html("[robot]");
		
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
	
	//On decoupe le message reçu
	let messageArray = message.split('***');
	let mainMessage = messageArray[0];
	let numberOfTheRoundAsString = messageArray[1];
	let playersAndPoints = messageArray[2];
	let championName = messageArray[3];
	let persistenceError = messageArray[4];
		
	//On affichage les images de debut du tour de jeu
	let playedPL = "grał[a]";
	let playedEN = "played";
	let playedFR = "a joué";
	
	// On gere le 1er jeu
	if (mainMessage.includes(playedPL) ||
		mainMessage.includes(playedEN) ||
		mainMessage.includes(playedFR)) {
		
		$("#gameImage01").attr("src", "images/temporary.png");
		$("#gameImage02").attr("src", "images/temporary.png");
		
		//On efface les noms des joueurs
		$("#playerName01").html("____");
		$("#playerName02").html("____");
	}
	
	let winnerStringPL = "Zwycięzca";
	let winnerStringEN = "Winner";
	let winnerStringFR = "Gagnant[e]";
	
	if ((mainMessage.includes(winnerStringPL) ||
		mainMessage.includes(winnerStringEN) ||
		mainMessage.includes(winnerStringFR)) &&
		message.includes(gameSignature)) {
		
		/*
		Exemple mainMessage: "[Round 04] End.##winner##WINNERNAME##Rock##Player01##Rock##Player02##Paper"
		*/
		
		let messageArray01 = mainMessage.split('##');
		let roundString = messageArray01[0];
		let player01 = messageArray01[1];
		let choice01 = messageArray01[2];										//let "vs" = messageArray01[3];
		let player02 = messageArray01[4];										//let choice02 = messageArray01[5];
		let winnerString = messageArray01[6];
		let winnerName = messageArray01[7];
		let winnerChoice = messageArray01[8];
		
		// on colore les infos du gagnant/de la gagnante
		let winnerInfo = winnerName + " (" + winnerChoice + ")"
		let winnerInfoColor = 'black';		
		
		if ( winnerChoice.includes('Rock')) {
			winnerInfoColor = "red";
			
		} else if (winnerChoice.includes('Paper')) {
			winnerInfoColor = "blue";
			
		} else if (winnerChoice.includes('Scissors')) {
			winnerInfoColor = "green";
		}
		
		// AFFICHAGE DES NOMS DES JOUEURS
		let pseudonyme = $("#labelUserName").html();
		
		// On assigne les places
		if (pseudonyme == player01) {											// Si je suis joueur, je prends la place 01
			
			$("#playerName01").html(player01);
			$("#playerName02").html(player02);
			
		} else if (pseudonyme == player02) {
			
			$("#playerName01").html(player02);
			$("#playerName02").html(player01);
			
		} else {
			
			$("#playerName01").html(player01);
			$("#playerName02").html(player02);
		}
		
		//On affiche les images du defi
		if (winnerName == pseudonyme) {											// Si je gagne
			
			if (winnerChoice == 'Rock') {										//Rock > Scissors
				
				$("#gameImage01").attr("src", "images/rock01a.png");
				$("#gameImage02").attr("src", "images/scissors03b.png");
			
			} else if ( winnerChoice == 'Paper' ) {								//Paper -> Rock
				
				$("#gameImage01").attr("src", "images/paper01a.png");
				$("#gameImage02").attr("src", "images/rock03b.png");
				
			} else if ( winnerChoice == 'Scissors' ) {							//Scissors > Paper
				
				$("#gameImage01").attr("src", "images/scissors01a.png");
				$("#gameImage02").attr("src", "images/paper03b.png");
			}
		
		} else if (winnerName == "None" || winnerName == "Nikt" || winnerName == "Aucun") {
				
				if (choice01 == "Rock") {										//Rock > Scissors
					
					$("#gameImage01").attr("src", "images/rock02a.png");
					$("#gameImage02").attr("src", "images/rock02b.png");
				
				} else if (choice01 == "Paper") {								//Paper -> Rock
					
					$("#gameImage01").attr("src", "images/paper02a.png");
					$("#gameImage02").attr("src", "images/paper02b.png");
				
				} else if (choice01 == "Scissors") {							//Scissors > Paper
					
					$("#gameImage01").attr("src", "images/scissors02a.png");
					$("#gameImage02").attr("src", "images/scissors02b.png");
				}
				
		} else {																//if winnerName != me && != none
			
			if (winnerChoice == "Rock") {										//Rock > Scissors
				
				$("#gameImage01").attr("src", "images/scissors03a.png");
				$("#gameImage02").attr("src", "images/rock01b.png");
				
			} else if (winnerChoice == "Paper") {								//Paper -> Rock
				
				$("#gameImage01").attr("src", "images/rock03a.png");
				$("#gameImage02").attr("src", "images/paper01b.png");
				
			} else if (winnerChoice == "Scissors") {							//Scissors > Paper
				
				$("#gameImage01").attr("src", "images/rock03a.png");
				$("#gameImage02").attr("src", "images/scissors01b.png");
			}
		}
		
		let winnerInfoWithColor = winnerString + ": <font color='" + winnerInfoColor + "'>" + winnerInfo + "</font>";
		
	mainMessage = roundString + " " + winnerInfoWithColor;						//On prepare le message pour affichage dans le tchat
	}
	
	$("#games").append("<tr><td>" + mainMessage + "</td></tr>");				//On affiche le message dans le tchat
	
	
	//AFFICHAGE DE BRAVO, NOM DE CHAMPION & STATS DU MATCH
	
	let round = parseInt(numberOfTheRoundAsString);
	let languageCode = $("#labelLanguage").html();
	
	if (round < 3 && message.includes(gameSignature)) {
		$("#labelNewMatch").css('color', 'blue');								// est visible
		
		if (round == 1) {
			
			$("#labelPlayersList").html("[...]");
			sayNewMatch(languageCode);											//PL, EN, FR.
			
		} else if (round == 2) {
			
			$("#labelPlayersList").html(playersAndPoints);
			sayGoodLuck(languageCode);		//PL, EN ou FR
		}
		
	} else if (round >= 3 && message.includes(gameSignature)) {
		
		$("#labelNewMatch").css('color', messagesBackgroundColor);				// devient invisible
		$("#labelNewMatch").html("/");											// devient invisible en gardant la ligne presente
		$("#labelPlayersList").html(playersAndPoints);
		sayPoints();
	}
	
	if (championName == 'none') {
		
		if (languageCode == 'PL') {
			championName = 'nikt';
		
		} else if (languageCode == 'FR') {
			championName = 'aucun';
		}
	}
	
	$("#labelChampName").css('color', 'black');
	$("#labelChampName").html(championName);
	$("#labelResultsError").html(persistenceError);
	
	//On felicite le/la nouve[au/lle] champion[ne] si championPoints atteint
	
	let maxPointsString = playersAndPoints.split('---')[1];
	maxPointsString = maxPointsString.split('(')[1];
	maxPointsString = maxPointsString.split(')')[0];
	
	let maxPoints = parseInt(maxPointsString);
	
	if (maxPoints == championPoints) {
		sayBravoChampion(championName);
	}
}


function sayNewMatch(langue) {
	
	if (langue == "PL") {
		$("#labelNewMatch").html(newMatchPL);
		
	} else if (langue == "EN") {
		$("#labelNewMatch").html(newMatchEN);
	
	} else if (langue == "FR") {
		$("#labelNewMatch").html(newMatchFR);
	}
}


function sayGoodLuck(langue) {
	
	if (langue == "PL") {
		$("#labelNewMatch").html(goodLuckPL);
		
	} else if (langue == "EN") {
		$("#labelNewMatch").html(goodLuckEN);
	
	} else if (langue == "FR") {
		$("#labelNewMatch").html(goodLuckFR);
	}
}


function sayPoints() {
	
	if ($("#labelLanguage").html() == "PL" ) {
		$("#labelPoints").html("Punkty: ");
		
	} else {
		$("#labelPoints").html("Points: ");
	}
}


function sayBravoChampion(newChampName) {
	
	newChampName = newChampName.toUpperCase();
	let langue = $("#labelLanguage").html();
	$("#labelNewMatch").css('color', 'blue');
	
	if (langue == "PL") {
		$("#labelNewMatch").html(bravoChampPL + ": " + newChampName);
	
	} else if (langue == "EN") {
		$("#labelNewMatch").html(bravoChampEN + ": " + newChampName);
	
	} else if (langue == "FR") {
		$("#labelNewMatch").html(bravoChampFR + ": " + newChampName);
	}
}


function setLanguagePL() {
	
	$("#labelWelcome").html("Witamy do EuropasciGame");
	$("#labelYouAre").html("Jesteś: ");
	$("#userNameButton").html("Zaloguj się");
	$("#labelChampion").html("obecn[y/a] mistrz[yni]: ");
	$("#labelChat").html("Czatuj tutaj");
	$("#btnSend").html("Wyślij");
	$("#labelPlay").html("Graj tutaj");
	$("#labelSayLanguage").html("Język: ");
	$("#labelLanguage").html("PL");
	$("#btnPL").prop("disabled", "true");
	$("#btnEN").prop("disabled", !"true");
	$("#btnFR").prop("disabled", !"true");
	
	if ($("#labelChampName").html() == 'none' ||
		$("#labelChampName").html() == 'aucun[e]') {
		
		$("#labelChampName").html("nikt");
	}
	
	if (($("#labelPoints").html()).includes('Points')) {
		$("#labelPoints").html("Punkty: ");
	}
	
	if ( $("#labelNewMatch").html() == newMatchEN ||
		$("#labelNewMatch").html() == newMatchFR ) {
		
		sayNewMatch("PL");
	}
	
	if ( $("#labelNewMatch").html() == goodLuckEN ||
		$("#labelNewMatch").html() == goodLuckFR ) {
		
		sayGoodLuck("PL");
	}
	
	translateBravo("PL");
}


function setLanguageEN() {
	
	$("#labelWelcome").html("Welcome to EuropasciGame!");
	$("#labelYouAre").html("You are: ");
	$("#userNameButton").html("Sign in");
	$("#labelChampion").html("Current champion: ");
	$("#labelChat").html("Chat here");
	$("#btnSend").html("Send");
	$("#labelPlay").html("Play here");
	$("#labelSayLanguage").html("Language: ");
	$("#labelLanguage").html("EN");
	$("#btnEN").prop("disabled", "true");
	$("#btnFR").prop("disabled", !"true");
	$("#btnPL").prop("disabled", !"true");
	
	if ($("#labelChampName").html() == 'nikt' ||
		$("#labelChampName").html() == 'aucun[e]') {
		
		$("#labelChampName").html("none");
	}
	
	if (($("#labelPoints").html()).includes('Punkty')) {
		$("#labelPoints").html("Points: ");
	}
	
	if ( $("#labelNewMatch").html() == newMatchPL ||
		$("#labelNewMatch").html() == newMatchFR ) {
		
		sayNewMatch("EN");
	}
	
	if ( $("#labelNewMatch").html() == goodLuckPL ||
		$("#labelNewMatch").html() == goodLuckFR ) {
		
		sayGoodLuck("EN");
	}
	
	translateBravo("EN");
}


function setLanguageFR() {
	
	$("#labelWelcome").html("Bienvenue à EuropasciGame");
	$("#labelYouAre").html("Tu es: ");
	$("#userNameButton").html("Se connecter");
	$("#labelChampion").html("Champion[ne] actuel[le]: ");
	$("#labelChat").html("Tchate ici");
	$("#btnSend").html("Envoyer");
	$("#labelPlay").html("Joue ici");
	$("#labelSayLanguage").html("Langue: ");
	$("#labelLanguage").html("FR");
	$("#btnFR").prop("disabled", "true");
	$("#btnPL").prop("disabled", !"true");
	$("#btnEN").prop("disabled", !"true");
	
	if ($("#labelChampName").html() == 'nikt' ||
		$("#labelChampName").html() == 'none') {
		
		$("#labelChampName").html("aucun[e]");
	}
	
	if (($("#labelPoints").html()).includes('Punkty')) {
		$("#labelPoints").html("Points: ");
	}
	
	if ( $("#labelNewMatch").html() == newMatchPL ||
		$("#labelNewMatch").html() == newMatchEN ) {
		
		sayNewMatch("FR");
	}
	
	if ( $("#labelNewMatch").html() == goodLuckPL ||
		$("#labelNewMatch").html() == goodLuckEN ) {
		
		sayGoodLuck("FR");
	}
	
	translateBravo("FR");
}


function translateBravo(newLanguageCode) {
	
	if ( $("#labelNewMatch").html().includes("Brawo!") ||
			$("#labelNewMatch").html().includes("Bravo!")) {
		
		let bravo = $("#labelNewMatch").html();
		let bravoArray = bravo.split(" ");
		let newChampion = bravoArray[3];
		
		if (newLanguageCode == "PL") {
			$("#labelNewMatch").html(bravoChampPL + ": " + newChampion)
		
		} else if(newLanguageCode == "EN") {
			$("#labelNewMatch").html(bravoChampEN + ": " + newChampion)
		
		} else if(newLanguageCode == "FR") {
			$("#labelNewMatch").html(bravoChampFR + ": " + newChampion)
		
		} else {}
	}
}


function showHelp() {
	
	let help = "To play, click \"start\" then make a choice (rock, paper or scissors)." + 
		"\n\nTo play in Single-Player-Mode: play twice a same choice (rock-rock, paper-paper, scissors-scissors)." +
		"\n\nIn case of connection failure, refresh the web page." +
		"\nMaximum number of players: 10." +
		"\nPoints needed to become champion: 12.";
		
	alert(help);
}


function describeApp() {
	
	let appInfo = "EuropasciGame is an implementation of the famous game \"rock-paper-scissors\", developed by Joseph B. Apasa." +
		"\n" +
		"\nThe main technologies used in the project are: " +
		"Java 8, Spring Boot 2.5, WebSockets, Spring Data JPA, H2 Database (in memory)/PostgreSQL (in cloud), JUnit 5, Maven, HTML, CSS & BootStrap, JavaScript & JQuery." +
		"\n" +
		"\nThe game can be played in Polish, English, and French." + 
		"\nAny player can join a match at any round." +
		"\nA player can play against a human or a robot opponent." +
		"\nUp to 10 players can play in a same match." +
		"\nA player needs 12 points to become champion." +
		"\n" +
		"\nThe project is available on GitHub at https://github.com/grafo3a/europascigame/.";
		
	alert(appInfo);
}


function contactUs() {
	alert("An email address will be available later for contact.");
}


//-----------------------------------------------------------
//Point de départ quand le DOM est pret.

$(function () {
	
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    
    $( "#startGame" ).click(function() { startGame(); });
    $( "#btnSend" ).click(function() { sendOpinion(); });
    $( "#userNameButton" ).click(function() { setUserName(); });
    
    $( "#btnPL" ).click(function() { setLanguagePL(); });
    $( "#btnEN" ).click(function() { setLanguageEN(); });
    $( "#btnFR" ).click(function() { setLanguageFR(); });
    $( "#btnEN" ).prop("disabled", "true");
    
    $( "#playRock" ).click(function() { playRock(); });
    $( "#playPaper" ).click(function() { playPaper(); });
    $( "#playScissors" ).click(function() { playScissors(); });
	
	$( "#help").click(function(){ showHelp() });
	$( "#about").click(function(){ describeApp() });
    $( "#contact").click(function(){ contactUs() });
	
    connect("true");
    console.log("--- Debug mode is disabled in production.");
    
    //Pour activer le mode debug, commentez la ligne suivante/To enable debug mode, comment the following line
    console.log = function() {}
});
