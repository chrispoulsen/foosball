package dk.nesluop.fun.backend.services.impl;

import dk.nesluop.fun.entities.Match;
import dk.nesluop.fun.backend.model.Rateable;
import dk.nesluop.fun.backend.services.RatingService;
import jskills.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RatingServiceImpl implements RatingService {

    private GameInfo gameInfo;

    public RatingServiceImpl() {
//        GameInfo defaultGameInfo = GameInfo.getDefaultGameInfo();
//        Rating defaultRating = defaultGameInfo.getDefaultRating();
//        gameInfo = new GameInfo(defaultRating.getMean(), defaultRating.getStandardDeviation(), defaultGameInfo.getBeta(),
//                defaultGameInfo.getDynamicsFactor(), 0.0);
        gameInfo = new GameInfo( 1200, 400, 200, 4, 0 );
    }

    @Override
    public void calculateAndSetRatings(Match result) {
        Team winnerTeam = new Team();
        Team looserTeam = new Team();

        // do the players
        extractTrueskillTeam(winnerTeam, result.getWinner().getMembers().toArray(
                new Rateable[result.getWinner().getMembers().size()]));
        extractTrueskillTeam(looserTeam, result.getLooser().getMembers().toArray(
                new Rateable[result.getLooser().getMembers().size()]));

        Map<IPlayer, Rating> newRatings =
            TrueSkillCalculator.calculateNewRatings(gameInfo, Team.concat(winnerTeam, looserTeam), 1, 2);

        for(Map.Entry<IPlayer,Rating> entry: newRatings.entrySet() ) {
            Rateable p = (Rateable) entry.getKey().getId();
            Rating r = entry.getValue();
            p.setMean(r.getMean());
            p.setStandardDeviation(r.getStandardDeviation());
        }
        // do the teams
        winnerTeam.clear();
        looserTeam.clear();
        extractTrueskillTeam(winnerTeam, result.getWinner());
        extractTrueskillTeam(looserTeam, result.getLooser());

        newRatings =
            TrueSkillCalculator.calculateNewRatings(gameInfo, Team.concat(winnerTeam, looserTeam), 1, 2);

        for(Map.Entry<IPlayer,Rating> entry: newRatings.entrySet() ) {
            Rateable p = ((Rateable) entry.getKey().getId());
            Rating r = entry.getValue();
            p.setMean(r.getMean());
            p.setStandardDeviation(r.getStandardDeviation());
        }
    }

    private <T extends Rateable> void extractTrueskillTeam(Team team, T... rateables) {
        for(Rateable rateable: rateables)
        {
            team.addPlayer(new Player<Rateable>(rateable),
                    new Rating(rateable.getMean(), rateable.getStandardDeviation()));
        }
    }

    @Override
    public void resetRating(Rateable rateable) {
        Rating defaultRating = gameInfo.getDefaultRating();
        rateable.setMean(defaultRating.getMean());
        rateable.setStandardDeviation(defaultRating.getStandardDeviation());
    }


}
