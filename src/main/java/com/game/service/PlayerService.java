package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public interface PlayerService {

    Page<Player> getAllPlayers(Specification<Player> specification , Pageable pageable);
    Long getPlayersCount (Specification<Player> specification);
    Player getPlayerById(Long id);
    Player createPlayer(Player player);
    void deletePlayer(Long id);
    Player updatePlayer(Long id, Player player);

}
