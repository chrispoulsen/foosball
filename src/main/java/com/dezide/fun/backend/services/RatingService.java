package com.dezide.fun.backend.services;

import com.dezide.fun.entities.Match;
import com.dezide.fun.backend.model.Rateable;

public interface RatingService {

    void calculateAndSetRatings(Match result);

    void resetRating(Rateable rateable);
}
