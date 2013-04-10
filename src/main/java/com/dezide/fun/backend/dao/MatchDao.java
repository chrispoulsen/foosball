package com.dezide.fun.backend.dao;

import com.dezide.fun.entities.Match;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MatchDao {

    @PersistenceContext
    private EntityManager em;

    public void saveMatch(Match match) {
        em.persist(match);
    }

    public void delete( Match match ) {
        em.remove( em.merge( match ) );
    }

}
