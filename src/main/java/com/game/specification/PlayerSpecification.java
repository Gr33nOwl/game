package com.game.specification;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.domain.Specification;
import java.util.Date;

public class PlayerSpecification {

    public static Specification<Player> filterByName(String name) {
        if (name == null) return null;
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%"));
    }

    public static Specification<Player> filterByTitle(String title) {
        if (title == null) return null;
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%"));
    }

    public static Specification<Player> filterByRace(Race race) {
        if (race == null) return null;
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("race"), race));
    }

    public static Specification<Player> filterByProfession(Profession profession) {
        if (profession == null) return null;
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("profession"), profession));
    }

    public static Specification<Player> filterByBirthDay (Long after, Long before){
        if (after == null && before == null) return null;
        if (after == null) return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), new Date(before));
        if (before == null) return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after));
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("birthday"), new Date(after), new Date(before));
    }

    public static Specification<Player> filterByBanned (Boolean banned){
        if (banned == null) return null;
        if (banned) return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("banned"));
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("banned"));
    }

    public static Specification<Player> filterByExperience (Integer minExperience, Integer maxExperience){
        if (minExperience == null && maxExperience == null) return null;
        if (minExperience == null) return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExperience);
        if (maxExperience == null) return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExperience);
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("experience"), minExperience, maxExperience);
    }

    public static Specification<Player> filterByLevel (Integer minLevel, Integer maxLevel){
        if (minLevel == null && maxLevel == null) return null;
        if (minLevel == null) return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("level"), maxLevel);
        if (maxLevel == null) return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("level"), minLevel);
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get("level"), minLevel, maxLevel);
    }


}
