package net.apasajb.europascigame.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PlayersRepository extends JpaRepository<Player, Integer> {
	
	// Une requete JPQL
	@Query(value="SELECT p.col02Username, p.col03Points FROM Player p ORDER BY p.col03Points DESC")
	ArrayList<String[]> showPlayersWithPoints();
	
	Player findByCol02Username(String username);
}
