package com.dezide.fun.backend.services.impl;

import com.dezide.fun.backend.dao.MatchDao;
import com.dezide.fun.backend.dao.PersonDao;
import com.dezide.fun.backend.dao.TeamDao;
import com.dezide.fun.entities.Match;
import com.dezide.fun.entities.Person;
import com.dezide.fun.entities.Team;
import com.dezide.fun.backend.services.MatchService;
import com.dezide.fun.backend.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service
public class MatchServiceImpl implements MatchService {

    private final PersonDao personDao;
    private final TeamDao teamDao;
    private final MatchDao matchDao;
    private final RatingService ratingService;

//    @Autowired
    public MatchServiceImpl(PersonDao personDao, TeamDao teamDao, MatchDao matchDao, RatingService ratingService) {
        this.personDao = personDao;
        this.teamDao = teamDao;
        this.matchDao = matchDao;
        this.ratingService = ratingService;
    }

    @Override
    @Transactional
    public void recordMatchResult(String winner1, String winner2, String looser1, String looser2) {
        Person win1 = personDao.getPersonByName(winner1);
        Person win2 = personDao.getPersonByName(winner2);
        Person los1 = personDao.getPersonByName(looser1);
        Person los2 = personDao.getPersonByName(looser2);
        Team winner = teamDao.getTeam(win1, win2);
        Team looser = teamDao.getTeam(los1, los2);
        Match match = new Match();
        match.setWinner(winner);
        match.setLooser(looser);
        winner.getMatches().add(match);
        looser.getMatches().add(match);
        ratingService.calculateAndSetRatings(match);
        matchDao.saveMatch(match);
    }
}
