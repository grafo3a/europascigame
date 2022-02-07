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
			int currentMatchRound = playersPersistence.getCurrentMatchRound();			// ex. 1
			String roundForDisplay = playersPersistence.getRoundForDisplay();			// ex. "01"
			
			try {
				if (actionNumber == 0) {
					
					gameService.setGameAction01(playerMessage);
					gameService.setGamePlayer01();
					gameService.setActionNumber(1);
					gameService.setPlayerLanguage01();
					allPlayersWithPoints = playersPersistence.getPlayersResultsString();
					responseMessage = gameService.respondMessagePlayer01(roundForDisplay) +		//[0] message
							"***" + currentMatchRound +											//[1] round
							"***" + allPlayersWithPoints +										//[2] stats
							"***" + playersPersistence.getChampion();							//[3] champion
				
				} else if (actionNumber == 1) {
					
					String winner;
					String persistenceError;
					
					gameService.setGameAction02(playerMessage);
					gameService.setGamePlayer02();
					gameService.setActionNumber(0);
					gameService.setGameChoices();
					gameService.setPlayerLanguage02();
					gameService.updateGameLanguage();
					gameService.findTheWinner(roundForDisplay);				// Cette action alimente la variable gameResult
					
					winner = gameService.getWinner();
					playersPersistence.setPersistenceError("");				// On supprime l'erreur precedente
					
					if (winner.equalsIgnoreCase("none") == false &&
						winner.equalsIgnoreCase("aucun") == false &&
						winner.equalsIgnoreCase("nikt") == false) {
						
						playersPersistence.checkUpdatePlayerInDatabase(winner);
					}
					
					allPlayersWithPoints = playersPersistence.getPlayersWithPoints();
					persistenceError = playersPersistence.getPersistenceError();
					
					responseMessage = gameService.getGameResult() +						//[0] message
							"***" + currentMatchRound +									//[1] round
							"***" + allPlayersWithPoints +								//[2] stats
							"***" + playersPersistence.getChampion() +					//[3] champion
							"***" + persistenceError +									//[4] errors
							"***" + gameSignature;										//[5] signature
				}
				
			} catch (Exception ex) {
				responseMessage = "#--- ERROR: " + ex.getMessage();
			}
			
		} else {
			responseMessage = playerMessage;
		}
		
		GameResponse gameResponse = new GameResponse(HtmlUtils.htmlUnescape(responseMessage));
		Thread.sleep(1000); // 1 second of simulated delay
		
		return gameResponse;
	}
}
