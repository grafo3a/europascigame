package net.apasajb.europascigame.dao;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.stereotype.Component;


@Entity
@Table(name="players")
@Component
public class Player implements Serializable {
	
	private static final long serialVersionUID = 966413440546825670L;
	
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int col01Id;
	
	@Column(name="username", length=20, nullable=false)
	private String col02Username;
	
	@Column(name="points", length=5, nullable=false)
	private int col03Points;
	
	
	public Player() {	//Constructeur par defaut
		super();
	}
	
	
	public Player(String col02Username, int col03Points) {
		super();
		this.col02Username = col02Username;
		this.col03Points = col03Points;
	}
	
	
	//=================== GETTERS & SETTERS =================//
	
	public int getCol01Id() {
		return col01Id;
	}


	public void setCol01Id(int col01Id) {
		this.col01Id = col01Id;
	}


	public String getCol02Username() {
		return col02Username;
	}


	public void setCol02Username(String col02Username) {
		this.col02Username = col02Username;
	}


	public int getCol03Points() {
		return col03Points;
	}


	public void setCol03Points(int col03Points) {
		this.col03Points = col03Points;
	}
	//========================================================//
}
