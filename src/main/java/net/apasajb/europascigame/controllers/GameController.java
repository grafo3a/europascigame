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
		
		Thread.sleep(1000); // 1 second of simulated delay
		String playerMessage = HtmlUtils.htmlEscape(gameRequest.getPlayerMessage());
		String responseMessage = null;
		
		final String gameSignature = "#x0j=R7h2@W1lFb4Sp9C+i1T*3mK6d4V$8z_N5g3Q%#";
		
		if (playerMessage.contains("---played" + gameSignature + "---")) {
			
			int actionNumber = gameService.getActionNumber();
			
			try {
				
				if(actionNumber == 0) {
				
					gameService.setGameAction01(playerMessage);
					gameService.setGamePlayer01();
					gameService.setActionNumber(1);
					gameService.setPlayerLanguage01();
					
					gameService.respondMessagePlayer01();
					
					responseMessage = gameService.respondMessagePlayer01();
					
				} else if (actionNumber == 1) {
					
					String winner;
					String allPlayersWithPoints;
					String persistenceError;
					
					gameService.setGameAction02(playerMessage);
					gameService.setGamePlayer02();
					gameService.setActionNumber(0);
					gameService.setGameChoices();
					gameService.setPlayerLanguage02();
					gameService.updateGameLanguage();
					gameService.findTheWinner();
					
					winner = gameService.getWinner();
					playersPersistence.setPersistenceError(""); // On supprime l'erreur precedente
					
					if (winner.equalsIgnoreCase("none") == false &&
							winner.equalsIgnoreCase("aucun") == false &&
							winner.equalsIgnoreCase("nikt") == false) {
						
						playersPersistence.checkUpdatePlayerInDatabase(winner);
					}
					
					allPlayersWithPoints = playersPersistence.getPlayersWithPoints();
					persistenceError = playersPersistence.getPersistenceError();
					
					responseMessage = gameService.getGameResult() + 
							"*****" + allPlayersWithPoints +
							"*****" + persistenceError +
							"*****" + gameSignature;
				}
				
			} catch (Exception ex) {
				
				responseMessage = "#--- ERROR: " + ex.getMessage();
			}
			
		} else {
			
			responseMessage = playerMessage;
		}
		
		GameResponse gameResponse = new GameResponse(HtmlUtils.htmlUnescape(responseMessage));
		
		return gameResponse;
	}
}
