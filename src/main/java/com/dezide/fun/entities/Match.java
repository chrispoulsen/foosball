package com.dezide.fun.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class Match implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Timestamp playedDate = new Timestamp(System.currentTimeMillis());

    @NotNull
    @ManyToOne
    private Team winner;

    @NotNull
    @ManyToOne
    private Team looser;

    public Timestamp getPlayedDate() {
        return playedDate;
    }

    public void setPlayedDate(Timestamp playedDate) {
        this.playedDate = playedDate;
    }

    public Team getWinner() {
        return winner;
    }

    public void setWinner(Team winner) {
        this.winner = winner;
    }

    public Team getLooser() {
        return looser;
    }

    public void setLooser(Team looser) {
        this.looser = looser;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        if (id != null ? !id.equals(match.id) : match.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @PreRemove
    public void preRemove()
    {
        getWinner().getMatches().remove( this );
        getLooser().getMatches().remove( this );
    }
}
