package net.apasajb.europascigame.pojo;


public class GameRequest {

	private String playerMessage;
	
	
	public GameRequest() { // constructeur
		super();
	}
	
	
	public GameRequest(String playerMessage) { // constructeur
		super();
		this.playerMessage = playerMessage;
	}
	
	
	//==================== GETTERS & SETTERS ====================//
	
	public String getPlayerMessage() {
		return playerMessage;
	}


	public void setPlayerMessage(String playerMessage) {
		this.playerMessage = playerMessage;
	}
	
	//============================================================//
}
