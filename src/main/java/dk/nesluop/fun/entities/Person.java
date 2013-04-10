package dk.nesluop.fun.entities;


import dk.nesluop.fun.backend.model.Rateable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Person implements Serializable, Rateable {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private double mean = 100;

    @NotNull
    private double standardDeviation;

    @ManyToMany
    @JoinTable(
            name="PERSON2TEAM",
            joinColumns={@JoinColumn(name="PERSON_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="TEAM_ID", referencedColumnName="ID")})
    private Set<Team> teams;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getMean() {
        return mean;
    }

    @Override
    public void setMean(double mean) {
        this.mean = mean;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    @Override
    public double getStandardDeviation() {
        return standardDeviation;
    }

    @Override
    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (id != null ? !id.equals(person.id) : person.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
