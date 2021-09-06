package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Calendar;
import java.util.Date;


@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void checkId(Long id) {
        if (id <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is not valid");
    }

    public void checkName(String name) {
        if (name == null || name.isEmpty() || name.length() > 12) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is not valid");
    }

    public void checkTitle(String title) {
        if (title == null || title.isEmpty() || title.length() > 30)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Title is not valid");
    }

    public void checkRace(Race race) {
        if (race == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Race is not valid");
    }

    public void checkProfession(Profession profession) {
        if (profession == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Profession is not valid");

    }

    public void checkExperience(Integer experience) {
        if (experience < 0 || experience > 10000000)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Experience is not valid");
    }

    public void checkBirthday(Date birthday) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(birthday.getTime());
        if (calendar.get(Calendar.YEAR) < 2000L || calendar.get(Calendar.YEAR) > 3000L)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Birthday is not valid");
    }

    public Integer getCurrentLevel(Integer experience) {
        return (((int) Math.sqrt(2500 + 200 * experience)) - 50) / 100;
    }

    public Integer getExperienceUntilNextLevel(Integer experience, Integer level) {
        return 50 * (level + 1) * (level + 2) - experience;
    }


    @Override
    public Page<Player> getAllPlayers(Specification<Player> specification, Pageable pageable) {
        return playerRepository.findAll(specification, pageable);
    }

    @Override
    public Long getPlayersCount(Specification<Player> specification) {
        return playerRepository.count(specification);
    }

    @Override
    public Player getPlayerById(Long id) {
        checkId(id);
        return playerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found"));
    }

    @Override
    public void deletePlayer(Long id) {
        checkId(id);
        Player player = getPlayerById(id);
        playerRepository.delete(player);
    }

    @Override
    public Player updatePlayer(Long id, Player player) {

        Player playerToUpdate = getPlayerById(id);

        if (player.getName() != null) {
            checkName(player.getName());
            playerToUpdate.setName(player.getName());
        }

        if (player.getTitle() != null) {
            checkTitle(player.getTitle());
            playerToUpdate.setTitle(player.getTitle());
        }

        if (player.getRace() != null) {
            checkRace(player.getRace());
            playerToUpdate.setRace(player.getRace());
        }

        if (player.getProfession() != null) {
            checkProfession(player.getProfession());
            playerToUpdate.setProfession(player.getProfession());
        }

        if (player.getBirthday() != null) {
            checkBirthday(player.getBirthday());
            playerToUpdate.setBirthday(player.getBirthday());
        }

        if (player.getIsBanned() != null) {
            playerToUpdate.setBanned(player.getIsBanned());
        }

        if (player.getExperience() != null) {
            checkExperience(player.getExperience());
            playerToUpdate.setExperience(player.getExperience());
        }

        playerToUpdate.setLevel(getCurrentLevel(playerToUpdate.getExperience()));
        playerToUpdate.setUntilNextLevel(getExperienceUntilNextLevel(playerToUpdate.getExperience(), playerToUpdate.getLevel()));
        return playerRepository.save(playerToUpdate);

    }

    @Override
    public Player createPlayer(Player player) {
        checkName(player.getName());
        checkTitle(player.getTitle());
        checkRace(player.getRace());
        checkProfession(player.getProfession());
        checkExperience(player.getExperience());
        checkBirthday(player.getBirthday());
        if (player.getIsBanned() == null) player.setBanned(false);
        player.setLevel(getCurrentLevel(player.getExperience()));
        player.setUntilNextLevel(getExperienceUntilNextLevel(player.getExperience(), player.getLevel()));
        return playerRepository.saveAndFlush(player);
    }


}



