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
	private String champion = "none";
	private String playersResultsString = "";
	private final int maxNumberOfPlayers = 10;									// inclut Robot.
	private final int maxPoints = 12;											// Pour devenir champion
	private int currentMatchRound = 1;
	private String roundForDisplay = "01";
	
	
	public void checkUpdatePlayerInDatabase(String username) {
		
		Player playerEntity = playersRepository.findByCol02Username(username);
		
		if (playerEntity != null) {												//Si l'entité est presente en BDD
			
			int playerPoints = playerEntity.getCol03Points();
			playerPoints++;
			
			playerEntity.setCol03Points(playerPoints);
			playersRepository.save(playerEntity);
			
		} else {																//Si l'entité est absente en BDD, on la cree
			
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
		
		currentMatchRound++;
		roundForDisplay = Integer.toString(currentMatchRound);
		
		if (currentMatchRound < 10) {											// Pour obtenir le format "01"
			roundForDisplay = "0" + currentMatchRound;
		}
		
		playersResultsString = "";
		ArrayList<String[]> playersWithPointsList = playersRepository.showPlayersWithPoints();
		
		if (playersWithPointsList.isEmpty() == false) {
			for (String[] playerArray : playersWithPointsList) {
				
				String username = playerArray[0];
				String points = playerArray[1];
				playersResultsString = playersResultsString + "---" + username + "(" + points + ")";
				
				if (Integer.parseInt(points) >= maxPoints) {					// si on a 12 points, on devient champion.
					
					champion = username;
					playersRepository.deleteAll();								//on reinitialise les points en BDD
					currentMatchRound = 1;
					roundForDisplay = "01";
				}
			}
		
		} else {																//Si la BDD est vide
			playersResultsString = "[...]";
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
	
	
	public String getChampion() {
		return champion;
	}
	
	
	public String getPlayersResultsString() {
		return playersResultsString;
	}
	
	
	public void setPlayersResultsString(String playersResultsString) {
		this.playersResultsString = playersResultsString;
	}
	
	
	public String getRoundForDisplay() {
		return roundForDisplay;
	}
	
	
	public int getCurrentMatchRound() {
		return currentMatchRound;
	}
	
	
	public void setCurrentMatchRound(int currentMatchRound) {
		this.currentMatchRound = currentMatchRound;
	}
	
	//==========================================================//
}
