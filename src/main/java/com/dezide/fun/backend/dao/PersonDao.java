package com.dezide.fun.backend.dao;

import com.dezide.fun.entities.Person;
import com.dezide.fun.entities.Person_;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@Repository
public class PersonDao {

    @PersistenceContext
    private EntityManager em;

    public Person getPersonByName(String name) {
        if(name != null && !"".equals(name)) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Person> query = cb.createQuery(Person.class);
            Root<Person> person = query.from(Person.class);
            query.where(cb.equal(person.get(Person_.name), cb.literal(name)));
            return em.createQuery(query).getSingleResult();
        } else {
            return null;
        }
    }

    public List<String> getAutoCompleteSuggestions(String partialName, List<String> exclusions) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<Person> person = query.from(Person.class);
        query.select(person.get(Person_.name));
        // name matching
        Predicate whereClause = null;
        if (partialName != null) {
            whereClause = cb.like(cb.upper(person.get(Person_.name)), cb.upper(cb.parameter(String.class, "partialName")));
        }

        // exclude already selected...
        if (exclusions.size() > 0) {
            Predicate exclusionPredicate = cb.not(person.get(Person_.name).in(exclusions));
            if (whereClause == null) {
                whereClause = exclusionPredicate;
            } else {
                whereClause = cb.and(whereClause, exclusionPredicate);
            }
        }
        query.where(whereClause).orderBy(cb.asc(person.get(Person_.name)));

        TypedQuery<String> q = em.createQuery(query);
        q.setParameter("partialName", String.format("%%%s%%", partialName));
        return q.getResultList();
    }

    public List<Person> getPersons() {
        CriteriaQuery<Person> pc = em.getCriteriaBuilder().createQuery(Person.class);
        return em.createQuery(pc).getResultList();
    }


    public void deletePerson(Person person) {
        em.remove( em.merge( person ) );
    }

    public void create( Person person ) {
        em.persist( person );
    }

}
