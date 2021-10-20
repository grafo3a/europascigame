package net.apasajb.europascigame.dao;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.apasajb.europascigame.services.GameService;


/**
 * Offers methods for updating players & points in database.
 * @author ApasaJB
 */

@Component
public class PlayersPersistence {
	
	@Autowired
	PlayersRepository playersRepository;
	
	@Autowired
	GameService gameService;
	
	
	private String persistenceError = "";
	final int maxNumberOfPlayers = 20;	// inclut Computer.
	
	
	public void checkUpdatePlayerInDatabase(String username) {
		
		Player playerEntity = playersRepository.findByCol02Username(username);
		
		if (playerEntity != null) {
			
			int playerPoints = playerEntity.getCol03Points();
			playerPoints++;
			
			playerEntity.setCol03Points(playerPoints);
			playersRepository.save(playerEntity);
			
		} else {
			// Si entité absente en BDD
			
			int numberOfPlayersInDatabase = (int) playersRepository.count();
			
			if (numberOfPlayersInDatabase < maxNumberOfPlayers) {
				
				Player player = new Player(username, 1);
				playersRepository.save(player);
				
			} else {
				
				persistenceError = " (ERROR: player " + username + 
						" not saved in database because the maximum number of players [" + maxNumberOfPlayers + "] is reached)";
			}
		}
	}
	
	
	public String getPlayersWithPoints() {
		
		String pointsLabel = gameService.getPointsLabel();
		String playersResultsString = "";
		
		ArrayList<String[]> playersWithPointsList = playersRepository.showPlayersWithPoints();
		
		if (playersWithPointsList.isEmpty() == false) {
			
			playersResultsString = pointsLabel + ":";
			
			for (String[] playerArray : playersWithPointsList) {
				
				String username = playerArray[0];
				String points = playerArray[1];
				String playerResult = "---" + username + "(" + points + ")";
				playersResultsString = playersResultsString + playerResult;
			}
		}
		
		return playersResultsString;
	}
	
	
	//=================== GETTERS & SETTERS =================//
	
	public String getPersistenceError() {
		return persistenceError;
	}
	
	
	public void setPersistenceError(String persistenceError) {
		this.persistenceError = persistenceError;
	}
	//==========================================================//
}
