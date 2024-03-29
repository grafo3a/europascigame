package net.apasajb.europascigame.pojo;


/**
 * Represents a request object coming from JavaScript.
 * @author ApasaJB
 */
public class GameRequest {
	
	private String playerMessage;
	
	public GameRequest() {
		super();
	}
	
	public GameRequest(String playerMessage) {
		
		super();
		this.playerMessage = playerMessage;
	}
	
	//=========================== GETTERS & SETTERS //
	public String getPlayerMessage() {
		return playerMessage;
	}
	
	public void setPlayerMessage(String playerMessage) {
		this.playerMessage = playerMessage;
	}
	//================================================//
}
