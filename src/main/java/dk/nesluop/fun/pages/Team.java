package dk.nesluop.fun.pages;

import dk.nesluop.fun.backend.dao.TeamDao;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class Team
{

    @Inject
    private TeamDao teamDao;

    @Property
    private dk.nesluop.fun.entities.Team teamGridRow;

    public List<dk.nesluop.fun.entities.Team> getTeams()
    {
        return teamDao.getTeams();
    }
}
