package com.dezide.fun.pages;

import com.dezide.fun.backend.dao.TeamDao;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class Team
{

    @Inject
    private TeamDao teamDao;

    @Property
    private com.dezide.fun.entities.Team teamGridRow;

    public List<com.dezide.fun.entities.Team> getTeams()
    {
        return teamDao.getTeams();
    }
}
