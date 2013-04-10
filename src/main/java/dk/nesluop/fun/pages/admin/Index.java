package dk.nesluop.fun.pages.admin;

import dk.nesluop.fun.backend.dao.MatchDao;
import dk.nesluop.fun.backend.dao.PersonDao;
import dk.nesluop.fun.backend.dao.TeamDao;
import dk.nesluop.fun.entities.Match;
import dk.nesluop.fun.entities.Person;
import dk.nesluop.fun.entities.Team;
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
