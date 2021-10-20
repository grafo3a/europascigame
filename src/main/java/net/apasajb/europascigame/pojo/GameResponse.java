package net.apasajb.europascigame.pojo;


/**
 * Represents a response object to be sent back to JavaScript.
 * @author ApasaJB
 */

public class GameResponse {

	private String serverMessage;
	
	
	public GameResponse() {	// constructeur
		super();
	}
	
	
	public GameResponse(String serverMessage) {	// constructeur
		super();
		this.serverMessage = serverMessage;
	}
	
	
	//==================== GETTERS & SETTERS ====================//
	
	public String getServerMessage() {
		return serverMessage;
	}
	
	
	public void setServerMessage(String serverMessage) {
		this.serverMessage = serverMessage;
	}
	
	//============================================================//
}
