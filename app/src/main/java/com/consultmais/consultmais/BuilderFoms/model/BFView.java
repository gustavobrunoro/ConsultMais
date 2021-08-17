package com.consultmais.consultmais.BuilderFoms.model;

import com.consultmais.consultmais.BuilderFoms.utils.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = Constants.JSON_KEY_TYPE, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(name = Constants.TYPE_TEXT_VIEW, value = BFTextView.class),
        @JsonSubTypes.Type(name = Constants.TYPE_EDIT_TEXT, value = BFEditText.class),
        @JsonSubTypes.Type(name = Constants.TYPE_CHECKBOX, value = BFCheckbox.class),
        @JsonSubTypes.Type(name = Constants.TYPE_CHECKBOX_GROUP, value = BFCheckboxGroup.class),
        @JsonSubTypes.Type(name = Constants.TYPE_RADIO_GROUP, value = BFRadioGroup.class),
        @JsonSubTypes.Type(name = Constants.TYPE_RADIO_GROUP_RATINGS, value = BFRadioGroupRatings.class),
        @JsonSubTypes.Type(name = Constants.TYPE_DROP_DOWN_LIST, value = BFDropDownList.class),
        @JsonSubTypes.Type(name = Constants.TYPE_DATE_PICKER, value = BFDatePicker.class),
        @JsonSubTypes.Type(name = Constants.TYPE_TIME_PICKER, value = BFTimePicker.class),
        @JsonSubTypes.Type(name = Constants.TYPE_SWITCH, value = BFSwitch.class),
        @JsonSubTypes.Type(name = Constants.TYPE_SECTION_BREAK, value = BFSectionBreak.class),
})
public class BFView {
    @JsonProperty(Constants.JSON_KEY_TYPE)
    private String type;
    private int tag;
    private int value;
    private boolean obrig;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTag () {
        return tag;
    }

    public void setTag (int tag) {
        this.tag = tag;
    }

    public int getValue () {
        return value;
    }

    public void setValue (int value) {
        this.value = value;
    }

    public boolean isObrig () {
        return obrig;
    }

    public void setObrig (boolean obrig) {
        this.obrig = obrig;
    }
}
