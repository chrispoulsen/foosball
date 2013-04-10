package com.dezide.fun.pages.admin.person;

import com.dezide.fun.backend.dao.PersonDao;
import com.dezide.fun.entities.Person;
import com.dezide.fun.pages.admin.Index;
import com.dezide.fun.backend.services.RatingService;
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
