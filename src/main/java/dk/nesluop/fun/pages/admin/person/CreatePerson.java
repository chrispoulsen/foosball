package dk.nesluop.fun.pages.admin.person;

import dk.nesluop.fun.backend.dao.PersonDao;
import dk.nesluop.fun.entities.Person;
import dk.nesluop.fun.pages.admin.Index;
import dk.nesluop.fun.backend.services.RatingService;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.transaction.annotation.Transactional;

public class CreatePerson {

    @Property
    @Persist(PersistenceConstants.FLASH)
    private boolean addAnother;

    @Property
    private Person person;

    @Inject
    private RatingService ratingService;

    @Inject
    private PersonDao personDao;

    @Transactional
    Object onSuccess()
    {
        ratingService.resetRating( person );
        personDao.create( person );

        if( addAnother ) {
            return CreatePerson.class;
        } else {
            return Index.class;
        }
    }
}
