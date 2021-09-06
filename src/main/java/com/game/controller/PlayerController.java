package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;

import static com.game.specification.PlayerSpecification.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.springframework.data.jpa.domain.Specification.*;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


    @GetMapping("/players")
    public ResponseEntity<List<Player>> getPlayersList(@RequestParam(value = "name", required = false) String name,
                                                       @RequestParam(value = "title", required = false) String title,
                                                       @RequestParam(value = "race", required = false) Race race,
                                                       @RequestParam(value = "profession", required = false) Profession profession,
                                                       @RequestParam(value = "after", required = false) Long after,
                                                       @RequestParam(value = "before", required = false) Long before,
                                                       @RequestParam(value = "banned", required = false) Boolean banned,
                                                       @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                       @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                       @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                       @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                                       @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder playerOrder,
                                                       @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
                                                       @RequestParam(value = "pageSize", defaultValue = "3") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(playerOrder.getFieldName()));
        Specification<Player> specifications =
                where(filterByName(name)).
                and(filterByTitle(title)).
                and(filterByRace(race)).
                and(filterByProfession(profession)).
                and(filterByBirthDay(after, before)).
                and(filterByBanned(banned)).
                and(filterByExperience(minExperience, maxExperience)).
                and(filterByLevel(minLevel, maxLevel));

        return new ResponseEntity<>(playerService.getAllPlayers(specifications, pageable).getContent(), HttpStatus.OK);
    }

    @GetMapping("/players/count")
    public ResponseEntity<Long> getPlayersCount(@RequestParam(value = "name", required = false) String name,
                                                       @RequestParam(value = "title", required = false) String title,
                                                       @RequestParam(value = "race", required = false) Race race,
                                                       @RequestParam(value = "profession", required = false) Profession profession,
                                                       @RequestParam(value = "after", required = false) Long after,
                                                       @RequestParam(value = "before", required = false) Long before,
                                                       @RequestParam(value = "banned", required = false) Boolean banned,
                                                       @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                                       @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                                       @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                                       @RequestParam(value = "maxLevel", required = false) Integer maxLevel){

        Specification<Player> specifications =
                where(filterByName(name)).
                        and(filterByTitle(title)).
                        and(filterByRace(race)).
                        and(filterByProfession(profession)).
                        and(filterByBirthDay(after, before)).
                        and(filterByBanned(banned)).
                        and(filterByExperience(minExperience, maxExperience)).
                        and(filterByLevel(minLevel, maxLevel));

        return new ResponseEntity<>(playerService.getPlayersCount(specifications), HttpStatus.OK);
    }


    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable("id") Long id) {
        return new ResponseEntity<>(playerService.getPlayerById(id), HttpStatus.OK);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable("id") Long id) {
        playerService.deletePlayer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/players")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        return new ResponseEntity<>(playerService.createPlayer(player), HttpStatus.OK);
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") Long id, @RequestBody Player player) {
        return new ResponseEntity<>(playerService.updatePlayer(id, player), HttpStatus.OK);
    }

}
