package net.apasajb.europascigame.services;

import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import org.springframework.stereotype.Component;


/**
 * Offers methods wich process game choices of players and determine the winner of a round.
 * @author ApasaJB
 */
@Component
public class GameService {
	
	private String gameAction01;
	private String gameAction02;
	private String gameChoice01;
	private String gameChoice02;
	private String gamePlayer01;
	private String gamePlayer02;
	private String gameResult;
	private String winner = "None";
	private String winnerChoice;
	private String newMatchString;
	private String playerLanguage01 = "en";
	private String playerLanguage02 = "en";
	private final String gameSignature = "#x0j=R7h2@W1lFb4Sp9C+i1T*3mK6d4V$8z_N5g3Q%#";
	private int actionNumber;
	Locale locale = new Locale("en");
	
	// On cree un paquet de ressources / we create a resources bundle
	String baseName = "g11n/gameservice";							
	ResourceBundle paquet = ResourceBundle.getBundle(baseName, locale);
	
	public void setPlayerLanguage01() {
		
		String[] arrayGameAction01 = gameAction01.split("---");
		String playerLanguage01 = arrayGameAction01[4];
		this.setPlayerLanguage01(playerLanguage01.toLowerCase());
	}
	
	public void setPlayerLanguage02() {
		
		String[] arrayGameAction02 = gameAction02.split("---");
		String playerLanguage02 = arrayGameAction02[4];
		this.setPlayerLanguage02(playerLanguage02.toLowerCase());
	}
	
	public void updateGameLanguage() {
		
		if (playerLanguage01.equals(playerLanguage02)) {
			// Changement de langue si 2 joueurs ont la meme langue
			locale = new Locale.Builder()
					.setLanguage(playerLanguage02)
					.build();
			paquet = ResourceBundle.getBundle(baseName, locale);
			
		} else {
			locale = new Locale.Builder()
					.setLanguage("en")
					.build();
			paquet = ResourceBundle.getBundle(baseName, locale);
		}
		
		//On reinitialise les variables
		playerLanguage01 = "en";
		playerLanguage02 = "en";
	}
	
	public String respondMessagePlayer01(String roundForDisplay) {
		
		String m01_ROUND = paquet.getString("m01_ROUND");
		String m02_PLAYED = paquet.getString("m02_PLAYED");
		String responseMessage01 = "[" + m01_ROUND + " " + roundForDisplay + "] " + this.getGamePlayer01() + " " + m02_PLAYED;
		newMatchString = paquet.getString("m09_NEW_MATCH");
		
		return responseMessage01;
	}
	
	public void setGamePlayer01() {
		
		String[] arrayGamePlayers01 = gameAction01.split("---");
		String gamePlayer01 = arrayGamePlayers01[1];
		this.setGamePlayer01(gamePlayer01);
	}
	
	public void setGamePlayer02() {
		
		String[] arrayGamePlayers02 = gameAction02.split("---");
		String gamePlayer02 = arrayGamePlayers02[1];
		this.setGamePlayer02(gamePlayer02);
	}
	
	public void setGameChoices() {
		
		String[] arrayGameChoices01 = gameAction01.split("---");
		String gameChoice01 = arrayGameChoices01[3];
		this.setGameChoice01(gameChoice01);
		
		String[] arrayGameChoices02 = gameAction02.split("---");
		String gameChoice02 = arrayGameChoices02[3];
		this.setGameChoice02(gameChoice02);
	}
	
	public void findTheWinner(String roundForDisplay) {
		
		boolean isGameValid = true;
		winner = "None";
		winnerChoice = "";
		
		if (gamePlayer01.equals(gamePlayer02)) {
			//Implementation de Single-Player-Mode
			
			if (gameChoice01.equals(gameChoice02)) {
				//Robot devient gameplayer02
				gamePlayer02 = "Robot";
				gameChoice02 = this.getRandomGameChoice();
				
			} else {
				gameResult = paquet.getString("m03_FAULT") + " " + paquet.getString("m04_ADVICE");
				winner = "None";
				isGameValid = false;
			}
		}
		
		if (isGameValid) {
			String m01_ROUND = paquet.getString("m01_ROUND");
			String m06_WINNER = paquet.getString("m06_WINNER");
			String m07_NONE = paquet.getString("m07_NONE");
			String m08_DRAW = paquet.getString("m08_DRAW");
			
			if (gameChoice01.equals("Rock") && gameChoice02.equals("Paper")){
				winner = gamePlayer02;
				winnerChoice = gameChoice02;
				
			} else if (gameChoice01.equals("Rock") && gameChoice02.equals("Scissors")) {
				winner = gamePlayer01;
				winnerChoice = gameChoice01;
				
			} else if (gameChoice01.equals("Paper") && gameChoice02.equals("Scissors")) {
				winner = gamePlayer02;
				winnerChoice = gameChoice02;
				
			} else if (gameChoice01.equals("Paper") && gameChoice02.equals("Rock")) {
				winner = gamePlayer01;
				winnerChoice = gameChoice01;
				
			} else if (gameChoice01.equals("Scissors") && gameChoice02.equals("Rock")) {
				winner = gamePlayer02;
				winnerChoice = gameChoice02;
				
			} else if (gameChoice01.equals("Scissors") && gameChoice02.equals("Paper")) {
				winner = gamePlayer01;
				winnerChoice = gameChoice01;
				
			} else if (gameChoice01.equals(gameChoice02)) {
				winner = m07_NONE;
				winnerChoice = m08_DRAW;
				
			} else {
				winner = m07_NONE;
				winnerChoice = "";
			}
			
			gameResult = "[" + m01_ROUND + " " + roundForDisplay + "]##" +
					gamePlayer01 + "##" + gameChoice01 + "##vs##" + gamePlayer02 + "##" + gameChoice02 + "##" +
					m06_WINNER + "##" + winner + "##" + winnerChoice;
		}
	}
	
	public String getRandomGameChoice() {
		
		String randomGameChoice = null;
		Random randomObject = new Random();
		
		/*
		We generate a random number from 0 to 2 &
		We assign a choice to each possible random number.
		*/
		int randomNumber = randomObject.nextInt(3);
		
		switch (randomNumber) {
			case 0:
				randomGameChoice = "Rock";
				break;
				
			case 1:
				randomGameChoice = "Paper";
				break;
				
			case 2:
				randomGameChoice = "Scissors";
				break;
				
			default:
				break;
		}
		
		return randomGameChoice;
	}
	
	//=========================== GETTERS & SETTERS //
	public String getGameAction01() {
		return gameAction01;
	}
	
	public void setGameAction01(String gameAction01) {
		this.gameAction01 = gameAction01;
	}
	
	public String getGameAction02() {
		return gameAction02;
	}
	
	public void setGameAction02(String gameAction02) {
		this.gameAction02 = gameAction02;
	}
	
	public String getGameChoice01() {
		return gameChoice01;
	}
	
	public void setGameChoice01(String gameChoice01) {
		this.gameChoice01 = gameChoice01;
	}
	
	public String getGameChoice02() {
		return gameChoice02;
	}
	
	public void setGameChoice02(String gameChoice02) {
		this.gameChoice02 = gameChoice02;
	}
	
	public String getGameResult() {
		return gameResult;
	}
	
	public void setGameResult(String gameResult) {
		this.gameResult = gameResult;
	}
	
	public String getGamePlayer01() {
		return gamePlayer01;
	}
	
	public void setGamePlayer01(String gamePlayer01) {
		this.gamePlayer01 = gamePlayer01;
	}
	
	public String getGamePlayer02() {
		return gamePlayer02;
	}
	
	public void setGamePlayer02(String gamePlayer02) {
		this.gamePlayer02 = gamePlayer02;
	}
	
	public String getWinner() {
		return winner;
	}
	
	public void setWinner(String winner) {
		this.winner = winner;
	}
	
	public int getActionNumber() {
		return actionNumber;
	}
	
	public void setActionNumber(int actionNumber) {
		this.actionNumber = actionNumber;
	}
	
	public String getPlayerLanguage01() {
		return playerLanguage01;
	}
	
	public void setPlayerLanguage01(String playerLanguage01) {
		this.playerLanguage01 = playerLanguage01;
	}
	
	public String getPlayerLanguage02() {
		return playerLanguage02;
	}
	
	public void setPlayerLanguage02(String playerLanguage02) {
		this.playerLanguage02 = playerLanguage02;
	}
	
	public String getNewMatchString() {
		return newMatchString;
	}
	
	public String getGameSignature() {
		return gameSignature;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	//================================================//
}
