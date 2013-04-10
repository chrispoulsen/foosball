package com.dezide.fun.pages.admin;

import com.dezide.fun.backend.dao.MatchDao;
import com.dezide.fun.backend.dao.PersonDao;
import com.dezide.fun.backend.dao.TeamDao;
import com.dezide.fun.entities.Match;
import com.dezide.fun.entities.Person;
import com.dezide.fun.entities.Team;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class Index {

    @Property
    private Person personGridRow;

    @Property
    private Team teamGridRow;

    @Inject
    private PersonDao personDao;

    @Inject
    private TeamDao teamDao;

    @Inject
    private MatchDao matchDao;

    public List<Person> getPersons()
    {
        return personDao.getPersons();
    }

    public List<Team> getTeams() {
        return teamDao.getTeams();
    }


    @Transactional
    void onDeletePerson(Person person)
    {
        personDao.deletePerson(person);
    }


    @Transactional
    void onDeleteTeam(Team team)
    {
        for( Match match: team.getMatches() ) {
            matchDao.delete( match );
        }
        team.getMatches().clear();
        teamDao.deleteTeam(team);
    }

}
