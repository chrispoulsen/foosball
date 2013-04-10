package dk.nesluop.fun.entities;

import dk.nesluop.fun.backend.model.Rateable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
public class Team implements Serializable, Rateable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private double mean = 100;

    @NotNull
    private double standardDeviation;

    @NotNull
    @ManyToMany( mappedBy = "teams" )
    private Set<Person> members;

    @OneToMany
    private List<Match> matches;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Person> getMembers() {
        return members;
    }

    public void setMembers(Set<Person> members) {
        this.members = members;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    @Override
    public double getMean() {
        return mean;
    }

    @Override
    public void setMean(double mean) {
        this.mean = mean;
    }

    @Override
    public double getStandardDeviation() {
        return standardDeviation;
    }

    @Override
    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public Long getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        if (id != null ? !id.equals(team.id) : team.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
