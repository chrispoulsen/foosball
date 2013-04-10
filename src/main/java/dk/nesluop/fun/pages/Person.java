package dk.nesluop.fun.pages;

import dk.nesluop.fun.backend.dao.PersonDao;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class Person
{

    @Inject
    private PersonDao personDao;

    @Property
    private dk.nesluop.fun.entities.Person personGridRow;

    public List<dk.nesluop.fun.entities.Person> getPersons()
    {
        return personDao.getPersons();
    }

}
