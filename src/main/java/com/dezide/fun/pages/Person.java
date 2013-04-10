package com.dezide.fun.pages;

import com.dezide.fun.backend.dao.PersonDao;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class Person
{

    @Inject
    private PersonDao personDao;

    @Property
    private com.dezide.fun.entities.Person personGridRow;

    public List<com.dezide.fun.entities.Person> getPersons()
    {
        return personDao.getPersons();
    }

}
