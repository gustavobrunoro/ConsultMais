package com.consultmais.consultmais.BuilderFoms.model;

import com.consultmais.consultmais.BuilderFoms.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BFRadioGroupRatings extends BFView {

    @JsonProperty(Constants.JSON_KEY_DESCRIPTION)
    private String description;

    @JsonProperty(Constants.JSON_KEY_MIN_RATINGS)
    private int minRating;

    @JsonProperty(Constants.JSON_KEY_IN_STEPS_OF)
    private int inStepsOf;

    @JsonProperty(Constants.JSON_KEY_NUMBER_OF_RATINGS)
    private int numberOfRatings;

    @JsonIgnore
    private int rating;

    public int getMinRating() {
        return minRating;
    }

    public void setMinRating(int minRating) {
        this.minRating = minRating;
    }

    public int getInStepsOf() {
        return inStepsOf;
    }

    public void setInStepsOf(int inStepsOf) {
        this.inStepsOf = inStepsOf;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
