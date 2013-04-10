package dk.nesluop.fun.backend.dao;


import dk.nesluop.fun.entities.Person;
import dk.nesluop.fun.entities.Team;
import dk.nesluop.fun.entities.Team_;
import dk.nesluop.fun.backend.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.*;

@Repository
public class TeamDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RatingService ratingService;

    public Team getTeam(Person... members) {
        List<Person> teamMembers = new ArrayList<Person>();
        List<Long> teamMemberIds = new ArrayList<Long>();
        // strip out null values
        for(Person member:members) {
            if(member != null)
            {
                teamMembers.add(member);
                teamMemberIds.add(member.getId());
            }
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> q = cb.createQuery(Tuple.class);
        Root<Team> team = q.from(Team.class);

        Join<Team,Person> member1 = team.join(Team_.members);

        q.multiselect(team.get(Team_.id));

//        ParameterExpression<Long> person1Id = cb.parameter(Long.class);
        q.where(
//            cb.equal(person1Id, member1.get(Person_.id))
//            member1.get(Person_.id).in( teamMemberIds)
                member1.in(teamMembers)
        );

        q.groupBy(team.get(Team_.id));
        q.having(cb.equal(cb.count(team.get(Team_.members)),teamMembers.size()));

        TypedQuery<Tuple> query = em.createQuery(q);

//        query.setParameter(person1Id,teamMembers.get(0).getId());

        List<Tuple> result = query.getResultList();

        if( result.size() == 1) {
            // found id fetch it...
            CriteriaQuery<Team> cq = cb.createQuery(Team.class);
            Root<Team> tr = cq.from(Team.class);
            ParameterExpression<Long> teamIdParam = cb.parameter(Long.class);
            cq.where(cb.equal(tr.get(Team_.id),teamIdParam));
            return em.createQuery(cq).setParameter(teamIdParam, ((Long) result.get(0).get(0))).getSingleResult();
        } else {
            return createTeam(teamMembers);
        }
    }

    private Team createTeam(List<Person> members) {
        String generatedName = "Team ";
        for(int i=members.size()-1;i >= 0;i--) {
            Person p = members.get(i);
            generatedName += p.getName();
            if( i > 1 ) {
                generatedName += ", ";
            } else if( i == 1) {
                generatedName += " and ";
            }
        }

        Team t = new Team();
        ratingService.resetRating( t );
        t.setName(generatedName);
        t.setMembers(new HashSet<>(members));
        em.persist(t);

        for(Person p: members) {
            p.addTeam(t);
            em.merge(p);
        }

        return t;
    }

    public List<Team> getTeams() {
        CriteriaQuery<Team> pc = em.getCriteriaBuilder().createQuery(Team.class);
        return em.createQuery(pc).getResultList();
    }

    public void deleteTeam(Team team) {
        em.remove( em.merge( team ) );
    }

}
