package net.apasajb.europascigame.services;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class GameServiceTests {
	
	String player01 = "Player01";
	String player02 = "Player02";
	
	
	@Test
	void findTheWinnerRockEtc() {
		
		GameService testGameService = new GameService();
		testGameService.setGamePlayer01(player01);
		testGameService.setGamePlayer02(player02);
		
		testGameService.setGameChoice01("Rock");
		testGameService.setGameChoice02("Paper");
		testGameService.findTheWinner("01");
		
		assertEquals(player02, testGameService.getWinner());
		
		testGameService.setGameChoice01("Rock");
		testGameService.setGameChoice02("Scissors");
		testGameService.findTheWinner("01");
		
		assertEquals(player01, testGameService.getWinner());
	}
	
	
	@Test
	void findTheWinnerPaperEtc() {
		
		GameService testGameService = new GameService();
		testGameService.setGamePlayer01(player01);
		testGameService.setGamePlayer02(player02);
		
		testGameService.setGameChoice01("Paper");
		testGameService.setGameChoice02("Scissors");
		testGameService.findTheWinner("01");
		
		assertEquals(player02, testGameService.getWinner());
		
		testGameService.setGameChoice01("Paper");
		testGameService.setGameChoice02("Rock");
		testGameService.findTheWinner("01");
		
		assertEquals(player01, testGameService.getWinner());
	}
	
	
	@Test
	void findTheWinnerScissorsEtc() {
		
		GameService testGameService = new GameService();
		testGameService.setGamePlayer01(player01);
		testGameService.setGamePlayer02(player02);
		
		testGameService.setGameChoice01("Scissors");
		testGameService.setGameChoice02("Rock");
		testGameService.findTheWinner("01");
		
		assertEquals(player02, testGameService.getWinner());
		
		testGameService.setGameChoice01("Scissors");
		testGameService.setGameChoice02("Paper");
		testGameService.findTheWinner("01");
		
		assertEquals(player01, testGameService.getWinner());
	}
	
	
	@Test
	void getRandomGameChoice() {
		
		GameService testGameService = new GameService();
		String randomChoice = testGameService.getRandomGameChoice();
		
		assertTrue(randomChoice.equals("Rock")||randomChoice.equals("Paper")||randomChoice.equals("Scissors"));
	}
}
