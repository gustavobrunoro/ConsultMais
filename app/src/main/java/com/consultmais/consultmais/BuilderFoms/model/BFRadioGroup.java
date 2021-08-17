package com.consultmais.consultmais.BuilderFoms.model;

import com.consultmais.consultmais.BuilderFoms.utils.Constants;
import com.consultmais.consultmais.Model.Perguntaopcao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BFRadioGroup extends BFView {

    @JsonProperty(Constants.JSON_KEY_DESCRIPTION)
    private String description;

    @JsonProperty(Constants.JSON_KEY_OPTIONS)
    private List<Perguntaopcao> options;

    @JsonIgnore
    private String selectedOption;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Perguntaopcao> getOptions() {
        return options;
    }

    public void setOptions(List<Perguntaopcao> options) {
        this.options = options;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }


}
