package dk.nesluop.fun.backend.services;

import dk.nesluop.fun.entities.Match;
import dk.nesluop.fun.backend.model.Rateable;

public interface RatingService {

    void calculateAndSetRatings(Match result);

    void resetRating(Rateable rateable);
}
