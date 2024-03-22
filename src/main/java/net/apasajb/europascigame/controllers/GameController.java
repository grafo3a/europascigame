package net.apasajb.europascigame.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import net.apasajb.europascigame.dao.PlayersPersistence;
import net.apasajb.europascigame.pojo.GameRequest;
import net.apasajb.europascigame.pojo.GameResponse;
import net.apasajb.europascigame.services.GameService;


/** 
 * Receives WebSocket messages, processes them and sends a response back to players (JavaScript).
 * @author ApasaJB
 */
@Controller
public class GameController {
	
	@Autowired
	GameService gameService;
	
	@Autowired
	PlayersPersistence playersPersistence;
	
	@MessageMapping("/hello")
	@SendTo("/topic/games")
	public GameResponse processPlayerMessage(GameRequest gameRequest) throws Exception {
		
		String playerMessage = HtmlUtils.htmlEscape(gameRequest.getPlayerMessage());
		String responseMessage = null;
		String gameSignature = gameService.getGameSignature();
		
		if (playerMessage.contains("---played" + gameSignature + "---")){
			String allPlayersWithPoints;
			int actionNumber = gameService.getActionNumber();
			
			/*
			currentMatchRound example: 1
			roundForDisplay example: "01"
			*/
			int currentMatchRound = playersPersistence.getCurrentMatchRound();
			String roundForDisplay = playersPersistence.getRoundForDisplay();
			
			try {
				if (actionNumber == 0) {
					gameService.setGameAction01(playerMessage);
					gameService.setGamePlayer01();
					gameService.setActionNumber(1);
					gameService.setPlayerLanguage01();
					allPlayersWithPoints = playersPersistence.getPlayersResultsString();
					
					/*
					responseMessage is used in the front-end page as follows:
					[0] respondMessagePlayer01(*) = message
					[1] currentMatchRound = round
					[2] allPlayersWithPoints = stats
					[3] getChampion() = champion
					*/
					responseMessage = gameService.respondMessagePlayer01(roundForDisplay) +
							"***" + currentMatchRound +
							"***" + allPlayersWithPoints +
							"***" + playersPersistence.getChampion();
					
				} else if (actionNumber == 1) {
					String winner;
					String persistenceError;
					
					gameService.setGameAction02(playerMessage);
					gameService.setGamePlayer02();
					gameService.setActionNumber(0);
					gameService.setGameChoices();
					gameService.setPlayerLanguage02();
					gameService.updateGameLanguage();
					
					// On alimente la variable gameResult
					gameService.findTheWinner(roundForDisplay);
					winner = gameService.getWinner();
					
					// On supprime l'erreur precedente
					playersPersistence.setPersistenceError("");
					
					if (winner.equalsIgnoreCase("none") == false &&
						winner.equalsIgnoreCase("aucun") == false &&
						winner.equalsIgnoreCase("nikt") == false) {
						
						playersPersistence.checkUpdatePlayerInDatabase(winner);
					}
					
					allPlayersWithPoints = playersPersistence.getPlayersWithPoints();
					persistenceError = playersPersistence.getPersistenceError();
					
					/*
					responseMessage is used in the front-end page as follows:
					[0] getGameResult() = message
					[1] currentMatchRound = round
					[2] allPlayersWithPoints = stats
					[3] getChampion() = champion
					[4] persistenceError = errors
					[5] gameSignature = signature
					*/
					responseMessage = gameService.getGameResult() +
							"***" + currentMatchRound +
							"***" + allPlayersWithPoints +
							"***" + playersPersistence.getChampion() +
							"***" + persistenceError +
							"***" + gameSignature;
				}
				
			} catch (Exception ex) {
				responseMessage = "#-- ERROR: " + ex.getMessage();
			}
			
		} else {
			responseMessage = playerMessage;
		}
		
		GameResponse gameResponse = new GameResponse(HtmlUtils.htmlUnescape(responseMessage));
		Thread.sleep(1000); // 1 second of simulated delay
		
		return gameResponse;
	}
}
